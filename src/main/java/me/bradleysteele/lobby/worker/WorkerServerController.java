/*
 * Copyright 2018 Bradley Steele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.bradleysteele.lobby.worker;

import com.google.common.collect.Sets;
import me.bradleysteele.commons.register.worker.BWorker;
import me.bradleysteele.lobby.resource.yml.Config;
import me.bradleysteele.lobby.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Bradley Steele
 */
public class WorkerServerController extends BWorker {

    private static final WorkerServerController instance = new WorkerServerController();

    public static WorkerServerController get() {
        return instance;
    }

    private final Set<World> excluded = Sets.newHashSet();

    private WorkerServerController() {
        this.setPeriod(6000L);
        this.setSync(false);
    }

    @Override
    public void run() {
        if (Config.SERVER_DISABLE_NIGHT.getAsBoolean()) {
            Bukkit.getWorlds().stream()
                    .filter(world -> !isExcludedWorld(world))
                    .forEach(world -> world.setTime(0));
        }
    }

    @Override
    public void onRegister() {
        setExcludedWorlds(Config.SERVER_EXCLUDED_WORLDS.getAsStringList().stream()
                .map(Bukkit::getWorld)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
    }

    @Override
    public void onUnregister() {
        excluded.clear();
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(isApplicable(Config.SERVER_DISABLE_ENTITY_SPAWN, event.getLocation().getWorld()));
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState() && isApplicable(Config.SERVER_DISABLE_WEATHER, event.getWorld()));
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() == EntityType.PLAYER
                && isApplicable(Config.SERVER_DISABLE_HUNGER, (Player) event.getEntity())) {
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        event.setCancelled(event.getEntityType() == EntityType.PLAYER
                && isApplicable(Config.SERVER_DISABLE_DAMAGE, (Player) event.getEntity()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(isApplicable(Config.SERVER_DISABLE_BLOCK_BREAK, event.getPlayer()));

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(isApplicable(Config.SERVER_DISABLE_BLOCK_PLACE, event.getPlayer()));
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(isApplicable(Config.SERVER_DISABLE_PICKUP, event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(isApplicable(Config.SERVER_DISABLE_DROP, event.getPlayer()));
    }

    /**
     * An excluded {@link World} is a world which settings from
     * the {@link Config} will not be applied to.
     *
     * @return unmodifiable set of excluded worlds.
     */
    public Set<World> getExcludedWorlds() {
        return Collections.unmodifiableSet(excluded);
    }

    /**
     * @param world the world to check.
     * @return {@code true} if the world is excluded.
     */
    public boolean isExcludedWorld(World world) {
        return excluded.contains(world);
    }

    /**
     * @param worlds excluded worlds
     */
    public void setExcludedWorlds(Collection<World> worlds) {
        excluded.clear();
        excluded.addAll(worlds);
    }

    /**
     * @param world the world to exclude.
     */
    public void addExcludedWorld(World world) {
        excluded.add(world);
    }

    /**
     * @param world the world to remove from exclusion.
     */
    public void removeExcludedWorld(World world) {
        excluded.remove(world);
    }

    private boolean isApplicable(Config setting, World world) {
        return setting.getAsBoolean() && !isExcludedWorld(world);
    }

    private boolean isApplicable(Config setting, Player player) {
        return isApplicable(setting, player.getWorld()) && !player.hasPermission(Permissions.BYPASS);
    }
}
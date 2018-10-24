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

import me.bradleysteele.commons.register.worker.BWorker;
import me.bradleysteele.commons.resource.Resource;
import me.bradleysteele.lobby.resource.ResourceType;
import me.bradleysteele.lobby.resource.Resources;
import me.bradleysteele.lobby.resource.yml.Config;
import me.bradleysteele.lobby.util.Locations;
import org.bukkit.GameMode;
import org.bukkit.Location;

/**
 * @author Bradley Steele
 */
public class WorkerLocations extends BWorker {

    private Location spawn;
    private GameMode gamemode;

    @Override
    public void onRegister() {
        String gm = Config.SPAWN_GAMEMODE.getAsString().toUpperCase();

        try {
            gamemode = GameMode.valueOf(gm);
        } catch (IllegalArgumentException e) {
            gamemode = GameMode.SURVIVAL;
            plugin.getConsole().warn(String.format("Invalid spawn gamemode &c%s&7, falling back to Survival.", gm));
        }

        Resource resource = Resources.get().getResource(ResourceType.LOCATIONS);

        spawn = Locations.parseLocation(resource.getSection("spawn"));
        // warps
    }

    /**
     * @return the spawn location.
     */
    public Location getSpawnLocation() {
        return spawn;
    }

    /**
     * @return gamemode players are set to when being joining
     *         the server and being sent to spawn.
     */
    public GameMode getSpawnGamemode() {
        return gamemode;
    }

    /**
     * @param location the spawn location.
     * @param update   if the spawn should be saved.
     */
    public void setSpawn(Location location, boolean update) {
        spawn = location;

        if (update) {
            Resource resource = Resources.get().getResource(ResourceType.LOCATIONS);
            Locations.toResourceSection(resource.getSection("spawn"), location);

            plugin.getResourceProvider().saveResource(resource);
        }
    }

    /**
     * @param location the spawn location.
     */
    public void setSpawn(Location location) {
        setSpawn(location, false);
    }

    /**
     * @param gamemode the gamemode to set players.
     */
    public void setGamemode(GameMode gamemode) {
        this.gamemode = gamemode;
    }
}
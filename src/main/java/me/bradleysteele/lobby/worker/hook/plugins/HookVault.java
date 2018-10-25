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

package me.bradleysteele.lobby.worker.hook.plugins;

import com.google.common.collect.Maps;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.lobby.resource.yml.Config;
import me.bradleysteele.lobby.worker.hook.AbstractHook;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Bradley Steele
 */
public class HookVault extends AbstractHook {

    private final Map<String, String> formats = Maps.newHashMap();

    private Plugin plugin;
    private Chat chat;

    @Override
    public void hook() {
        hooked = false;
        formats.clear();

        ResourceSection section = Config.SERVER_CHAT_FORMAT_GROUPS.getAsResourceSection();

        if (section != null) {
            formats.putAll(section.getKeys().stream()
                    .collect(Collectors.toMap(key -> key, section::getString)));
        }

        plugin = Bukkit.getPluginManager().getPlugin("Vault");

        if (plugin != null) {
            chat = Bukkit.getServicesManager().getRegistration(Chat.class).getProvider();
            hooked = true;
        }
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * @return unmodifiable map of formats (group:format).
     */
    public Map<String, String> getFormats() {
        return Collections.unmodifiableMap(formats);
    }

    /**
     * @param player  message sender.
     * @param message the message.
     * @return formatted format using the provided player.
     */
    public String format(Player player, String message) {
        String format = formats.getOrDefault(chat.getPrimaryGroup(player), Config.SERVER_CHAT_FORMAT_DEFAULT.getAsString());
        return format
                .replace("{prefix}", chat.getPlayerPrefix(player))
                .replace("{name}", player.getName())
                .replace("{message}", message);
    }

    public String format(AsyncPlayerChatEvent event) {
        return format(event.getPlayer(), event.getMessage());
    }
}
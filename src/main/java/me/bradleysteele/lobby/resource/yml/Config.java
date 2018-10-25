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

package me.bradleysteele.lobby.resource.yml;

import com.google.common.collect.Lists;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.commons.resource.type.ResourceYaml;
import me.bradleysteele.lobby.resource.ResourceType;
import me.bradleysteele.lobby.resource.Resources;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bradley Steele
 */
public enum Config {

    CONFIG_VERSION("config-version", 1),

    SERVER_EXCLUDED_WORLDS("server.excluded-worlds", Collections.emptyList()),
    SERVER_DISABLE_ENTITY_SPAWN("server.disable-entity-spawn", true),
    SERVER_DISABLE_NIGHT("server.disable-night", true),
    SERVER_DISABLE_WEATHER("server.disable-weather", true),
    SERVER_DISABLE_HUNGER("server.disable-hunger", true),
    SERVER_DISABLE_DAMAGE("server.disable-damage", true),
    SERVER_DISABLE_BLOCK_BREAK("server.disable-block-break", true),
    SERVER_DISABLE_BLOCK_PLACE("server.disable-block-place", true),
    SERVER_DISABLE_PICKUP("server.disable-pickup", true),
    SERVER_DISABLE_DROP("server.disable-drop", true),
    SERVER_CHAT_HANDLE("server.chat.handle", true),
    SERVER_CHAT_DISABLE("server.chat.disable", false),
    SERVER_CHAT_FORMAT_DEFAULT("server.chat.format.default", "{name}&f: {message}"),
    SERVER_CHAT_FORMAT_GROUPS("server.chat.format.groups", ""),

    JOIN_MESSAGE("join.message", ""),
    JOIN_MOTD("join.motd", Collections.emptyList()),
    JOIN_SPAWN("join.spawn", true),

    QUIT_MESSAGE("quit.message", ""),

    SPAWN_GAMEMODE("spawn.gamemode", "ADVENTURE"),
    SPAWN_ON_VOID("spawn.on-void", true),
    SPAWN_ON_ENABLE("spawn.on-enable", true),
    SPAWN_COMMAND("spawn.command", true);

    public static ResourceYaml getConfig() {
        return (ResourceYaml) Resources.get().getResource(ResourceType.CONFIG);
    }

    private final String path;
    private final Object def;

    /**
     * @param path the path to the setting.
     * @param def  default value of the setting.
     */
    Config(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    /**
     * @return path of the setting in config.yml.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return value of the path as a string, also replaces the {prefix} placeholder.
     */
    public String getAsString() {
        return getConfig().getString(path, String.valueOf(def));
    }

    /**
     * @return value of the path as a string list.
     */
    public List<String> getAsStringList() {
        List<String> list = Lists.newArrayList();

        if (getConfig().getConfiguration().isString(path)) {
            list.add(getAsString());
        } else {
            list.addAll(getConfig().getConfiguration().getStringList(path));
        }

        return list.stream()
                .map(s -> s.replace("{prefix}", Locale.PREFIX.getMessage().get(0)))
                .collect(Collectors.toList());
    }

    /**
     * @return value of the path as an int.
     */
    public int getAsInt() {
        return getConfig().getInt(path, Integer.parseInt(getAsString()));
    }

    /**
     * @return default value as an integer.
     */
    public int getAsIntDefault() {
        return Integer.parseInt(String.valueOf(def));
    }

    /**
     * @return value of the path as a short.
     */
    public short getAsShort() {
        return getConfig().getShort(path, Short.parseShort(getAsString()));
    }

    /**
     * @return value of the path as a double.
     */
    public double getAsDouble() {
        return getConfig().getDouble(path, Double.parseDouble(getAsString()));
    }

    /**
     * @return value of the path as a boolean.
     */
    public boolean getAsBoolean() {
        return getConfig().getBoolean(path, Boolean.parseBoolean(getAsString()));
    }

    /**
     * @return value of the path as a resource section.
     */
    public ResourceSection getAsResourceSection() {
        return getConfig().getSection(path);
    }
}
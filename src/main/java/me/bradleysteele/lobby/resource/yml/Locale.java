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
import com.google.common.collect.Maps;
import me.bradleysteele.commons.resource.type.ResourceYaml;
import me.bradleysteele.commons.util.Messages;
import me.bradleysteele.lobby.resource.ResourceType;
import me.bradleysteele.lobby.resource.Resources;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bradley Steele
 */
public enum Locale {

    PREFIX("prefix", "&7[&6BLobby&7]"),

    CMD_NO_PERM("command.no-permission", "{prefix} &7You do not have permission for that command."),
    CMD_UNKNOWN("command.unknown", "{prefix} &7Unknown command! Type &e/trs help &7for a list of commands."),
    CMD_INVALID("command.invalid", "{prefix} &7Invalid usage, use &e/{args}&7."),

    CONFIG_CHANGE_AVAILABLE("config-change-available", "{prefix} &7A config change is available. Current: &c{current} &7New: &a{new}&7."),

    CHAT_DISABLED("chat-disabled", "{prefix} &7Chat is disabled.")

    ;

    public static ResourceYaml getLocale() {
        return (ResourceYaml) Resources.get().getResource(ResourceType.LOCALE);
    }

    private final String path;
    private final List<String> def;

    /**
     * @param path the path pointing to the message.
     * @param def  default value in case message is missing or null.
     */
    Locale(String path, String... def) {
        this.path = path;
        this.def = Messages.colour(def);
    }

    /**
     * @return yaml path pointing to the message.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return default value in case message is missing or {@code null}.
     */
    public List<String> getDefault() {
        return def;
    }

    /**
     * @return list of messages or {@link Locale#def} if missing.
     */
    public List<String> getMessage(Object... rep) {
        List<String> replacements = Stream.of(rep)
                .map(String::valueOf)
                .collect(Collectors.toList());

        List<String> unreplaced;

        if (getLocale().getConfiguration().isString(path)) {
            unreplaced = Lists.newArrayList(getLocale().getString(path, def.get(0)));
        } else {
            unreplaced = getLocale().getConfiguration().getStringList(path);
        }

        // Use the default if the returned is empty.
        if (unreplaced.isEmpty()) {
            unreplaced = def;
        }

        // Not replacing anything if the message is the prefix.
        if (this == PREFIX) {
            return unreplaced;
        }

        Map<String, String> replace = Maps.newHashMap();
        List<String> replaced = Lists.newArrayList();

        // Replacements list acting as a key-value map:
        // key1, value1, key2, value2, key3, value3, etc..
        List<String> temp = Lists.newArrayList(replacements);
        temp.add("{prefix}");
        temp.add(Locale.PREFIX.getMessage().get(0)); // No need to check length.

        // Convert key-value list to map.
        for (int i = 0; i < temp.size() - 1; i += 2) {
            replace.put(temp.get(i), temp.get(i + 1));
        }

        // Finally, replace.
        for (String s : unreplaced) {
            for (Map.Entry<String, String> r : replace.entrySet()) {
                s = s.replace(r.getKey(), r.getValue());
            }

            replaced.add(s);
        }

        return Messages.colour(replaced);
    }
}
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

package me.bradleysteele.lobby.sidebar;

import me.bradleysteele.commons.util.Messages;
import org.bukkit.ChatColor;

/**
 * @author Bradley Steele
 */
public class SidebarLine {

    private static final String COLOUR_CHAR = String.valueOf(ChatColor.COLOR_CHAR);

    private String prefix = "";
    private String suffix = "";

    public SidebarLine(String text) {
        setText(text);
    }

    /**
     * @return first half of the sidebar line.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @return second half of the sidebar line.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param text the text to display.
     */
    public void setText(String text) {
        text = Messages.colour(text);

        if (text.length() > 16) {
            prefix = text.substring(0, 16);
            suffix = text.substring(16, text.length());

            if (prefix.endsWith(COLOUR_CHAR)) {
                prefix = prefix.substring(0,  prefix.length() - 1);
                suffix = COLOUR_CHAR + suffix;
            }

            suffix = ChatColor.getLastColors(prefix) + suffix;

            // Chop < 32
            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
        } else {
            prefix = text;
        }
    }
}
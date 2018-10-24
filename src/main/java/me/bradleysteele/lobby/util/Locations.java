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

package me.bradleysteele.lobby.util;

import me.bradleysteele.commons.resource.ResourceSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Bradley Steele
 */
public final class Locations {

    public static Location parseLocation(ResourceSection section) {
        if (section == null) {
            return null;
        }

        World world = Bukkit.getWorld(section.getString("world"));

        if (world == null) {
            world = Bukkit.getWorlds().get(0);
        }

        return new Location(world,
                section.getDouble("x", 0d),
                section.getDouble("y", 0d),
                section.getDouble("z", 0d),
                section.getFloat("yaw", 0f),
                section.getFloat("pitch", 0f));
    }

    public static ResourceSection toResourceSection(ResourceSection section, Location location) {
        if (location == null) {
            return null;
        }

        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());

        return section;
    }

}
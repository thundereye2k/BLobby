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

package me.bradleysteele.lobby.sidebar.viewable;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Bradley Steele
 */
public interface Viewable<T> {

    /**
     * @param player the player to show the viewable to.
     */
    void show(Player player);

    /**
     * @param player the player to hide the viewable from.
     */
    void hide(Player player);

    /**
     * @param player the viewer.
     * @return object being viewed.
     */
    T getViewable(Player player);

    /**
     * @return set of viewers.
     */
    Set<Player> getViewers();

    /**
     * @param player the player to check.
     * @return {@code true} if they are a viewer.
     */
    boolean isViewer(Player player);

    /**
     * @param player   viewer to add.
     * @param viewable the object being viewed.
     */
    void addViewer(Player player, T viewable);

    /**
     * @param player viewer to remove.
     */
    void removeViewer(Player player);
}
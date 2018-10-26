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

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Bradley Steele
 */
public abstract class AbstractViewable<T> implements Viewable<T> {

    private final Map<Player, T> viewers = Maps.newHashMap();

    @Override
    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(viewers.keySet());
    }

    @Override
    public T getViewable(Player player) {
        return viewers.get(player);
    }

    @Override
    public boolean isViewer(Player player) {
        return viewers.containsKey(player);
    }


    @Override
    public void addViewer(Player player, T viewable) {
        viewers.put(player, viewable);
    }

    @Override
    public void removeViewer(Player player) {
        viewers.remove(player);
    }
}
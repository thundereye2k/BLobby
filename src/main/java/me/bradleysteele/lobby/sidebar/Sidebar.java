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
import me.bradleysteele.lobby.sidebar.viewable.AbstractViewable;
import org.bukkit.entity.Player;

/**
 * @author Bradley Steele
 */
public class Sidebar extends AbstractViewable<PlayerSidebar> {

    private String title;

    public Sidebar(String title) {
        this.title = title;
    }

    @Override
    public void show(Player player) {
        PlayerSidebar sidebar;

        if (this.isViewer(player)) {
            sidebar = this.getViewable(player);
        } else {
            sidebar = new PlayerSidebar(player, title);
            this.addViewer(player, sidebar);
        }

        sidebar.update();
    }

    @Override
    public void hide(Player player) {
        if (this.isViewer(player)) {
            PlayerSidebar sidebar = this.getViewable(player);
            sidebar.clear();
            sidebar.update();

            this.removeViewer(player);
        }
    }

    /**
     * Removes all lines from the sidebar.
     */
    public void clear() {
        this.getViewers().forEach(viewer -> this.getViewable(viewer).clear());
    }

    /**
     * @param title the sidebar title.
     */
    public void setTitle(String title) {
        this.title = Messages.colour(title);

        this.getViewers().forEach(viewer -> getViewable(viewer).setTitle(title));
        this.getViewers().forEach(this::show);
    }

    /**
     * @param line the line to append.
     */
    public void addLine(SidebarLine line) {
        this.getViewers().forEach(viewer -> this.getViewable(viewer).addLine(line));
    }

    /**
     * @param line the line to append.
     */
    public void addLine(String line) {
        addLine(new SidebarLine(Messages.colour(line)));
    }
}
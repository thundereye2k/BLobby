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

import com.google.common.collect.Lists;
import me.bradleysteele.commons.register.worker.BWorker;
import me.bradleysteele.lobby.resource.yml.Config;
import me.bradleysteele.lobby.sidebar.Sidebar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * @author Bradley Steele
 */
public class WorkerSidebar extends BWorker {

    private static final WorkerSidebar instance = new WorkerSidebar();

    public static WorkerSidebar get() {
        return instance;
    }

    private Sidebar sidebar;
    private List<String> content = Lists.newArrayList();

    private WorkerSidebar() {
        this.setSync(false);
        this.setPeriod(10L);
    }

    @Override
    public void run() {
        if (Config.SIDEBAR_ENABLED.getAsBoolean()) {
            sidebar.getViewers().forEach(viewer -> {
                sidebar.clear();

                content.forEach(line -> {
                    sidebar.addLine(line.replace("{player}", viewer.getName()));
                });

                sidebar.show(viewer);
            });
        }
    }

    @Override
    public void onRegister() {
        sidebar = new Sidebar(Config.SIDEBAR_TITLE.getAsString());
        content = Config.SIDEBAR_CONTENT.getAsStringList();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        sidebar.show(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        sidebar.hide(event.getPlayer());
    }
}
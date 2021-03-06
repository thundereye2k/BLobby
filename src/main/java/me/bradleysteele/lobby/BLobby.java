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

package me.bradleysteele.lobby;

import me.bradleysteele.commons.BPlugin;
import me.bradleysteele.lobby.resource.Resources;
import me.bradleysteele.lobby.worker.WorkerLobby;
import me.bradleysteele.lobby.worker.WorkerLocations;
import me.bradleysteele.lobby.worker.WorkerServerController;
import me.bradleysteele.lobby.worker.WorkerSidebar;

/**
 * @author Bradley Steele
 */
public class BLobby extends BPlugin {

    private static BLobby instance;

    public static BLobby getInstance() {
        return instance;
    }

    public BLobby() {
        instance = this;
    }

    @Override
    public void enable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.register(
                Resources.class,

                WorkerLocations.class,
                WorkerServerController.class,
                WorkerLobby.class,
                WorkerSidebar.class
        );
    }
}
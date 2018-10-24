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

package me.bradleysteele.lobby.worker.hook;

import org.bukkit.plugin.Plugin;

/**
 * @author Bradley Steele
 */
public interface PluginHook<T extends Plugin> {

    /**
     * Attempts to hook into a {@link Plugin}.
     */
    void hook();

    /**
     * @return hooked plugin class.
     */
    T getPlugin();

    /**
     * @return {@code true} if the plugin hook was
     *         successful.
     */
    boolean isHooked();

}
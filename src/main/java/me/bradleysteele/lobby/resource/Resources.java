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

package me.bradleysteele.lobby.resource;

import me.bradleysteele.commons.BPlugin;
import me.bradleysteele.commons.register.Registrable;
import me.bradleysteele.commons.resource.Resource;
import me.bradleysteele.commons.resource.ResourceProvider;
import me.bradleysteele.commons.resource.ResourceReference;
import me.bradleysteele.lobby.BLobby;

import java.util.stream.Stream;

/**
 * The {@link Resources} singleton class is used as a holder
 * for all of the resources related to the {@link BLobby} plugin.
 *
 * @author Bradley Steele
 */
public class Resources implements Registrable {

    private static final Resources instance = new Resources();

    public static Resources get() {
        return instance;
    }

    private BPlugin plugin;

    @Override
    public void register() {
        Stream.of(ResourceType.values()).forEach(type -> plugin.getResourceProvider().loadResource(type.getReference()));
    }

    /**
     * Gets the resource from the provided resource type.
     * <p>
     * The {@link ResourceProvider} will load and cache the resource if
     * it has not already been done.
     *
     * @param reference the resource's reference.
     * @return the resource.
     */
    public Resource getResource(ResourceReference reference) {
        return plugin.getResourceProvider().getResource(reference);
    }

    public Resource getResource(ResourceType type) {
        return getResource(type.getReference());
    }
}
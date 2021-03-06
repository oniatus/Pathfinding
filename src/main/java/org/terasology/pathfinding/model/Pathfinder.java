/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.pathfinding.model;

import com.google.common.collect.Lists;
import org.terasology.navgraph.NavGraphSystem;
import org.terasology.navgraph.WalkableBlock;

import java.util.Arrays;
import java.util.List;

/**
 * @author synopia
 */
public class Pathfinder {
    private HAStar haStar;
    private PathCache cache;
    private NavGraphSystem world;

    public Pathfinder(NavGraphSystem world, LineOfSight lineOfSight) {
        this.world = world;
        haStar = new HAStar(lineOfSight);
        cache = new PathCache();
    }

    public void clearCache() {
        cache.clear();
    }

    public Path findPath(final WalkableBlock target, final WalkableBlock start) {
        return findPath(target, Arrays.asList(start)).get(0);
    }

    public List<Path> findPath(final WalkableBlock target, final List<WalkableBlock> starts) {
        List<Path> result = Lists.newArrayList();
        haStar.reset();
        for (WalkableBlock start : starts) {
            Path path = cache.findPath(start, target, new PathCache.Callback() {
                @Override
                public Path run(WalkableBlock from, WalkableBlock to) {
                    if (from == null || to == null) {
                        return Path.INVALID;
                    }
                    WalkableBlock refFrom = world.getBlock(from.getBlockPosition());
                    WalkableBlock refTo = world.getBlock(to.getBlockPosition());

                    Path path;
                    if (haStar.run(refFrom, refTo)) {
                        path = haStar.getPath();
                    } else {
                        path = Path.INVALID;
                    }
                    return path;
                }
            });
            result.add(path);
        }

        return result;
    }


    @Override
    public String toString() {
        return haStar.toString();
    }
}

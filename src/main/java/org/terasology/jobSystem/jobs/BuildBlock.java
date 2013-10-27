/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.jobSystem.jobs;

import com.google.common.collect.Lists;
import org.terasology.engine.CoreRegistry;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.jobSystem.Job;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Vector3i;
import org.terasology.pathfinding.componentSystem.PathfinderSystem;
import org.terasology.pathfinding.model.WalkableBlock;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.BlockManager;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 * @author synopia
 */
public class BuildBlock implements Job {

    private final PathfinderSystem pathfinderSystem;
    private final static float[][] neighbors = new float[][]{{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
    private final WorldProvider worldProvider;
    private final Block blockType;

    public BuildBlock() {
        pathfinderSystem = CoreRegistry.get(PathfinderSystem.class);
        worldProvider = CoreRegistry.get(WorldProvider.class);
        blockType = CoreRegistry.get(BlockManager.class).getBlock("engine:Dirt");
    }

    @Override
    public List<Vector3i> getTargetPositions(EntityRef block) {
        List<Vector3i> result = Lists.newArrayList();

        Vector3f worldPos = block.getComponent(LocationComponent.class).getWorldPosition();
        WalkableBlock walkableBlock;
        Vector3f pos = new Vector3f();
        for (float[] neighbor : neighbors) {
            pos.set(worldPos.x + neighbor[0], worldPos.y, worldPos.z + neighbor[1]);
            walkableBlock = pathfinderSystem.getBlock(pos);
            if (walkableBlock != null) {
                result.add(walkableBlock.getBlockPosition());
            }

        }
        return result;
    }

    @Override
    public boolean canMinionWork(EntityRef block, EntityRef minion) {
        Vector3f pos = new Vector3f();
        pos.sub(block.getComponent(LocationComponent.class).getWorldPosition(), minion.getComponent(LocationComponent.class).getWorldPosition());
        pos.y /= 4;
        float length = pos.length();
        return length < 2;
    }

    @Override
    public void letMinionWork(EntityRef block, EntityRef minion) {
        worldProvider.setBlock(block.getComponent(BlockComponent.class).getPosition(), blockType);
    }

    @Override
    public boolean isValidBlock(EntityRef block) {
        Block type = worldProvider.getBlock(block.getComponent(BlockComponent.class).getPosition());
        return type.isPenetrable();
    }
}
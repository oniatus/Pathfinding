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
package org.terasology.minion.move;

import org.terasology.logic.behavior.tree.Node;
import org.terasology.logic.behavior.tree.Status;
import org.terasology.logic.behavior.tree.Task;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.minion.path.MinionPathComponent;
import org.terasology.navgraph.WalkableBlock;
import org.terasology.pathfinding.componentSystem.PathfinderSystem;
import org.terasology.registry.In;

import javax.vecmath.Vector3f;

/**
 * Set <b>MinionPathComponent</b>'s target to the block below local player.<br/>
 * <br/>
 * Always returns <b>SUCCESS</b>.<br/>
 * <br/>
 * Auto generated javadoc - modify README.markdown instead!
 */
public class SetTargetLocalPlayerNode extends Node {
    @Override
    public SetTargetLocalPlayerTask createTask() {
        return new SetTargetLocalPlayerTask(this);
    }

    public static class SetTargetLocalPlayerTask extends Task {
        @In
        private LocalPlayer localPlayer;
        @In
        private PathfinderSystem pathfinderSystem;

        public SetTargetLocalPlayerTask(Node node) {
            super(node);
        }

        @Override
        public Status update(float dt) {
            Vector3f position = localPlayer.getPosition();
            WalkableBlock block = pathfinderSystem.getBlock(position);
            if (block != null) {
                MinionPathComponent pathComponent = actor().component(MinionPathComponent.class);
                pathComponent.targetBlock = block.getBlockPosition();
                pathComponent.pathState = MinionPathComponent.PathState.NEW_TARGET;
                actor().save(pathComponent);
            }
            return Status.SUCCESS;
        }

        @Override
        public void handle(Status result) {

        }

        @Override
        public SetTargetLocalPlayerNode getNode() {
            return (SetTargetLocalPlayerNode) super.getNode();
        }
    }
}

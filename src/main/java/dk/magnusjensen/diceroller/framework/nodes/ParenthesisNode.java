/*
 * Copyright 2022 Magnus Jensen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.magnusjensen.diceroller.framework.nodes;

import java.util.ArrayList;
import java.util.List;

public class ParenthesisNode implements Node {
    private final Node node;

    public ParenthesisNode(Node node) {
        this.node = node;
    }

    @Override
    public float evaluate() {
        return node.evaluate();
    }

    @Override
    public String createPrintableString() {
        return "(" + node.createPrintableString() + ")";
    }

    @Override
    public String createResultingString() {
        return node.createResultingString();
    }

    @Override
    public void getSteps(List<Step> steps) {
        node.getSteps(steps);
    }
}

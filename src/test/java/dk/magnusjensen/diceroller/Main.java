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

package dk.magnusjensen.diceroller;

import dk.magnusjensen.diceroller.framework.nodes.Node;
import dk.magnusjensen.diceroller.framework.nodes.Step;

import java.util.ArrayList;
import java.util.List;

import static dk.magnusjensen.diceroller.DiceRoller.parseDiceRoll;

public class Main {

    public static void main(String[] args) {
        Node node = parseDiceRoll("(5+3+2)+5d100kl3");
        //Node node = parseDiceRoll("5d20k3");
        System.out.println(node.evaluate());
        System.out.println(node.createPrintableString());
        System.out.println(node.createResultingString());

        List<Step> nodes = new ArrayList<>();
        node.getSteps(nodes);

        System.out.println(StepStringBuilder.build(nodes));
    }
}

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

import dk.magnusjensen.diceroller.framework.dies.Dice;
import dk.magnusjensen.diceroller.framework.modifiers.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiceNode implements Node {

    private int numOfRolls;
    private Dice dice;
    private List<Integer> rolls = new ArrayList<>();
    private List<Modifier> modifiers = new ArrayList<>();

    public DiceNode(int numOfRolls, Dice dice) {
        this.numOfRolls = numOfRolls;
        this.dice = dice;
    }

    public DiceNode(int numOfRolls, Dice dice, List<Modifier> modifiers) {
        this.numOfRolls = numOfRolls;
        this.dice = dice;
        this.modifiers = modifiers;
    }

    @Override
    public float evaluate() {
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < this.numOfRolls; i++) {
            rolls.add(dice.roll());
        }

        // Default rolls to ascending order.
        rolls = rolls.stream().sorted().collect(Collectors.toList());

        this.rolls.addAll(rolls);

        for (Modifier mod : modifiers) {
            rolls = mod.modifyResult(rolls);
        }

        return rolls.stream().reduce(0, Integer::sum);
    }

    @Override
    public String createPrintableString() {
        StringBuilder builder = new StringBuilder();

        for (Modifier mod : modifiers) {
            builder.append(mod.createPrintableString());
        }
        return "" + numOfRolls + "d" + dice.getNumOfSides() + builder;
    }

    @Override
    public String createResultingString() {
       return "Not implemented";
    }

    @Override
    public void getSteps(List<Step> steps) {
        List<Step> modifierSteps = new ArrayList<>();
        modifiers.forEach((mod) -> modifierSteps.add(mod.getStep()));
        steps.add(new DiceStep(rolls, modifierSteps));
    }
}

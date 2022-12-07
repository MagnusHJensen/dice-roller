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

import dk.magnusjensen.diceroller.framework.modifiers.KeepModifierStep;
import dk.magnusjensen.diceroller.framework.nodes.ArithmeticStep;
import dk.magnusjensen.diceroller.framework.nodes.DiceNode;
import dk.magnusjensen.diceroller.framework.nodes.DiceStep;
import dk.magnusjensen.diceroller.framework.nodes.Step;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class StepStringBuilder {
    private static String DICE_STRING = "";

    public static String build(List<Step> steps) {
        List<String> strings = new ArrayList<>();
        steps.forEach(step -> {
            if (step instanceof DiceStep diceStep) {
                strings.add(buildStep(diceStep));
            } else if (step instanceof ArithmeticStep arithmeticStep) {
                strings.add(buildStep(arithmeticStep));
            }
        });

        strings.add("13.4");

        DICE_STRING = "";

        return String.join(" = ", strings);
    }

    private static String buildStep(KeepModifierStep keepModifierStep) {
        return String.format("[%s, %s]",
            keepModifierStep.droppedRolls().stream().map((val) -> "_" + val).collect(Collectors.joining(", ")),
            keepModifierStep.keptRolls().stream().map(String::valueOf).collect(Collectors.joining(", "))
            );
    }

    private static String buildStep(DiceStep diceStep) {
        List<String> modifierStrings = new ArrayList<>();

        String rolls = String.format("[%s]", diceStep.rolls().stream().map(String::valueOf).collect(Collectors.joining(", ")));
        modifierStrings.add(rolls);
        diceStep.modifierSteps().forEach((modStep) -> {
            if (modStep instanceof KeepModifierStep keepModifierStep) {

                modifierStrings.add(buildStep(keepModifierStep));
            }
        });

        DICE_STRING = "DICE COMP HERE";

        return String.join(" -> ", modifierStrings);
    }

    private static String buildStep(ArithmeticStep arithmeticStep) {
        String leftSideString = arithmeticStep.getLeftSide() instanceof DiceNode ? DICE_STRING : String.format("%.1f", arithmeticStep.getLeftSideResult());
        String rightSideString = arithmeticStep.getRightSide() instanceof DiceNode ? DICE_STRING : String.format("%.1f", arithmeticStep.getRightSideResult());


        return String.format("%s %s %s", leftSideString, arithmeticStep.getOperator().getSign(), rightSideString);
    }
}

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

package dk.magnusjensen.diceroller.framework.modifiers;

import dk.magnusjensen.diceroller.framework.nodes.Step;

import java.util.ArrayList;
import java.util.List;

public class KeepModifier implements Modifier {

    private final int keepAmount;
    private final boolean keepHighest;
    private final List<Integer> keptRolls = new ArrayList<>();
    private final List<Integer> droppedRolls = new ArrayList<>();

    public KeepModifier(int keepAmount, boolean keepHighest) {
        if (keepAmount == 0) {
            throw new RuntimeException("Keep amount can not be 0.");
        }
        this.keepAmount = keepAmount;
        this.keepHighest = keepHighest;
    }

    @Override
    public List<Integer> modifyResult(List<Integer> rolls) {
        if (keepAmount >= rolls.size()) {
            keptRolls.addAll(rolls);
            return keptRolls;
        }

        List<Integer> sortedRolls = rolls.stream().sorted().toList();

        if (keepHighest) {
            keptRolls.addAll(sortedRolls.subList(sortedRolls.size() - keepAmount, sortedRolls.size()));
            droppedRolls.addAll(sortedRolls.subList(0, sortedRolls.size() - keepAmount));
        } else {
            keptRolls.addAll(sortedRolls.subList(0, keepAmount));
            droppedRolls.addAll(sortedRolls.subList(keepAmount, sortedRolls.size()));
        }

        return keptRolls;
    }

    public int getKeepAmount() {
        return keepAmount;
    }

    public boolean isKeepHighest() {
        return keepHighest;
    }

    public List<Integer> getKeptRolls() {
        return keptRolls;
    }

    public List<Integer> getDroppedRolls() {
        return droppedRolls;
    }

    public String createPrintableString() {
        return "k" + (keepHighest ? "" : "l") + this.keepAmount;
    }

    @Override
    public Step getStep() {
        return new KeepModifierStep(droppedRolls, keptRolls);
    }
}

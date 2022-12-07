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

import dk.magnusjensen.diceroller.framework.Operator;

public class ArithmeticStep implements Step {
    private final Operator operator;
    private final float leftSideResult;
    private final Node leftSide;
    private final float rightSideResult;
    private final Node rightSide;

    public ArithmeticStep(Operator operator, float leftSideResult, Node leftSide, float rightSideResult, Node rightSide) {
        this.operator = operator;
        this.leftSideResult = leftSideResult;
        this.leftSide = leftSide;
        this.rightSideResult = rightSideResult;
        this.rightSide = rightSide;
    }

    public Operator getOperator() {
        return operator;
    }

    public float getLeftSideResult() {
        return leftSideResult;
    }

    public Node getLeftSide() {
        return leftSide;
    }

    public float getRightSideResult() {
        return rightSideResult;
    }

    public Node getRightSide() {
        return rightSide;
    }
}

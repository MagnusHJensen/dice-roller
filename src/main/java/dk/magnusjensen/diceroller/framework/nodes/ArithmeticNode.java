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

import java.util.ArrayList;
import java.util.List;

public class ArithmeticNode implements Node {
    private final Node leftSide;
    private final Node rightSide;
    private final Operator operator;
    private float leftSideResult;
    private float rightSideResult;
    private float result;

    public ArithmeticNode(Node leftSide, Node rightSide, Operator operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }

    @Override
    public float evaluate() {
        leftSideResult = this.leftSide.evaluate();
        rightSideResult = rightSide.evaluate();
        result = this.operator.getResult().apply(leftSideResult, rightSideResult);
        return result;
    }

    @Override
    public String createPrintableString() {
        return leftSide.createPrintableString() + operator.getSign() + rightSide.createPrintableString();
    }

    @Override
    public String createResultingString() {
        return leftSide.createResultingString() + " " + operator.getSign() + " " + rightSide.createResultingString() + " = " + result;
    }

    @Override
    public void getSteps(List<Step> steps) {
        leftSide.getSteps(steps);
        rightSide.getSteps(steps);
        steps.add(new ArithmeticStep(operator, leftSideResult, leftSide, rightSideResult, rightSide));
    }


}

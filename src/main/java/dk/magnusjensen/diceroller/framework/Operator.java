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

package dk.magnusjensen.diceroller.framework;

import java.util.function.BiFunction;

public enum Operator {
    ADDITION("+", Float::sum),
    SUBTRACTION("-", (left, right) -> left - right),
    MULTIPLICATION("*", (left, right) -> left * right),
    DIVISON("/", (left, right) -> left / right),
    EXPONENTATION("**", (left, right) -> (float) Math.pow(left, right)),
    MODULO("%", (left, right) -> left % right);


    private final String sign;
    private final BiFunction<Float, Float, Float> result;
    Operator(String sign, BiFunction<Float, Float, Float> result) {
        this.sign = sign;
        this.result = result;
    }

    public static Operator fromString(String string) {
        for (Operator operator : values()) {
            if (operator.getSign().equals(string)) {
                return operator;
            }
        }

        return null;
    }

    public String getSign() {
        return sign;
    }

    public BiFunction<Float, Float, Float> getResult() {
        return result;
    }
}

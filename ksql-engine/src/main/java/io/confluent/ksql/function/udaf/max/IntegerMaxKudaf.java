/*
 * Copyright 2017 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confluent.ksql.function.udaf.max;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.streams.kstream.Merger;

import java.util.Collections;

import io.confluent.ksql.function.AggregateFunctionArguments;
import io.confluent.ksql.function.BaseAggregateFunction;
import io.confluent.ksql.function.KsqlAggregateFunction;

public class IntegerMaxKudaf extends BaseAggregateFunction<Integer, Integer> {

  IntegerMaxKudaf(String functionName, int argIndexInValue) {
    super(functionName, argIndexInValue, () -> Integer.MIN_VALUE, Schema.OPTIONAL_INT32_SCHEMA,
          Collections.singletonList(Schema.OPTIONAL_INT32_SCHEMA)
    );
  }

  @Override
  public Integer aggregate(Integer currentValue, Integer aggregateValue) {
    if (currentValue == null) {
      return aggregateValue;
    }
    return Math.max(currentValue, aggregateValue);
  }

  @Override
  public Merger<String, Integer> getMerger() {
    return (aggKey, aggOne, aggTwo) -> Math.max(aggOne, aggTwo);
  }

  @Override
  public KsqlAggregateFunction<Integer, Integer> getInstance(
      final AggregateFunctionArguments aggregateFunctionArguments) {
    return new IntegerMaxKudaf(functionName, aggregateFunctionArguments.udafIndex());
  }
}

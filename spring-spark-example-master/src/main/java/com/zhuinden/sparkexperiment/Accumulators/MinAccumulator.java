package com.zhuinden.sparkexperiment.Accumulators;

import org.apache.spark.util.AccumulatorV2;

public class MinAccumulator extends AccumulatorV2<Integer,Integer> {

    public Integer minValue ;


    public MinAccumulator() {
        minValue = null ;
    }

    @Override
    public boolean isZero() {
        return (minValue == null);
    }

    @Override
    public AccumulatorV2<Integer, Integer> copy() {
        return this;
    }

    @Override
    public void reset() {
        minValue = null;
    }

    @Override
    public void add(Integer integer) {
        if( integer == null){
        }
        if(minValue == null){
            minValue = integer;
        }
        if(integer.intValue() < minValue.intValue()) {
            minValue = integer ;
        }

    }

    @Override
    public void merge(AccumulatorV2<Integer, Integer> accumulatorV2) {
            add(accumulatorV2.value());
    }

    @Override
    public Integer value() {
        return minValue;
    }


}

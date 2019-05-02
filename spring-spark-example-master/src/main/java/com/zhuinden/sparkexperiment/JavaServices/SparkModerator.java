package com.zhuinden.sparkexperiment.JavaServices;

import com.zhuinden.sparkexperiment.SparkServices.RddSpark;
import org.apache.spark.api.java.JavaRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SparkModerator {


    @Autowired
    RddSpark rddSpark;


    public String pipeline(){
        JavaRDD<String> list1 =  rddSpark.readCsvFile("input1");
        JavaRDD<String> list2 = rddSpark.readCsvFile("input2");
        return "done";
    }

}

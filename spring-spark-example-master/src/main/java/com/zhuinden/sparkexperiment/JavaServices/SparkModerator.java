package com.zhuinden.sparkexperiment.JavaServices;

import com.zhuinden.sparkexperiment.SparkServices.RddSpark;
import org.apache.hadoop.mapreduce.v2.app.job.Job;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SparkModerator {


    @Autowired
    private SparkSession sparkSession;

    @Autowired
    RddSpark rddSpark;


    public String pipeline(){
        JavaSparkContext sc = new  JavaSparkContext(sparkSession.sparkContext());

        JavaRDD<String> list1 =  rddSpark.readCsvFile(sc,"input1");

        JavaRDD<String> list2 =  rddSpark.readCsvFile(sc,"input1");

        return "done";
    }

}

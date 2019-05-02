package com.zhuinden.sparkexperiment.SparkServices;


import com.zhuinden.sparkexperiment.Accumulators.MinAccumulator;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.util.AccumulatorV2;
import org.apache.spark.util.LongAccumulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import scala.Tuple2;

import java.io.FileNotFoundException;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by achat1 on 9/23/15.
 * Just an example to see if it works.
 */
@Component
public class RddSpark {
    @Autowired
    private SparkSession sparkSession;







    public void validationstartRDD(int id) {

        JavaSparkContext sc = new  JavaSparkContext(sparkSession.sparkContext());
        String path  = "C://temptest//" + id +".csv";
        JavaRDD<String> file = sc.textFile(path).cache();

        System.out.println(file.take(5));

        List<String> headers = Arrays.asList(file.take(1).get(0).split(","));

        System.out.println(file.take(5));


        JavaPairRDD<String,Integer> mypairmap = file.mapToPair(s -> {
            String[] line = s.split(",");
            String line_id = line[0];
            int val =0 ;
            for ( String col : line){
                if (col.equals("")) val ++ ;
            }

            return new Tuple2(line_id,val);
        });

        mypairmap.filter(tuple2 -> tuple2._2() > 0).foreach(x-> System.out.println(x._1 + " " +  x._2));
    }


    public JavaRDD<String> readCsvFile(JavaSparkContext sc ,String inputFile){



         MinAccumulator acc = new MinAccumulator();
         sc.sc().register(acc);

             JavaRDD<String> rdd = sc.textFile("src/main/resources/"+inputFile+".csv");
            JavaRDD<String> rdd1 = rdd.map( (String line) ->  {


                try {
                    Integer i = Integer.parseInt(line.split(",")[0]);
                    acc.add(i);
                }
                catch (Exception e){

                }


                return line ;
            } );



        rdd1.collect();
        System.out.println("min accumulator value = " + acc.value());
        return rdd1 ;

    }
}

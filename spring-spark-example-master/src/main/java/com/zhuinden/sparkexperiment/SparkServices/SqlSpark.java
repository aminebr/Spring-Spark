package com.zhuinden.sparkexperiment.SparkServices;


import com.zhuinden.sparkexperiment.Entities.Validationerror;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by achat1 on 9/23/15.
 * Just an example to see if it works.
 */
@Component
public class SqlSpark {
    @Autowired
    private SparkSession sparkSession;





    /* method to get a report */
    public  List<Validationerror> validationstart(int id ) {
        Dataset<Row> csv = sparkSession.read().format("csv").option("header","true").load("C://temptest//" + id +".csv");
        String csv_header_schema[] = csv.schema().fieldNames();
        String querry_filler = "" ;
        for (String col:csv_header_schema) {
            System.out.println(col);
            if (col.equals("ID")){

            }
            else querry_filler += "(CASE WHEN ISNULL(" + col + ") THEN 1 ELSE 0 END )+";
        }

        querry_filler += " 0";

        csv.createOrReplaceTempView("contract");

        Dataset<Row> aggregatedcsv = sparkSession.sql("SELECT ID , SUM ( " + querry_filler +  ") as SUM from contract group by ID HAVING SUM > 0");
        aggregatedcsv.show();



        List<Row> rows = aggregatedcsv.collectAsList();//JavaConversions.asScalaBuffer(words)).count();
        return rows.stream().map(new Function<Row, Validationerror>() {
            @Override
            public Validationerror apply(Row row) {
                return new Validationerror(row.getString(0), row.getLong(1));
            }
        }).collect(Collectors.toList());



    }



    /* method to merge 2 files */
    public void trytomerge(String file_to_merge_name, int id) {


        Dataset<Row> merge_csv = sparkSession.read().format("csv").option("header","true").load("C://temptest//" + file_to_merge_name);
        String merge_csv_header_schema[] = merge_csv.schema().fieldNames();


        Dataset<Row> original_csv = sparkSession.read().format("csv").option("header","true").load("C://temptest//" + id +".csv");
        String original_csv_header_schema[] = original_csv.schema().fieldNames();

        String original_colnames ="";
        String merge_colnames ="";

        Map<Integer,Integer> columns_mapping  = new HashMap<>();


        for (int i = 0 ; i < merge_csv_header_schema.length ; i++){
            boolean found = false ;

            for (int j = 0 ; j < original_csv_header_schema.length ; j++){
                if(original_csv_header_schema[i].equals(merge_csv_header_schema[j])){
                    found = true ;
                    columns_mapping.put(i,j);
                    break;
                }
            }
            if (found == false ) return;
        }

        for (int i = 0 ; i < original_csv_header_schema.length-1 ; i++){
            merge_colnames +="merge."+ merge_csv_header_schema[i] + ", ";
            original_colnames +="original."+ merge_csv_header_schema[columns_mapping.get(i)] + ", ";
        }

        original_colnames +="original."+ original_csv_header_schema[columns_mapping.get(merge_csv_header_schema.length-1)];
        merge_colnames +="merge."+ merge_csv_header_schema[merge_csv_header_schema.length-1];



        original_csv.createOrReplaceTempView("original");
        merge_csv.createOrReplaceTempView("merge");

        Dataset<Row> result= sparkSession.sql("SELECT "+merge_colnames+" FROM merge UNION (select "+original_colnames+" from original where ID NOT IN (select ID from merge)) ");
        System.out.println(result.count());
        result.coalesce(1).write().option("header", "true").csv("C://temptest//" + 3 +".csv");

    }





    /* method to get a pagination out of a dataset */
    public List<Row> getpagination(int id,int paginationsize , int page) {

        Dataset<Row> csv = sparkSession.read().format("csv").option("header","true").load("C://temptest//" + id +".csv");
        String csv_header_schema[] = csv.schema().fieldNames();
        // page 1 => 0 - > 50
        // page 2 => 50 -> 100
        Dataset<Row> df1 = csv.limit( (page-1) * paginationsize) ;
        Dataset<Row> df2 = csv.except(df1);
        df2.limit(page*paginationsize);


        List<Row> rows = df2.collectAsList();//JavaConversions.asScalaBuffer(words)).count();

        return rows;

    }


}

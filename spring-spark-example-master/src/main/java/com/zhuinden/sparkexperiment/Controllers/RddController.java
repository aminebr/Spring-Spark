package com.zhuinden.sparkexperiment.Controllers;

import com.zhuinden.sparkexperiment.SparkServices.RddSpark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Owner on 2017. 03. 29..
 */
@RequestMapping("api")
@Controller
public class RddController {
    @Autowired
    RddSpark rddSpark;





    final String UPLOADED_FOLDER = "C://temptest//";




    @GetMapping("/getreportRDD")
    public void validateRDD(@RequestParam("id") int id){
        rddSpark.validationstartRDD(id);
    }



}

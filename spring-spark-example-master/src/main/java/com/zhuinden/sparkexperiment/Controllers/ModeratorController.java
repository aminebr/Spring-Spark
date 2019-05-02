package com.zhuinden.sparkexperiment.Controllers;


import com.zhuinden.sparkexperiment.JavaServices.SparkModerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ModeratorController {


    @Autowired
    SparkModerator sparkModerator;


    @RequestMapping(value = "/pipeline", method =  RequestMethod.GET)
    public ResponseEntity<String> initpipeline(){
        long startTime = System.currentTimeMillis();
        String response = sparkModerator.pipeline();
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

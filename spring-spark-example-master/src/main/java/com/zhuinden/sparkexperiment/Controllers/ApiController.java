package com.zhuinden.sparkexperiment.Controllers;

import com.zhuinden.sparkexperiment.Entities.Validationerror;
import com.zhuinden.sparkexperiment.SparkServices.SqlSpark;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Owner on 2017. 03. 29..
 */
@RequestMapping("api")
@Controller
public class ApiController {

    @Autowired
    SqlSpark sqlSpark;

    private static String UPLOADED_FOLDER = "C://temptest//";


    @PostMapping(value = "/upload", produces = "application/json") // //new annotation since 4.3
    public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
        }

        int hash = Integer.MAX_VALUE;

        try {


            //if (!file.getContentType().equals("text/csv")) return "redirect:uploadStatus";
            byte[] bytes = file.getBytes();

            // unique ID is the bytes hash
            hash = bytes.hashCode();
            Path path = Paths.get(UPLOADED_FOLDER + hash + ".csv");
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>(String.valueOf(hash), HttpStatus.OK);
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }


    @PostMapping("/uploadmerge")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ID") int id,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();

            // unique ID is the bytes hash
            int hash = bytes.hashCode();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

            sqlSpark.trytomerge(file.getOriginalFilename(), id);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }


    @GetMapping("/getreport")
    public ResponseEntity<List<Validationerror>> validate(@RequestParam("id") int id) {
        return new ResponseEntity<>(sqlSpark.validationstart(id), HttpStatus.OK);
    }


    @GetMapping("/showpaginated")
    public ResponseEntity<List<Row>> paginated(@RequestParam("id") int id, @RequestParam("paginationsize") int paginationsize, @RequestParam("page") int page) {
        return new ResponseEntity<List<Row>>(sqlSpark.getpagination(id, paginationsize, page), HttpStatus.OK);
    }


}

package com.phyriak.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class BatchController {
    private final JobLauncher jobLauncher;
    private final Job paymentImportJob;


    @PostMapping("/batch/import")
    public String importPayments(
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        Path tempFile = Files.createTempFile(
                "payments-",
                ".csv"
        );

        file.transferTo(tempFile);

        JobParameters params = new JobParametersBuilder()
                .addString(
                        "filePath",
                        tempFile.toString()
                )
                .addLong(
                        "time",
                        System.currentTimeMillis()
                )
                .toJobParameters();

        jobLauncher.run(paymentImportJob, params);

        return "IMPORT STARTED";
    }
}

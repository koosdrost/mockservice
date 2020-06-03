package com.kodr.test.mockservice.service;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockService {

    private static final Logger LOG = LoggerFactory.getLogger(MockService.class);

    @Value("${testfile.location}")
    private String fileLocation;

    public List<String> collectJsons() {
        List<String> jsons = new ArrayList<>();
        collectFiles().forEach(path -> {
            jsons.add(readFile(path.getFileName().toString()));
        });
        return jsons;
    }

    public List<Path> collectFiles() {
        try {
            return Files.walk(Paths.get(fileLocation))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public String readFile(String filename) {
        FileInputStream fis;
        String data = null;
        try {
            fis = new FileInputStream(fileLocation + filename);
            data = IOUtils.toString(fis, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error reading {}", filename);
        }
        return data;
    }

    public boolean isFileAvailable(String filename) {
        return collectFiles().stream().anyMatch(file -> file.getFileName().toString().equals(filename));
    }



}

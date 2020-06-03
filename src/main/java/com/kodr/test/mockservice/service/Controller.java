package com.kodr.test.mockservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
public class Controller {

    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private MockService service;

    @GetMapping("/paths")
    @ResponseBody
    public List<Path> returnFiles() {
        return service.collectFiles();
    }

    @GetMapping("/jsons")
    @ResponseBody
    public List<String> returnJsons() {
        return service.collectJsons();
    }

    @RequestMapping(value="/{filename}")
    @ResponseBody
    public String dynamicEndpoint(@PathVariable String filename) {
        if (service.isFileAvailable(filename)) {
            return service.readFile(filename);
        }
        LOG.error("File {} is not available", filename);
        return "File " + filename + " is not available";
    }
}

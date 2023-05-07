package com.example.demo.controller;

import com.example.demo.pojo.ResultModel;
import com.example.demo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doc")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/{queryString}/{type}/{page}")
    public ResultModel query(@PathVariable String queryString,@PathVariable String type,@PathVariable Integer page) throws Exception{
        return searchService.query(queryString, type, page);
    }

}

package com.example.demo.service;

import com.example.demo.pojo.ResultModel;

public interface SearchService {
    ResultModel query(String queryString,String price,int page) throws Exception;
}

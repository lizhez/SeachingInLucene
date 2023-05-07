package com.example.demo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ResultModel {

    boolean flag=true;

    private List<doc> docList;
    // 总数
    private Long recordCount;
    // 总页数
    private Long pageCount;
    // 当前页
    private long curPage;
}

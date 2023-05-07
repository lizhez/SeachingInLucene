package com.example.demo.service;

import com.example.demo.pojo.ResultModel;
import com.example.demo.pojo.doc;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{
    @Override
    public ResultModel query(String queryString, String queryType, int page) throws Exception{

        int start = (page - 1) * 5;
        int end = page * 5;

        Directory directory = FSDirectory.open(Paths.get("F:\\Lucenes\\dir"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        IKAnalyzer analyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser(queryType, analyzer);
        Query query = queryParser.parse(queryString);

        TopDocs topDocs = indexSearcher.search(query,end);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<doc> docList=new ArrayList<>();
        ResultModel resultModel = new ResultModel();

        if (topDocs.totalHits != 0) {
            for (int i = start; i < end;) {
                doc tempDoc = new doc();
                Document document = indexSearcher.doc(scoreDocs[i].doc);
                String fileName = document.get("name");
                tempDoc.setName(fileName.substring(0, fileName.lastIndexOf(".")));
                tempDoc.setContent(document.get("content"));
                docList.add(tempDoc);
                if(++i==topDocs.totalHits)
                    break;
            }

        }else{
            resultModel.setFlag(false);
        }
        resultModel.setDocList(docList);
        resultModel.setRecordCount(topDocs.totalHits);

        resultModel.setCurPage(page);
        Long pageCount = topDocs.totalHits % 5 > 0 ? (topDocs.totalHits/5) + 1 : topDocs.totalHits/5;
        resultModel.setPageCount(pageCount);

        indexReader.close();
        return resultModel;
    }
}

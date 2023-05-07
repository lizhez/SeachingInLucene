package com.example.demo;

import com.example.demo.pojo.ResultModel;
import com.example.demo.pojo.doc;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class test {
    @Test
    public void createIndex() throws Exception {

        Directory directory = FSDirectory.open(Paths.get("F:\\Lucenes\\dir"));
        IKAnalyzer analyzer = new IKAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, conf);

        File docPath = new File("F:\\Lucenes\\LuXun");

        for (File f : docPath.listFiles()) {

            String fileName = f.getName();

            InputStream in = new FileInputStream(f);
            InputStreamReader reader = null;
            reader = new InputStreamReader(in);

            BufferedReader br = new BufferedReader(reader);
            String fileContent = "";
            String line = null;

            while ((line = br.readLine()) != null) {
                fileContent += line;
            }


            Document document = new Document();

            TextField fileNameField = new TextField("name", fileName, Field.Store.YES);
            TextField fileContentField = new TextField("content", fileContent, Field.Store.YES);


            document.add(fileNameField);
            document.add(fileContentField);


            indexWriter.addDocument(document);
        }
        indexWriter.close();
    }

    @Test
    public void searchIndex() throws Exception {

        Directory directory = FSDirectory.open(Paths.get("F:\\Lucenes\\dir"));

        IndexReader indexReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        IKAnalyzer analyzer = new IKAnalyzer();
        String queryString="相传叫做百草园";
        QueryParser queryParser = new QueryParser("content", analyzer);
        Query query = queryParser.parse(queryString);


        TopDocs topDocs = indexSearcher.search(query, 10);

        System.out.println("查询结果总记录数：" + topDocs.totalHits);

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {

            int id = scoreDoc.doc;
            Document document = indexSearcher.doc(id);
            System.out.println(document.get("name"));
            System.out.println(document.get("content"));
        }
        indexReader.close();
    }

    @Test
    public void query() throws Exception{

        int start = 0;
        //查询到多少条为止
        int end = 5;

        Directory directory = FSDirectory.open(Paths.get("F:\\Lucenes\\dir"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        IKAnalyzer analyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("name", analyzer);
        Query query = queryParser.parse("朝花夕拾");

        TopDocs topDocs = indexSearcher.search(query,end);

        System.out.println("查询结果总记录数：" + topDocs.totalHits);
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
        System.out.println(topDocs.totalHits);
        //封装当前页
        resultModel.setCurPage(1);
        //总页数
        Long pageCount = topDocs.totalHits % 5 > 0 ? (topDocs.totalHits/5) + 1 : topDocs.totalHits/5;
        resultModel.setPageCount(pageCount);

        System.out.println(resultModel);

        indexReader.close();

    }
}

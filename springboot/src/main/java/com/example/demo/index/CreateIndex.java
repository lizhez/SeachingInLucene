package com.example.demo.index;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.file.Paths;

public class CreateIndex {

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

            org.apache.lucene.document.Document document = new Document();

            org.apache.lucene.document.TextField fileNameField = new org.apache.lucene.document.TextField("name", fileName, Field.Store.YES);
            org.apache.lucene.document.TextField fileContentField = new TextField("content", fileContent, Field.Store.YES);

            document.add(fileNameField);
            document.add(fileContentField);

            indexWriter.addDocument(document);
        }
        indexWriter.close();
    }
}

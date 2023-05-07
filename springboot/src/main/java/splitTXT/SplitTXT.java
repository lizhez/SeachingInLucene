package splitTXT;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SplitTXT {

    private String srcPath;
    private String destDirPath;

    private String fileName;
    private long fileSize;
    private List<Long> rowsList;
    private List<String> destFiles;
    private List<String> chapterTitles;
    private int size;


    public SplitTXT(String srcPath) {
        this.srcPath = srcPath;
        String[] srcNames = srcPath.split("\\.");
        this.destDirPath = srcNames[0];
        init(destDirPath);
    }

    private void init(String destDirPath) {

        this.fileName = new File(this.srcPath).getName();
        File srcFile = new File(this.srcPath);
        this.fileSize = srcFile.length();
        getChapsRows();
        size = (int) Math.ceil(rowsList.size() * 1.0 );
        this.destFiles = new ArrayList<>();
        String[] fileNames = this.fileName.split("\\.");

        for (int i = 0; i < size; i++) {
            this.destFiles.add(destDirPath + File.separator +  chapterTitles.get(i)  + "." + fileNames[1]);
        }

    }

    private void getChapsRows() {

        this.rowsList = new ArrayList<>();
        this.chapterTitles = new ArrayList<>();

        long row = 0L;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.srcPath), "UTF-8"))) {

            Pattern p = Pattern.compile("第{0,9}[章][\\s\\n]");
            Matcher m = null;
            String line = null;
            while ((line = br.readLine()) != null) {

                row += 1;
                m = p.matcher(line);
                if (m.find()) {
                    chapterTitles.add(line);
                    this.rowsList.add(row);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void split() {

        File destDir = new File(this.destDirPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        for (int i = 1; i <= size; i++) {
            if (i == 1) {
                splitDetail(i, 1L, rowsList.get(i) - 1);
            } else if (i == size) {
                splitDetail(i, rowsList.get(i - 1));
            } else {
                splitDetail(i, rowsList.get(i - 1), rowsList.get(i) - 1);
            }
        }
    }


    private void splitDetail(int num, Long... longs) {


        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(this.srcPath), "UTF-8"));
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(this.destFiles.get(num - 1)), "UTF-8"))) {

            long row = 0L;
            String line = null;
            String chapterContent = "";

            if (num == size) {
                while ((line = br.readLine()) != null) {
                    row += 1;
                    if (row >= longs[0]) {
                        bw.write(line + "\n");
                        chapterContent += line + "\n";
                    }
                }

                bw.flush();
            } else {
                while ((line = br.readLine()) != null) {
                    row += 1;
                    if (row >= longs[0] && row <= longs[1]) {
                        bw.write(line + "\n");
                        chapterContent += line + "\n";
                    }
                }
                bw.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        String srcPathStr = "F:\\Lucenes\\LuXun.txt";
        SplitTXT sbt = new SplitTXT(srcPathStr);
        sbt.split();
    }

}


package com.hanker;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static int FILE_CNT = 0;
    public static int ERROR_FILE_CNT = 0;

    // 복사할 directory 지정

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{
            // 파일 분류할 경로명
            System.out.println("분류할 경로명 입력 : ");
            String path = sc.nextLine(); // 경로 명

            System.out.println("복사할 위치 디렉토리 입력 : ");
            String outFolder = sc.nextLine(); // 복사 할 디렉토리 명

            int cnt = (new Main()).showFileList(path, outFolder + "\\");

            System.out.println("cnt = " + FILE_CNT);
            System.out.println("error cnt = " + ERROR_FILE_CNT);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // File Read
    public int showFileList(String path, String OUT_FOLDER) throws Exception {
        File dir = new File(path);
        File[] files = dir.listFiles();

        for(int i = 0 ; i < files.length ; i++){
            File file = files[i];

            if(file.isFile() && files[i].getName().endsWith(".png")){
                FILE_CNT++;
                // 3000 바이트 이하 png 파일 찾기
                if(files[i].length() < 3000){
                    String[] fileName = files[i].getName().split("\\.");

                    // 한건이라도 오류가 있으면 해당 디렉토리 복사
                    File inDir = new File(files[i].getParentFile().getPath());
                    File outDir = new File(OUT_FOLDER + files[i].getParentFile().getName());
                    System.out.println(outDir);
                    folderCopy(inDir, outDir);

                    // 오류 파일만 복사
                    FilenameFilter filter = new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.startsWith(fileName[0]);
                        }
                    };

                    File[] listFile = dir.listFiles(filter);
                    for(int j = 0 ; j < listFile.length ; j++){
                        // fileCopy 함수
                        //fileCopy(files[i].getPath(), OUT_FOLDER + listFile[j].getName());
                    }

                    ERROR_FILE_CNT++;
                }
            } else if (files[i].isDirectory()){
                showFileList(files[i].getPath(), OUT_FOLDER);
            }
        }

        return FILE_CNT;
    }

    // 디렉토리 복사
    private void folderCopy(File inPath, File outPath) {
        File[] inPath_file = inPath.listFiles();
        for(File file : inPath_file){
            File tmp = new File(outPath.getAbsolutePath() + File.separator + file.getName());
            if(file.isDirectory()){

                tmp.mkdir();
                folderCopy(file, tmp);
            } else{
                if(!outPath.exists()){
                    outPath.mkdir();
                }
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try{
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(tmp);
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while((cnt=fis.read(b)) != -1){
                        fos.write(b, 0, cnt);
                    }
                } catch(IOException ie){
                    ie.printStackTrace();
                } finally{
                    try{
                        fis.close();
                        fos.close();
                    } catch(IOException ie){
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

    // 파일 복사
    private void fileCopy(String inFile, String outFile) {
        try{
            FileInputStream fis = new FileInputStream(inFile);
            FileOutputStream fos = new FileOutputStream(outFile);

            int data = 0;
            while((data= fis.read())!= -1){
                fos.write(data);
            }
            fis.close();
            fos.close();
        } catch(IOException ie){
            ie.printStackTrace();
        }
    }
}

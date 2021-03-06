/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.lowbrain.gotaku.export;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Queue;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.err.println("5TQファイルのパスを第1引数として指定してください。");
            return;
        }

        File gotaokuFile = new File(args[0]);
        if (!gotaokuFile.isFile()) {
            System.err.println("第１引数に指定された5TQファイルのパスが間違っています。");
            return;
        }

        output5TQ(gotaokuFile);

        return;
    }

    private static void output5TQ(File inFile) throws Exception{
        FileInputStream fis = new FileInputStream(inFile);
        BufferedInputStream bis =new BufferedInputStream(fis);
        
        Queue<Header> headerQ = new LinkedList<Header>();
        Header header = null;

        byte[] block = new byte[256];
        int index = 0;

        // ヘッダ
        while(bis.read(block) != -1 && index < 2048) {
            headerQ.add(Header.genHeader(block));
            index = index + block.length;
        }

        // 問題
        while(bis.read(block) != -1) {
            if (header == null || !header.isBlockRange(index)) header = headerQ.poll();
            System.out.println(Question.genQuestion(block, header.getGenre()).toCsvString());
            index = index + block.length;
        }

        bis.close();
        fis.close();
    }
}

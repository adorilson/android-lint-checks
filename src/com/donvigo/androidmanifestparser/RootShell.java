package com.donvigo.androidmanifestparser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by vgaidarji on 13.03.14.
 */
public class RootShell {
    public static ArrayList<String> listFilesInFolder(String folder){
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "ls " + folder});

            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            ArrayList<String> files = new ArrayList<String>();

            String file;
            while ((file = br.readLine()) != null) {
                files.add(file);
            }

            proc.waitFor();
            return files;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}



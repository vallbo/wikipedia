package sk.fiit.vi.project.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by VB on 12/10/14.
 */
public class File {

    public Scanner getScanner(String path) throws FileNotFoundException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        inputStream = new FileInputStream(path);
        return new Scanner(inputStream, "UTF-8");
    }

}

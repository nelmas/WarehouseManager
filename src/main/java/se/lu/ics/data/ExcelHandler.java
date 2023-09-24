package se.lu.ics.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ExcelHandler {
    private static String path = "javafx-maven-git-demo\\src\\main\\resources\\excel\\VerdeVista.xlsx";
    public static void openExcelFile(){
        try {
            File file = new File(path);
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File tempFile = new File(tempDir,file.getName());
            Files.copy(file.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Runtime.getRuntime().exec("cmd /c start excel.exe " + tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static void writeFile(String path, String content) throws IOException {
        FileOutputStream outputStream = new FileOutputStream((path));
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
}

package org.jsoup.helper.test;

import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataUtilTest {
    @Test
    void testLoadFile() throws IOException {
        File file = new File("path/to/file.html"); // replace with valid file path
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    void testLoadInvalidFile() {
        assertThrows(FileNotFoundException.class, () -> {
            DataUtil.load(new File("path/to/invalid_file.txt"), "UTF-8", "");
        });
    }

    @Test
    void testLoadEmptyInputStream() {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(IOException.class, () -> {
            DataUtil.load(emptyInputStream, "UTF-8", "");
        });
    }

    @Test
    void testLoadInvalidCharset() {
        assertThrows(IOException.class, () -> {
            DataUtil.load(new File("path/to/file.html"), "invalid_charset", "");
        });
    }

    @Test
    void testLoadNullBaseUri() {
        Document document = DataUtil.load(new File("path/to/file.html"), "UTF-8", null);
        assertNotNull(document);
        // Check the default baseUri is used when null is passed
        assertEquals("", document.baseUri());
    }

    @Test
    void testLoadFileException() {
        File invalidFile = new File("path/to/invalid_file.txt");
        assertThrows(FileNotFoundException.class, () -> {
            DataUtil.load(invalidFile, "UTF-8", "");
        });
    }

    @Test
    void testLoadInvalidContentType() {
        File file = new File("path/to/file.html");
        assertThrows(IOException.class, () -> {
            DataUtil.load(file, "invalid_content_type", "");
        });
    }
}
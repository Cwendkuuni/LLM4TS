package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(AnnotatedClassLoader.class)
public class DataUtilTest {

    private static final String FILE_CONTENTS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\nHello World!\n</body>\n</html>";

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        file.createNewFile();
        try (OutputStream output = new FileOutputStream(file)) {
            output.write(FILE_CONTENTS.getBytes());
        }
        Document document = DataUtil.load(file, null);
        assertEquals("Test", document.title());
        file.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        file.createNewFile();
        try (OutputStream output = new FileOutputStream(file)) {
            output.write(FILE_CONTENTS.getBytes("UTF-16"));
        }
        Document document = DataUtil.load(file, "UTF-16");
        assertEquals("Test", document.title());
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testFile.html");
        file.createNewFile();
        try (OutputStream output = new FileOutputStream(file)) {
            output.write(FILE_CONTENTS.getBytes());
        }
        Document document = DataUtil.load(file, "UTF-123");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(FILE_CONTENTS.getBytes());
        Document document = DataUtil.load(inputStream, null);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(FILE_CONTENTS.getBytes("UTF-16"));
        Document document = DataUtil.load(inputStream, "UTF-16");
        assertEquals("Test", document.title());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(FILE_CONTENTS.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-123");
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String xmlContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\nHello World!\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContents.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String xmlContents = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<html>\n<head>\n<meta charset=\"UTF-16\"/>\n<title>Test</title>\n</head>\n<body>\nHello World!\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContents.getBytes("UTF-16"));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.xmlParser());
        assertEquals("Test", document.title());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String xmlContents = "<?xml version=\"1.0\" encoding=\"UTF-123\"?>\n<html>\n<head>\n<meta charset=\"UTF-123\"/>\n<title>Test</title>\n</head>\n<body>\nHello World!\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContents.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-123", null, Parser.xmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "This is a test.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputData, outputStream.toString());
    }
}
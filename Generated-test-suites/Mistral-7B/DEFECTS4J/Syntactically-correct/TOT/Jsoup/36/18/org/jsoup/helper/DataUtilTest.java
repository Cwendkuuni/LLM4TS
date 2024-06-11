package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DataUtilTest {

    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.html");
        testFile.getParentFile().mkdirs();
        testFile.createNewFile();
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test Page</h1>\n" +
                "</body>\n" +
                "</html>";
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        testInputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        Document document = DataUtil.load(testFile, null, null);
        assertEquals("Test Page", document.title());

        // Clean up
        testFile.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("Test Page", document.title());

        // Clean up
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        Document document = DataUtil.load(testInputStream, null, null);
        assertEquals("Test Page", document.title());

        // Clean up
        testInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        Document document = DataUtil.load(testInputStream, "UTF-8", null);
        assertEquals("Test Page", document.title());

        // Clean up
        testInputStream.close();
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        Document document = DataUtil.load(testInputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("Test Page", document.title());

        // Clean up
        testInputStream.close();
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws Exception {
        Document document = DataUtil.load(testFile, "InvalidCharset", null);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws Exception {
        Document document = DataUtil.load(testInputStream, "InvalidCharset", null);
    }
}
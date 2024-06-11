package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests for DataUtil class
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        String expectedContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.writeText(expectedContent);

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals("Loaded file content", expectedContent, doc.body().text());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        String expectedContent = "Das ist ein Testdatei."; // German text for charset test
        File testFile = new File("testFile_de.html");
        testFile.writeText(expectedContent);

        Document doc = DataUtil.load(testFile, "ISO-8859-1", null);
        assertEquals("Loaded file content with specific charset", expectedContent, doc.body().text());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNotFound() throws Exception {
        File nonExistentFile = new File("nonExistentFile.html");
        DataUtil.load(nonExistentFile, null, null);
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws Exception {
        String expectedContent = "This is a test stream.";
        String testData = "Content-Type: text/html\n" + expectedContent;
        ByteArrayInputStream testStream = new ByteArrayInputStream(testData.getBytes());

        Document doc = DataUtil.load(testStream, null, null, Parser.htmlParser());
        assertEquals("Loaded stream content", expectedContent, doc.body().text());
    }

    @Test
    public void testLoadStreamWithSpecifiedCharset() throws Exception {
        String expectedContent = "Das ist ein Teststrom."; // German text for charset test
        String testData = "Content-Type: text/html; charset=ISO-8859-1\n" + expectedContent;
        ByteArrayInputStream testStream = new ByteArrayInputStream(testData.getBytes());

        Document doc = DataUtil.load(testStream, "ISO-8859-1", null, Parser.htmlParser());
        assertEquals("Loaded stream content with specific charset", expectedContent, doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadStreamIOError() throws Exception {
        ByteArrayInputStream testStream = new ByteArrayInputStream(new byte[0]);

        Document doc = null;
        try {
            doc = DataUtil.load(testStream, null, null, Parser.htmlParser());
        } finally {
            testStream.close();
        }
        fail("Should have thrown IOException");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String expectedContent = "This is a test byte data.";
        ByteBuffer byteData = ByteBuffer.wrap(expectedContent.getBytes());

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Parsed byte data content", expectedContent, doc.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String expectedContent = "Das ist ein Testbyte data."; // German text for charset test
        String charset = "ISO-8859-1";
        byte[] byteData = expectedContent.getBytes(Charset.forName(charset));
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);

        Document doc = DataUtil.parseByteData(byteBuffer, charset, null, Parser.htmlParser());
        assertEquals("Parsed byte data content with specific charset", expectedContent, doc.body().text());
    }

    @Test
    public void testCrossStreams() throws Exception {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");

        String expectedContent = "This is a test cross streams.";
        String inputContent = "<html><body>" + expectedContent + "</body></html>";
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(inputContent);
        inputWriter.close();

        FileReader inputReader = new FileReader(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputReader, outputStream);
        inputReader.close();
        outputStream.close();

        FileReader outputReader = new FileReader(outputFile);
        Document doc = DataUtil.load(outputFile, null, null);
        assertEquals("Cross streamed content", expectedContent, doc.body().text());
        outputReader.close();
        outputFile.delete();
    }
}
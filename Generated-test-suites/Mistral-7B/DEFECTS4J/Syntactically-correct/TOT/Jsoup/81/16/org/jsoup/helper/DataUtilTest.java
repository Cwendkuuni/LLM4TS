package org.jsoup.helper.test;

import static org.junit.Assert.*;
import static org.jsoup.helper.DataUtil.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String filePath = "resources/validCharset.html"; // assuming you have a resource folder with a validCharset.html file
        Document document = DataUtil.load(new File(filePath), charset, null);
        assertNotNull(document);
        // Add checks for other elements or attributes as needed
    }

    @Test
    public void testLoadFileWithInvalidCharsetHeader() throws IOException {
        String charset = "invalidCharset"; // invalid charset name
        String filePath = "resources/validCharset.html"; // assuming you have a resource folder with a validCharset.html file
        try {
            Document document = DataUtil.load(new File(filePath), charset, null);
            fail("Expected IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            // Expected
        }
    }

    @Test
    public void testLoadStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String filePath = "resources/validCharset.html"; // assuming you have a resource folder with a validCharset.html file
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        // Add checks for other elements or attributes as needed
        inputStream.close();
    }

    @Test
    public void testLoadStreamWithNoCharsetHeader() throws IOException {
        String charset = null;
        String filePath = "resources/validCharset.html"; // assuming you have a resource folder with a validCharset.html file
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        // Add checks for other elements or attributes as needed
        inputStream.close();
    }

    @Test
    public void testLoadStreamWithInvalidCharsetHeader() throws IOException {
        String charset = "invalidCharset"; // invalid charset name
        String filePath = "resources/validCharset.html"; // assuming you have a resource folder with a validCharset.html file
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        try {
            Document document = DataUtil.load(inputStream, charset, null);
            fail("Expected IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputStreamData = "Your input stream data here...";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        assertNotNull(document);
        // Add checks for other elements or attributes as needed
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithNoCharsetHeader() throws IOException {
        String charset = null;
        String inputStreamData = "Your input stream data here...";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        assertNotNull(document);
        // Add checks for other elements or attributes as needed
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetHeader() throws IOException {
        String charset = "invalidCharset"; // invalid charset name
        String inputStreamData = "Your input stream data here...";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
            fail("Expected IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Your input stream data here...";
        String outputData = "Your output stream data here...";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            String outputDataFromStream = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
            assertEquals(outputData, outputDataFromStream);
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }
}
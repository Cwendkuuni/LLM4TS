package org.jsoup.helper.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private Document doc;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        // Initialize test data
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() throws IOException {
        if (doc != null) {
            doc.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><body><h1>Hello, World!</h1></body></html>";
        Document expectedDoc = Parser.htmlParser().parseInput(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)), "");

        File testFile = new File("test.html");
        FileWriter writer = new FileWriter(testFile);
        writer.write(fileContent);
        writer.close();

        Document actualDoc = DataUtil.load(testFile, "UTF-8", "");

        Assert.assertEquals(expectedDoc, actualDoc);
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document actualDoc = DataUtil.load(inputStream, "UTF-8", "");

        Document expectedDoc = Parser.htmlParser().parseInput(inputStream, "");
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document actualDoc = DataUtil.load(inputStream, "GBK", "");
            Assert.fail("Expected IOException but no exception was thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputContent = "<html><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        Document actualDoc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());

        Document expectedDoc = Parser.htmlParser().parseInput(new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8)), "");
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String expectedOutput = "Hello, World!";
        String actualOutput = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);

        Assert.assertEquals(expectedOutput, actualOutput);
    }
}
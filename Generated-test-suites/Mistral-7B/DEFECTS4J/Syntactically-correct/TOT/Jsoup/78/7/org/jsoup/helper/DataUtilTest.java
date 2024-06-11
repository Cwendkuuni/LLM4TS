package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    private Document doc;
    private static final String TEST_FILE = "test.html";

    @Before
    public void setUp() throws IOException {
        doc = DataUtil.load(new File(TEST_FILE), "UTF-8", null);
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        Document expectedDoc = DataUtil.load(new File(TEST_FILE), "UTF-8", null);
        assertEquals(expectedDoc.html(), doc.html());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileWithInvalidPath() {
        DataUtil.load(new File("non-existent-file.html"), "UTF-8", null);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() {
        try {
            DataUtil.load(new File(TEST_FILE), "GBK", null);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        Document expectedDoc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(expectedDoc.html(), doc.html());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        try {
            DataUtil.load(inputStream, "GBK", null);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        File sourceFile = new File("source.html");
        File destinationFile = new File("destination.html");

        DataUtil.crossStreams(new FileInputStream(sourceFile), new RandomAccessFile(destinationFile, "rw"));

        Document sourceDoc = DataUtil.load(sourceFile, "UTF-8", null);
        Document destinationDoc = DataUtil.load(destinationFile, "UTF-8", null);

        assertEquals(sourceDoc.html(), destinationDoc.html());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        Document expectedDoc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals(expectedDoc.html(), doc.html());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        try {
            DataUtil.parseInputStream(inputStream, "GBK", null, Parser.htmlParser());
        } finally {
            inputStream.close();
        }
    }
}
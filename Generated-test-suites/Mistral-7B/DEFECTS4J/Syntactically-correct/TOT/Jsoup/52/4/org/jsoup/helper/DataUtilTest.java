package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private static final String SAMPLE_UTF8_HTML = "sample.utf8.html";
    private static final String SAMPLE_GB2312_HTML = "sample.gb2312.html";
    private static final String SAMPLE_XML = "sample.xml";

    @Before
    public void setup() throws IOException {
        Files.copy(Paths.get("src/test/resources/" + SAMPLE_UTF8_HTML), new File("samples/utf8.html"));
        Files.copy(Paths.get("src/test/resources/" + SAMPLE_GB2312_HTML), new File("samples/gb2312.html"));
        Files.copy(Paths.get("src/test/resources/" + SAMPLE_XML), new File("samples/sample.xml"));
    }

    @Test
    public void testLoadFileUTF8() throws IOException {
        String charset = "UTF-8";
        Document doc = DataUtil.load(new File("samples/utf8.html"), charset, null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileGB2312() throws IOException {
        String charset = "gb2312";
        Document doc = DataUtil.load(new File("samples/gb2312.html"), charset, null);
        assertNotNull(doc);
        assertEquals("gb2312", doc.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        String charset = "invalid-charset";
        DataUtil.load(new File("samples/utf8.html"), charset, null);
    }

    @Test
    public void testLoadStreamUTF8() throws IOException {
        String charset = "UTF-8";
        try (InputStream in = Files.newInputStream(Paths.get("samples/utf8.html"));) {
            Document doc = DataUtil.load(in, charset, null, Parser.htmlParser());
            assertNotNull(doc);
            assertEquals("UTF-8", doc.outputSettings().charset());
        }
    }

    @Test
    public void testLoadStreamGB2312() throws IOException {
        String charset = "gb2312";
        try (InputStream in = Files.newInputStream(Paths.get("samples/gb2312.html"));) {
            Document doc = DataUtil.load(in, charset, null, Parser.htmlParser());
            assertNotNull(doc);
            assertEquals("gb2312", doc.outputSettings().charset());
        }
    }

    @Test(expected = IOException.class)
    public void testLoadStreamInvalidCharset() throws IOException {
        String charset = "invalid-charset";
        try (InputStream in = Files.newInputStream(Paths.get("samples/utf8.html"));) {
            DataUtil.load(in, charset, null, Parser.htmlParser());
        }
    }

    @Test
    public void testLoadStreamUTF8WithParser() throws IOException {
        String charset = "UTF-8";
        try (InputStream in = Files.newInputStream(Paths.get("samples/utf8.html"));) {
            Document doc = DataUtil.load(in, charset, null, Parser.xmlParser());
            assertNotNull(doc);
            assertEquals("UTF-8", doc.outputSettings().charset());
        }
    }

    @Test
    public void testParseByteDataUTF8() throws IOException {
        String charset = "UTF-8";
        byte[] utf8Data = Files.readAllBytes(Paths.get("samples/utf8.html"));
        ByteBuffer byteData = ByteBuffer.wrap(utf8Data);
        Document doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataGB2312() throws IOException {
        String charset = "gb2312";
        byte[] gb2312Data = Files.readAllBytes(Paths.get("samples/gb2312.html"));
        ByteBuffer byteData = ByteBuffer.wrap(gb2312Data);
        Document doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataDefaultCharset() throws IOException {
        byte[] data = Files.readAllBytes(Paths.get("samples/sample.xml"));
        ByteBuffer byteData = ByteBuffer.wrap(data);
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFileName = "samples/utf8.html";
        String outputFileName = "samples/output.html";
        try (InputStream in = Files.newInputStream(Paths.get(inputFileName));
             OutputStream out = new FileOutputStream(outputFileName)) {
            DataUtil.crossStreams(in, out);
        }
        byte[] outputData = Files.readAllBytes(Paths.get(outputFileName));
        assertEquals(Files.readAllBytes(Paths.get(inputFileName)), outputData);
    }
}
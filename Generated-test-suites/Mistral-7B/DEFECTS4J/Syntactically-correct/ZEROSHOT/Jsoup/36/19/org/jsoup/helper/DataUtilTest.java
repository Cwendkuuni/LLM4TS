package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private InputStream inputStream;
    private File file;
    private Document document;

    @Before
    public void setUp() throws Exception {
        file = new File("testData.html");
        writeTestDataToFile(file);
        inputStream = new FileInputStream(file);
    }

    @After
    public void tearDown() throws Exception {
        if (file.exists()) {
            file.delete();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        document = DataUtil.load(file, " invalidCharset", null);
        assertNotNull(document);
        assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeTestDataToStream(byteArrayOutputStream);
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeTestDataToStream(byteArrayOutputStream);
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCharsetParser() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeTestDataToStream(byteArrayOutputStream);
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Parser parser = Mockito.mock(Parser.class);
        document = DataUtil.load(inputStream, null, null, parser);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() {
        document = DataUtil.load(null, null, null);
        throw new AssertionError("An IOException should be thrown.");
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithCharsetFromMetaTag() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = null;
        if (meta != null) {
            if (meta.hasAttr("http-equiv")) {
                charset = DataUtil.getCharsetFromContentType(meta.attr("content"));
            } else {
                charset = meta.attr("charset");
            }
        }
        if (charset != null) {
            document = DataUtil.parseByteData(byteBuffer, charset, null, Parser.htmlParser());
            assertEquals(charset, document.outputSettings().charset());
        }
    }

    private void writeTestDataToFile(File file) throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test Data</body></html>";
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(testData.getBytes());
        fileOutputStream.close();
    }

    private void writeTestDataToStream(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body>Test Data</body></html>";
        byteArrayOutputStream.write(testData.getBytes());
    }
}
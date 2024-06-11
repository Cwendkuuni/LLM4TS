package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private static final String TEST_FILE_PATH = "path/to/your/test/file.html";
    private Document doc;

    @Before
    public void setUp() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        doc = DataUtil.load(testFile, null, null);
    }

    @After
    public void tearDown() throws IOException {
        if (doc != null) {
            doc.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        Document doc1 = DataUtil.load(testFile, null, null);
        Document doc2 = DataUtil.load(testFile, null, null);

        // Test if both documents are the same
        assertThat(doc1, is(doc2));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc1 = DataUtil.load(inputStream, "UTF-8", null);
        Document doc2 = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());

        // Test if both documents are the same
        assertThat(doc1, is(doc2));
    }

    @Test
    public void testLoadInputStreamWithDifferentParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc1 = DataUtil.load(inputStream, "UTF-8", null);
        Document doc2 = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());

        // Test if both documents are different
        assertThat(doc1.title(), is(not(doc2.title())));
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() throws IOException {
        Document doc = DataUtil.load(null, null, null);
    }

    @Test(expected = IOException.class)
    public void testLoadNullInputStream() throws IOException {
        Document doc = DataUtil.load(null, null, null);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html";
        String contentType3 = "application/xml; charset=UTF-16";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);

        // Test charset1
        assertThat(charset1, is("UTF-8"));

        // Test charset2
        assertThat(charset2, is(nullValue()));

        // Test charset3
        assertThat(charset3, is("UTF-16"));
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc1 = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Document doc2 = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());

        // Test if both documents are the same
        assertThat(doc1, is(doc2));
    }

    @Test
    public void testParseByteDataWithDifferentCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        String charset = "UTF-16";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc1 = DataUtil.parseByteData(byteBuffer, null, charset, Parser.htmlParser());
        Document doc2 = DataUtil.parseByteData(byteBuffer, "UTF-8", charset, Parser.htmlParser());

        // Test if both documents are different
        assertThat(doc1.title(), is(not(doc2.title())));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ByteBuffer byteBuffer1 = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer byteBuffer2 = DataUtil.readToByteBuffer(inputStream, 0);

        // Compare the byte buffers
        byte[] byteArray1 = byteBuffer1.array();
        byte[] byteArray2 = byteBuffer2.array();

        for (int i = 0; i < byteArray1.length; i++) {
            if (byteArray1[i] != byteArray2[i]) {
                throw new AssertionError("Byte arrays are not the same.");
            }
        }
    }
}
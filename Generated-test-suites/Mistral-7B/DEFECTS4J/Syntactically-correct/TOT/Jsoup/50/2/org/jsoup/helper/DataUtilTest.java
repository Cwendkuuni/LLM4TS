package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String testContent = "Test content";
        File testFile = new File("test.html");
        try (Writer writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write(testContent);
        }

        String charsetName = "UTF-8";
        String baseUri = "/test";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertEquals(testContent, doc.html());

        // Test loading file with invalid charset
        String invalidCharset = "invalidCharset";
        doc = DataUtil.load(testFile, invalidCharset, baseUri);
        assertNull(doc);

        // Test loading file with null charset
        doc = DataUtil.load(testFile, null, baseUri);
        assertNotNull(doc);
        assertEquals(testContent, doc.html());

        // Test loading file with empty charset
        charsetName = "";
        doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(testContent, doc.html());

        // Test loading file with unsupported charset
        charsetName = "UNSUPPORTED_CHARSET";
        doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNull(doc);

        // Clean up
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testContent = "Test content";
        InputStream testInputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        String charsetName = "UTF-8";
        String baseUri = "/test";

        Document doc = DataUtil.load(testInputStream, charsetName, baseUri);
        assertEquals(testContent, doc.html());

        // Test loading input stream with invalid charset
        String invalidCharset = "invalidCharset";
        doc = DataUtil.load(testInputStream, invalidCharset, baseUri);
        assertNull(doc);

        // Test loading input stream with null charset
        doc = DataUtil.load(testInputStream, null, baseUri);
        assertNotNull(doc);
        assertEquals(testContent, doc.html());

        // Test loading input stream with empty charset
        charsetName = "";
        doc = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(testContent, doc.html());

        // Test loading input stream with unsupported charset
        charsetName = "UNSUPPORTED_CHARSET";
        doc = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNull(doc);

        // Test loading input stream with different charset than content
        charsetName = "ISO-8859-1";
        doc = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNull(doc);

        // Clean up
        testInputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String testContent = "Test content";
        ByteBuffer testByteBuffer = ByteBuffer.wrap(testContent.getBytes(StandardCharsets.UTF_8));

        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "/test";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();

        Document doc = DataUtil.parseByteData(testByteBuffer, charsetName, baseUri, parser);
        assertEquals(testContent, doc.html());

        // Test parsing byte data with null charset
        charsetName = null;
        doc = DataUtil.parseByteData(testByteBuffer, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(testContent, doc.html());

        // Test parsing byte data with empty charset
        charsetName = "";
        doc = DataUtil.parseByteData(testByteBuffer, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(testContent, doc.html());

        // Test parsing byte data with unsupported charset
        charsetName = "UNSUPPORTED_CHARSET";
        doc = DataUtil.parseByteData(testByteBuffer, charsetName, baseUri, parser);
        assertNull(doc);

        // Test parsing byte data with different charset than content
        charsetName = "ISO-8859-1";
        doc = DataUtil.parseByteData(testByteBuffer, charsetName, baseUri, parser);
        assertNull(doc);

        // Test parsing byte data with BOM
        testByteBuffer.put(UNICODE_BOM);
        String newContent = "New Test content";
        testByteBuffer.put(newContent.getBytes(StandardCharsets.UTF_8));
        testByteBuffer.flip();

        doc = DataUtil.parseByteData(testByteBuffer, charsetName, baseUri, parser);
        assertEquals(newContent, doc.html());

        // Clean up
        testByteBuffer.clear();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "Test content";
        InputStream testInputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(testContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testInputStream);

        assertEquals(expectedByteBuffer, actualByteBuffer);

        // Test reading to byte buffer with null input stream
        InputStream nullInputStream = null;
        actualByteBuffer = DataUtil.readToByteBuffer(nullInputStream);
        assertNull(actualByteBuffer);

        // Test reading to byte buffer with empty input stream
        testInputStream.reset();
        actualByteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertNotNull(actualByteBuffer);
        assertFalse(actualByteBuffer.hasRemaining());

        // Test reading to byte buffer with limited size
        int maxSize = 5;
        actualByteBuffer = DataUtil.readToByteBuffer(testInputStream, maxSize);
        assertEquals(expectedByteBuffer.limit(), actualByteBuffer.limit());
        assertEquals(expectedByteBuffer.position(), actualByteBuffer.position());

        // Test reading to byte buffer with unlimited size
        actualByteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(expectedByteBuffer.limit(), actualByteBuffer.limit());
        assertEquals(expectedByteBuffer.position(), actualByteBuffer.position());

        // Clean up
        testInputStream.close();
    }
}
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputFile, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharsetInHeader() throws IOException {
        String charset = "ISO-8859-1";
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        try {
            Document doc = DataUtil.load(inputFile, charset, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Buffer underflow", e.getMessage());
        }
    }

    @Test
    public void testLoadFileWithInvalidCharsetInMetaTag() throws IOException {
        String charset = null;
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputFile, charset, baseUri);
        Elements metaElements = doc.select("meta[charset]");
        assertEquals(1, metaElements.size());
        Element meta = metaElements.first();
        charset = meta.attr("charset");
        assertEquals("ISO-8859-1", charset);
        doc = DataUtil.load(inputFile, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        String charset = null;
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputFile, charset, baseUri);
        assertEquals(Document.class, doc.getClass());
        charset = DataUtil.defaultCharset;
        doc = DataUtil.load(inputFile, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        InputStream inputStream = new FileInputStream("testFile.html");
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetInHeader() throws IOException {
        String charset = "ISO-8859-1";
        InputStream inputStream = new FileInputStream("testFile.html");
        String baseUri = "http://example.com/";
        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Buffer underflow", e.getMessage());
        }
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.load(inputStream, null, baseUri);
        assertEquals(Document.class, doc.getClass());
        doc = DataUtil.load(inputStream, DataUtil.defaultCharset, baseUri);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetInHeader() throws IOException {
        String charset = "ISO-8859-1";
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(inputFile);
        try {
            Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Buffer underflow", e.getMessage());
        }
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        assertEquals(Document.class, doc.getClass());
        doc = DataUtil.parseInputStream(inputStream, DataUtil.defaultCharset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testFile.html");
        File outputFile = new File("testOutput.html");
        String charset = "UTF-8";
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            Document doc = DataUtil.parseInputStream(outputStream, charset, null, Parser.htmlParser());
            assertNotNull(doc);
            assertEquals(new File("testFile.html").getAbsolutePath(), doc.baseUri());
            FileUtils.deleteQuietly(outputFile);
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @Test
    public void testParseInputStreamWithBomUtf32() throws IOException {
        // Not implemented in DataUtil
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        // Not implemented in DataUtil
    }

    @Test
    public void testParseInputStreamWithBomUtf8() throws IOException {
        String charset = "UTF-8";
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(inputFile);
        inputStream.mark(4);
        byte[] bom = new byte[4];
        inputStream.read(bom);
        assertEquals(0xEF, bom[0]);
        assertEquals(0xBB, bom[1]);
        assertEquals(0xBF, bom[2]);
        inputStream.reset();
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBomUtf8AndInvalidCharset() throws IOException {
        String charset = "ISO-8859-1";
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(inputFile);
        inputStream.mark(4);
        byte[] bom = new byte[4];
        inputStream.read(bom);
        assertEquals(0xEF, bom[0]);
        assertEquals(0xBB, bom[1]);
        assertEquals(0xBF, bom[2]);
        inputStream.reset();
        try {
            Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Buffer underflow", e.getMessage());
        }
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBomUtf8AndNoCharset() throws IOException {
        File inputFile = new File("testFile.html");
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(inputFile);
        inputStream.mark(4);
        byte[] bom = new byte[4];
        inputStream.read(bom);
        assertEquals(0xEF, bom[0]);
        assertEquals(0xBB, bom[1]);
        assertEquals(0xBF, bom[2]);
        inputStream.reset();
        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        assertEquals(Document.class, doc.getClass());
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithValidSize() throws IOException {
        File inputFile = new File("testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithInvalidSize() throws IOException {
        File inputFile = new File("testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, -1);
        assertEquals(ByteBuffer.class, byteBuffer.getClass());
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithEmptyInputStream() throws IOException {
        File inputFile = new File("testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(ByteBuffer.class, byteBuffer.getClass());
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithInterruptedThread() throws IOException, InterruptedException {
        File inputFile = new File("testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // ignore
            } finally {
                inputStream.close();
            }
        });
        t.start();
        Thread.sleep(100);
        byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertEquals(ByteBuffer.class, byteBuffer.getClass());
        inputStream.close();
        t.join();
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=gb2312";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharsetWithValidCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "gb2312";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
    }
}
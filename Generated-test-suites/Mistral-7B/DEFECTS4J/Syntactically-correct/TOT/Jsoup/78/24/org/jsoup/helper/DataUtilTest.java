package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.helper.DataUtil.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test loading a document from a file with UTF-8 encoding.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFromFileUtf8() throws IOException {
        String expectedHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test UTF-8 character: é</p>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testUTF8.html");
        testFile.createNewFile();
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(expectedHtml, 0, expectedHtml.length());
        }

        Document result = DataUtil.load(testFile, "UTF-8", "/");
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedHtml, result.html());

        testFile.delete();
    }

    /**
     * Test loading a document from a file with an unknown encoding.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFromFileUnknownEncoding() throws IOException {
        String expectedHtml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test ISO-8859-1 character: é</p>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testISO88591.html");
        testFile.createNewFile();
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(expectedHtml, 0, expectedHtml.length());
        }

        try {
            Document result = DataUtil.load(testFile, null, "/");
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }

        testFile.delete();
    }

    /**
     * Test loading a document from a stream with UTF-8 encoding.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFromInputStreamUtf8() throws IOException {
        String expectedHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test UTF-8 character: é</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));
        Document result = DataUtil.load(inputStream, "UTF-8", "/");
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedHtml, result.html());
    }

    /**
     * Test loading a document from a stream with an invalid encoding.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFromInputStreamInvalidEncoding() throws IOException {
        String expectedHtml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test ISO-8859-1 character: é</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        try {
            Document result = DataUtil.load(inputStream, null, "/");
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * Test parsing a document with a BOM.
     * @throws IOException on IO error
     */
    @Test
    public void testParseWithBomUtf8() throws IOException {
        String expectedHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test UTF-8 character: é</p>\n" +
                "</body>\n" +
                "</html>";

        byte[] bom = { 0xEF, 0xBB, 0xBF };
        byte[] data = expectedHtml.getBytes(StandardCharsets.UTF_8);
        byte[] combined = new byte[bom.length + data.length];
        System.arraycopy(bom, 0, combined, 0, bom.length);
        System.arraycopy(data, 0, combined, bom.length, data.length);

        InputStream inputStream = new ByteArrayInputStream(combined);
        Document result = DataUtil.parseInputStream(inputStream, "UTF-8", "/", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedHtml, result.html());
    }

    /**
     * Test parsing a document without a BOM.
     * @throws IOException on IO error
     */
    @Test
    public void testParseWithoutBomUtf8() throws IOException {
        String expectedHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test UTF-8 character: é</p>\n" +
                "</body>\n" +
                "</html>";

        byte[] data = expectedHtml.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(data);
        Document result = DataUtil.parseInputStream(inputStream, "UTF-8", "/", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedHtml, result.html());
    }

    /**
     * Test reading a stream into a byte buffer.
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String expectedHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test UTF-8 character: é</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));
        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = expectedHtml.getBytes(StandardCharsets.UTF_8);
        byte[] resultBytes = new byte[result.limit()];
        result.get(resultBytes);

        Assert.assertArrayEquals(expectedBytes, resultBytes);
    }

    /**
     * Test creating a mime boundary.
     */
    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();
        Assert.assertEquals(32, result.length());
        for (int i = 0; i < result.length(); i++) {
            Assert.assertNotNull(result.charAt(i));
            Assert.assertTrue(Character.isDefined(result.charAt(i)) || Character.isWhitespace(result.charAt(i)) || Character.isDigit(result.charAt(i)) || result.charAt(i) == '-');
        }
    }

    /**
     * Test detecting a BOM from a byte buffer.
     */
    @Test
    public void testDetectBom() {
        byte[] bomUtf32 = { 0x00, 0x00, 0xFE, 0xFF };
        byte[] bomUtf16Be = { 0xFE, 0xFF, 0x00, 0x00 };
        byte[] bomUtf8 = { 0xEF, 0xBB, 0xBF };
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        byte[] bomUtf32Data = new byte[bomUtf32.length + data.length];
        System.arraycopy(bomUtf32, 0, bomUtf32Data, 0, bomUtf32.length);
        System.arraycopy(data, 0, bomUtf32Data, bomUtf32.length, data.length);

        byte[] bomUtf16LeData = new byte[bomUtf16Be.length + data.length];
        System.arraycopy(bomUtf16Be, 0, bomUtf16LeData, bomUtf16Be.length, bomUtf16Be.length);
        for (int i = 0; i < bomUtf16LeData.length; i++) {
            bomUtf16LeData[i] = (byte) (bomUtf16Be[i] ^ 0x01);
        }
        System.arraycopy(data, 0, bomUtf16LeData, bomUtf16Be.length, data.length);

        byte[] bomUtf8Data = new byte[bomUtf8.length + data.length];
        System.arraycopy(bomUtf8, 0, bomUtf8Data, 0, bomUtf8.length);
        System.arraycopy(data, 0, bomUtf8Data, bomUtf8.length, data.length);

        ByteBuffer byteBufferUtf32 = DataUtil.readToByteBuffer(new ByteArrayInputStream(bomUtf32Data));
        BomCharset bomUtf32Result = DataUtil.detectCharsetFromBom(byteBufferUtf32);
        Assert.assertEquals("UTF-32", bomUtf32Result.charset);
        Assert.assertEquals(0, bomUtf32Result.offset);

        ByteBuffer byteBufferUtf16Be = DataUtil.readToByteBuffer(new ByteArrayInputStream(bomUtf16BeData));
        BomCharset bomUtf16BeResult = DataUtil.detectCharsetFromBom(byteBufferUtf16Be);
        Assert.assertEquals("UTF-16 BE", bomUtf16BeResult.charset);
        Assert.assertEquals(0, bomUtf16BeResult.offset);

        ByteBuffer byteBufferUtf16Le = DataUtil.readToByteBuffer(new ByteArrayInputStream(bomUtf16LeData));
        BomCharset bomUtf16LeResult = DataUtil.detectCharsetFromBom(byteBufferUtf16Le);
        Assert.assertEquals("UTF-16 LE", bomUtf16LeResult.charset);
        Assert.assertEquals(2, bomUtf16LeResult.offset);

        ByteBuffer byteBufferUtf8 = DataUtil.readToByteBuffer(new ByteArrayInputStream(bomUtf8Data));
        BomCharset bomUtf8Result = DataUtil.detectCharsetFromBom(byteBufferUtf8);
        Assert.assertEquals("UTF-8", bomUtf8Result.charset);
        Assert.assertEquals(3, bomUtf8Result.offset);

        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        BomCharset bomEmptyResult = DataUtil.detectCharsetFromBom(emptyByteBuffer);
        Assert.assertNull(bomEmptyResult);
    }
}
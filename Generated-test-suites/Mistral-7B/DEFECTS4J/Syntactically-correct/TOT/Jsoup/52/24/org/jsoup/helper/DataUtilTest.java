import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class DataUtilTest {

    @Test
    void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, null, null);
        Assertions.assertNotNull(doc);
    }

    @Test
    void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, "UTF-16", null);
        Assertions.assertNotNull(doc);
    }

    @Test
    void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(testStream, null, null);
        Assertions.assertNotNull(doc);
    }

    @Test
    void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream testByteStream = new ByteArrayOutputStream();
        testByteStream.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream testStream = new ByteArrayInputStream(testByteStream.toByteArray());
        Document doc = DataUtil.load(testStream, "UTF-16", null);
        Assertions.assertNotNull(doc);
    }

    @Test
    void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream testByteStream = new ByteArrayOutputStream();
        testByteStream.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream testStream = new ByteArrayInputStream(testByteStream.toByteArray());
        Document doc = DataUtil.load(testStream, null, null, Parser.xmlParser());
        Assertions.assertNotNull(doc);
    }

    @Test
    void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = new byte[(int) outputFile.length()];
        FileInputStream outputInputStream = new FileInputStream(outputFile);
        outputInputStream.read(outputBytes);

        Assertions.assertArrayEquals(inputFile.length(), outputBytes);

        inputFile.delete();
        outputFile.delete();
    }

    @Test
    void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assertions.assertNotNull(doc);
    }

    @Test
    void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String testData = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Test</h1></body></html>";
        byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, "UTF-16", null, Parser.htmlParser());
        Assertions.assertNotNull(doc);
    }
}
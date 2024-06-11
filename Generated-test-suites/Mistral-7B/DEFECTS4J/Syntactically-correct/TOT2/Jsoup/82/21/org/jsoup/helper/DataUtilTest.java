import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnectionException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document document = DataUtil.load(inputStream, null);
        Assert.assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document document = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document document = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        File testOutputFile = new File("test_output.html");
        Document document = DataUtil.load(testOutputFile, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document document = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document document = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-16BE"); // Invalid charset
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document document = DataUtil.load(inputStream, "UTF-16BE"); // Invalid charset
        Assert.fail();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() throws IOException {
        Document document = DataUtil.load((InputStream) null, null, null, DataUtil.Parser.htmlParser());
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullBaseUri() throws IOException {
        Document document = DataUtil.load(new FileInputStream(new File("test.html")), null, null, DataUtil.Parser.htmlParser());
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullParser() throws IOException {
        Document document = DataUtil.load(new FileInputStream(new File("test.html")), null, null, null);
        Assert.fail();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.limit());
    }
}
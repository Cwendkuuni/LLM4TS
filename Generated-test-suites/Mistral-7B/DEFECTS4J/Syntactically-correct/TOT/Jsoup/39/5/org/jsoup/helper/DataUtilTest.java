import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // assuming a valid test file exists
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // assuming a valid test file exists
        Document doc = DataUtil.load(file, "UTF-16LE", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=ISO-8859-1'></head></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=ISO-8859-1'></head></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<xml><data>Test Data</data></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() {
        Document doc = DataUtil.load(null, null, null);
        Assert.fail("Should have thrown IOException");
    }

    @Test(expected = IOException.class)
    public void testLoadNonExistentFile() throws IOException {
        File file = new File("non-existent.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.fail("Should have thrown IOException");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Input".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.fail("Should have thrown IOException");
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
        Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=gb2312'></head></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
        Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=invalidCharset'></head></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
        Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        Assert.fail("Should have thrown IOException");
    }
}
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFileWithCharsetAndBaseUri() throws Exception {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.xml");
        InputStream expectedInputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.load(file, "UTF-8", "/test");

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("UTF-8", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testLoadFileWithDefaultCharsetAndBaseUri() throws Exception {
        String fileContent = "<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.xml");

        Document document = dataUtil.load(file, null, "/test");

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("text/html;charset=UTF-8", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testLoadInputStreamWithCharsetAndBaseUri() throws Exception {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.load(inputStream, "UTF-8", "/test");

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("UTF-8", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharsetAndBaseUri() throws Exception {
        String inputStreamContent = "<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.load(inputStream, null, "/test");

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("text/html;charset=UTF-8", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String inputStreamContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.load(inputStream, "UTF-8", "/test", DataUtil.Parser.xmlParser());

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("text/xml", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testCrossStreams() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        dataUtil.crossStreams(inputStream, outputStream);

        String expectedOutput = "Hello World";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws Exception {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.parseInputStream(inputStream, "UTF-8", "/test", DataUtil.Parser.htmlParser());

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("UTF-8", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testParseInputStreamWithDefaultCharsetAndBaseUri() throws Exception {
        String inputStreamContent = "<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.parseInputStream(inputStream, null, "/test", DataUtil.Parser.htmlParser());

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("text/html;charset=UTF-8", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testParseInputStreamWithParser() throws Exception {
        String inputStreamContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.parseInputStream(inputStream, "UTF-8", "/test", DataUtil.Parser.xmlParser());

        assertEquals("/test/Hello World", document.body().text());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("text/xml", document.outputSettings().contentType());
        assertEquals("UTF-8", document.outputSettings().encoding());
        assertEquals("UTF-8", document.outputSettings().name());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream);

        assertEquals(inputStreamContent.length(), byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, 10);

        assertEquals(10, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String charset = dataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";

        String validatedCharset = dataUtil.validateCharset(charset);

        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithNullCharset() {
        String charset = null;

        String validatedCharset = dataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "5F_6C_6F_63_61_74_65_64_32_5F_64_6F_63_75_6D_65_2D_61_74_65_72";

        String mimeBoundary = dataUtil.mimeBoundary();

        assertEquals(expectedMimeBoundary, mimeBoundary);
    }
}

class BomCharset {
    String charset;
    boolean offset;

    public BomCharset(String charset, boolean offset) {
        this.charset = charset;
        this.offset = offset;
    }
}
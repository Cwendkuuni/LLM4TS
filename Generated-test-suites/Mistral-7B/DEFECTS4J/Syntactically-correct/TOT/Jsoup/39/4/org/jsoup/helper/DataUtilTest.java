import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
        parser = mock(Parser.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Document document = new Document();

        InputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] expectedData = "Test data".getBytes();
        outputStream.write(expectedData);
        InputStream expectedInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = dataUtil.load(file, charsetName, baseUri);

        ByteBuffer byteData = dataUtil.readToByteBuffer(expectedInputStream);
        Document expectedDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(expectedDocument, result);
        assertNotNull(result);
    }

    @Test
    public void testLoadInputStreamNullCharsetName() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String baseUri = "/test";

        try {
            dataUtil.load(inputStream, null, baseUri);
            Validate.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamEmptyCharsetName() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String baseUri = "/test";

        try {
            dataUtil.load(inputStream, "", baseUri);
            dataUtil.load(inputStream, null, baseUri);
        } catch (IOException e) {
            Validate.fail("Expected null charsetName to be handled correctly");
        }
    }

    @Test
    public void testLoadInputStreamUnsupportedCharsetName() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String baseUri = "/test";
        String charsetName = "UNSUPPORTED";

        try {
            dataUtil.load(inputStream, charsetName, baseUri);
            Validate.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamMetaDataCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String baseUri = "/test";

        String charsetNameFromContentType = "UTF-16";
        String charsetNameFromMetaTag = "UTF-8";
        String expectedCharsetName = "UTF-8";

        Document document = new Document();
        Element meta = document.createElement("meta");
        meta.attr("http-equiv", "Content-Type");
        meta.attr("content", "text/html; charset=" + charsetNameFromContentType);
        document.body().appendChild(meta);

        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = dataUtil.load(inputStream, null, baseUri);

        assertEquals(expectedCharsetName, result.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamMetaDataAndContentTypeCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String baseUri = "/test";

        String charsetNameFromContentType = "UTF-16";
        String charsetNameFromMetaTag = "UTF-8";
        String expectedCharsetName = "UTF-8";

        Document document = new Document();
        Element meta = document.createElement("meta");
        meta.attr("http-equiv", "Content-Type");
        meta.attr("content", "text/html; charset=" + charsetNameFromContentType);
        document.body().appendChild(meta);

        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = dataUtil.load(inputStream, charsetNameFromContentType, baseUri);

        assertEquals(expectedCharsetName, result.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithNullByteData() {
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = mock(Parser.class);

        Document result = dataUtil.parseByteData(null, charsetName, baseUri, parser);
        assertNull(result);
    }

    @Test
    public void testParseByteDataWithEmptyByteData() {
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = mock(Parser.class);

        Document result = dataUtil.parseByteData(ByteBuffer.allocate(0), charsetName, baseUri, parser);
        assertNull(result);
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = mock(Parser.class);

        byte[] bom = {(byte) 0xFFFE, (byte) 0xFFFE};
        ByteBuffer byteData = ByteBuffer.wrap(bom);

        String docData = "Test data (with UTF-16 BOM)";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes());
        ByteBuffer byteDataWithContent = dataUtil.readToByteBuffer(inputStream);
        byteDataWithContent.position(byteData.limit() + bom.length);

        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document());

        Document result = dataUtil.parseByteData(byteDataWithContent, charsetName, baseUri, parser);
        assertEquals(new Document(), result);
    }
}
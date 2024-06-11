import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;
    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
        parser = mock(Parser.class);
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream("Some HTML content".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testData/test.html");
        Document doc = dataUtil.load(file, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testData/test.html");
        Document doc = dataUtil.load(file, "UTF-16LE", "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testData/test.html");
        dataUtil.load(file, "XYZ", "");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        Document doc = dataUtil.load(inputStream, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        Document doc = dataUtil.load(inputStream, "UTF-16LE", "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        dataUtil.load(inputStream, "XYZ", "");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        Document doc = dataUtil.load(inputStream, null, "", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Some HTML content".getBytes(StandardCharsets.UTF_8));
        Document doc = dataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, "", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charset = "UTF-16LE";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Some HTML content".getBytes(StandardCharsets.UTF_8));
        Document doc = dataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charset, "", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithMissingCharset() {
        String charset = null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Some HTML content".getBytes(StandardCharsets.UTF_8));
        Document doc = dataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charset, "", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String charset = "XYZ";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Some HTML content".getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> dataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charset, "", parser));
    }
}
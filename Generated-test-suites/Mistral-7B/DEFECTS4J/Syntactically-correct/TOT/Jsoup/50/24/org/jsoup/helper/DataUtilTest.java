import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static org.junit.Assert.*;

public class DataUtilTest {

    private static Document inputDocument;
    private static String charsetName = "UTF-8";
    private static String baseUri = "http://example.com";

    @BeforeClass
    public static void setup() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Welcome to Test Page</h1>\n" +
                "</body>\n" +
                "</html>";
        inputDocument = DataUtil.parseString(htmlContent, Parser.htmlParser());
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        OutputStream outputStream = new FileOutputStream(testFile);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(inputDocument.html().getBytes());
        outputStream.write(byteStream.toByteArray());
        outputStream.close();

        Document expectedDocument = inputDocument;
        Document actualDocument = DataUtil.load(testFile, charsetName, baseUri);

        assertEquals(expectedDocument.html(), actualDocument.html());

        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(inputDocument.html().getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());

        try {
            Document actualDocument = DataUtil.load(inputStream, null, baseUri);
            fail("Expected IOException to be thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamsWithInvalidCharset() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(inputDocument.html().getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());

        String invalidCharset = "JklMNOP";
        try {
            Document actualDocument = DataUtil.load(inputStream, invalidCharset, baseUri);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(inputDocument.html().getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());

        Document expectedDocument = inputDocument;
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedByteArray = new byte[0];
        byte[] actualByteArray = outputStream.toByteArray();

        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testParseByteDataWithUTF8BOM() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(UNICODE_BOM);
        byteData.put(inputDocument.html().getBytes());

        String expectedCharset = "UTF-8";
        Document actualDocument = DataUtil.parseByteData(byteData, expectedCharset, baseUri, Parser.htmlParser());

        assertEquals(inputDocument.html(), actualDocument.html());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(UNICODE_BOM);
        byteData.put(inputDocument.html().getBytes());

        String invalidCharset = "JklMNOP";
        try {
            Document actualDocument = DataUtil.parseByteData(byteData, invalidCharset, baseUri, Parser.htmlParser());
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(UNICODE_BOM);
        byteData.put(inputDocument.html().getBytes());

        Document expectedDocument = inputDocument;
        Document actualDocument = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testParseByteDataWithNoBOMAndDefaultCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(inputDocument.html().getBytes());

        String expectedCharset = "UTF-8";
        Document actualDocument = DataUtil.parseByteData(byteData, expectedCharset, baseUri, Parser.htmlParser());

        assertEquals(inputDocument.html(), actualDocument.html());
    }

    @Test
    public void testParseByteDataWithNoBOMAndDetectedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(inputDocument.html().getBytes());

        Document expectedDocument = inputDocument;
        Document actualDocument = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(inputDocument.html().getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(byteStream.toByteArray());
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(inputDocument.html().getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(byteStream.toByteArray());

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }
}
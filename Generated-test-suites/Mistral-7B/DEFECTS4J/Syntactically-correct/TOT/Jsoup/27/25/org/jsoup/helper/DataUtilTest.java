import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "/baseURI");

        Document actualDocument = DataUtil.load(testFile, "UTF-8", "/baseURI");

        Assert.assertEquals(expectedDocument, actualDocument);

        testFile.delete(); // clean up
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "/baseURI");

        Document actualDocument = DataUtil.load(inputStream, null, "/baseURI");

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>你好，世界!</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "/baseURI");

        Document actualDocument = DataUtil.load(inputStream, "gb2312", "/baseURI");

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFromNullFile() throws IOException {
        File nullFile = null;

        DataUtil.load(nullFile, "UTF-8", "/baseURI");
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithNullCharset() throws IOException {
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "</body>\n" +
                "</html>");
        fileWriter.close();

        DataUtil.load(testFile, null, "/baseURI");

        testFile.delete(); // clean up
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithNullCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream nullInputStream = null;

        DataUtil.load(nullInputStream, null, "/baseURI");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "/baseURI");

        Document actualDocument = DataUtil.parseByteData(byteData, null, "/baseURI", Parser.htmlParser());

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>你好，世界!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "/baseURI");

        Document actualDocument = DataUtil.parseByteData(byteData, "gb2312", "/baseURI", Parser.htmlParser());

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>你好，世界!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes(StandardCharsets.UTF_8));

        Mockito.when(Parser.htmlParser().parseInput(Mockito.anyString(), Mockito.eq("/baseURI")))
                .thenReturn(Parser.htmlParser().parseInput(fileContent, "/baseURI"));

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "/baseURI");

        Document actualDocument = DataUtil.parseByteData(byteData, null, "/baseURI", Parser.htmlParser());

        Assert.assertEquals(expectedDocument, actualDocument);

        Mockito.verify(Parser.htmlParser()).parseInput(fileContent, "/baseURI");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";

        String expectedCharset = "gb2312";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, actualCharset);
    }
}
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = DataUtil.parseString(fileContent, Parser.htmlParser());
        Document actualDocument = DataUtil.load(file, StandardCharsets.UTF_8.name(), "/");

        Assert.assertEquals(expectedDocument, actualDocument);

        file.delete();
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parseString(inputStreamContent, Parser.htmlParser());
        Document actualDocument = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "/");

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parseString(inputStreamContent, Parser.htmlParser());
        Document actualDocument = DataUtil.load(inputStream, null, "/");

        Assert.assertEquals(expectedDocument, actualDocument);

        String detectedCharset = DataUtil.getCharsetFromContentType(inputStreamContent);
        Document documentWithDetectedCharset = DataUtil.load(new ByteArrayInputStream(inputStreamContent.getBytes(detectedCharset == null ? StandardCharsets.UTF_8 : Charset.forName(detectedCharset))), null, "/");
        Assert.assertEquals(expectedDocument, documentWithDetectedCharset);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"invalidCharset\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        try {
            Document document = DataUtil.load(file, "invalidCharset", "/");
            Assert.fail("Expected IOException");
        } finally {
            file.delete();
        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parseString(inputStreamContent, Parser.htmlParser());
        Document actualDocument = DataUtil.parseByteData(byteBuffer, StandardCharsets.UTF_8.name(), "/", Parser.htmlParser());

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithNoCharset() {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parseString(inputStreamContent, Parser.htmlParser());
        Document actualDocument = DataUtil.parseByteData(byteBuffer, null, "/", Parser.htmlParser());

        Assert.assertEquals(expectedDocument, actualDocument);

        String detectedCharset = DataUtil.getCharsetFromContentType(inputStreamContent);
        ByteBuffer byteBufferWithDetectedCharset = ByteBuffer.wrap(inputStreamContent.getBytes(detectedCharset == null ? StandardCharsets.UTF_8 : Charset.forName(detectedCharset)));
        Document documentWithDetectedCharset = DataUtil.parseByteData(byteBufferWithDetectedCharset, null, "/", Parser.htmlParser());
        Assert.assertEquals(expectedDocument, documentWithDetectedCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, 50);

        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);
    }
}
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();

        Document expected = DataUtil.load(file, "UTF-8", "/");

        InputStream inputStreamMock = Mockito.mock(InputStream.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(byteArrayInputStream.readAllBytes());
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        Document actual = DataUtil.load(inputStreamMock, "UTF-8", "/");

        Assert.assertEquals(expected, actual);

        file.delete();
    }

    @Test
    public void testLoadStreamWithCharsetDetectedFromMetaTag() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n</head>\n<body>\n<p>你好，世界!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document expected = DataUtil.load(inputStream, null, "/");

        Document actual = DataUtil.load(inputStream, null, "/");

        Assert.assertEquals(expected, actual);

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadStreamWithInvalidCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"XYZ-123\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Invalid charset test!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        DataUtil.load(inputStream, "XYZ-123", "/");

        inputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteData = Charset.forName("UTF-8").decode(ByteBuffer.wrap(content.getBytes()));

        Document expected = DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());

        Document actual = DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n<html>\n<head>\n<meta charset=\"gb2312\">\n</head>\n<body>\n<p>你好，世界!</p>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteData = Charset.forName("gb2312").decode(ByteBuffer.wrap(content.getBytes()));

        Document expected = DataUtil.parseByteData(byteData, "gb2312", "/", Parser.htmlParser());

        Document actual = DataUtil.parseByteData(byteData, "gb2312", "/", Parser.htmlParser());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        ByteBuffer expected = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'});

        ByteBuffer actual = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(expected, actual);

        inputStream.close();
    }
}
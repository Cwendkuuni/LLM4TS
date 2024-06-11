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
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = Mockito.mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        ByteBuffer expectedByteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>', '\n'});

        Mockito.when(file.length()).thenReturn(6);
        Mockito.when(file.getBytes()).thenReturn(expectedByteData.array());

        Document actualDocument = DataUtil.load(file, charsetName, baseUri);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals("<html>\n", actualDocument.body().html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>', '\n'});
        Mockito.when(inputStream.readAllBytes()).thenReturn(byteData.array());

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals("<html>\n", actualDocument.body().html());
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";
        Parser parser = Mockito.mock(Parser.class);

        String docData = "<html>\n";

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(Mockito.mock(Document.class));

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals("<html>\n", actualDocument.body().html());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "invalid_charset";
        String baseUri = "https://example.com/";
        Parser parser = Mockito.mock(Parser.class);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // An IOException should be thrown since the charset is not supported
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String actualData = outputStream.toString();
        String expectedData = "Hello World!";

        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(actualCharset);
    }

    @Test
    public void testMimeBoundary() {
        String actualBoundary = DataUtil.mimeBoundary();

        String expectedBoundary = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int boundaryLength = 32;

        Assert.assertEquals(expectedBoundary.substring(0, boundaryLength), actualBoundary.substring(0, boundaryLength));
    }
}
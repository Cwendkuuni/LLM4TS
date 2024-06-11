import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expected = "Expected content of the file";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        String actual = doc.text();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStreamWithUTF8Charset() throws IOException {
        String expected = "Expected content of the input stream";
        InputStream in = new ByteArrayInputStream("Expected content of the input stream".getBytes("UTF-8"));
        Document doc = DataUtil.load(in, "UTF-8", null);
        String actual = doc.text();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String expected = "Expected content of the input stream";
        InputStream in = new ByteArrayInputStream("Expected content of the input stream".getBytes("UTF-8"));
        Document doc = DataUtil.load(in, null, null);
        String actual = doc.text();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseByteDataWithUTF8Charset() throws IOException {
        String expected = "Expected content of the byte data";
        InputStream in = new ByteArrayInputStream("Expected content of the byte data".getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(readToByteBuffer(in), "UTF-8", null, Parser.htmlParser());
        String actual = doc.text();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String expected = "Expected content of the byte data";
        InputStream in = new ByteArrayInputStream("Expected content of the byte data".getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(readToByteBuffer(in), null, null, Parser.htmlParser());
        String actual = doc.text();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String data = "Expected content of the input stream";
        InputStream in = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        byte[] expectedByteArray = data.getBytes("UTF-8");
        byte[] actualByteArray = byteData.array();
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    // Replace the following method with your code for reading a file into a byte array and testing the load method with a file
    private byte[] readFileAsByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[fis.available()];
        fis.read(byteArray);
        fis.close();
        return byteArray;
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "test_file_with_invalid_charset.html";
        byte[] fileContent = readFileAsByteArray(filePath);
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(fileContent), null, null, Parser.htmlParser());
        String actual = doc.text();
        // Add assertions or checks to ensure the document is correctly loaded despite the invalid charset
    }
}
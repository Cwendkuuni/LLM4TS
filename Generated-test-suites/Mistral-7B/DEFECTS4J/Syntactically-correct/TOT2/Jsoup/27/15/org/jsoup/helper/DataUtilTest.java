import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;
import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    // test cases for load methods

    // ... Expert 1's test cases for load methods ...

    // test case for readToByteBuffer method

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html"); // replace with a valid file path
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        in.close();
        // Add more test cases for edge cases and exception handling scenarios
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        DataUtil.readToByteBuffer(in);
    }

    // test case for getCharsetFromContentType method

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/plain";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    // test case for parseByteData method

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byteData.put("<?xml version='1.0' encoding='UTF-8'?>".getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithInvalidParser() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        DataUtil.parseByteData(byteData, "UTF-8", "", new Object()); // replace with an invalid parser
    }
}
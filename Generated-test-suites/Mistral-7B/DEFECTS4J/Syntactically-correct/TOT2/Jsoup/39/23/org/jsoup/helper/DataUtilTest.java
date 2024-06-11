import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html"); // assuming the existence of test.html file
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String fileName = "test.html"; // assuming the existence of test.html file
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws Exception {
        String fileName = "test.html"; // assuming the existence of test.html file
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseByteData() throws Exception {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \n" +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \n" +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(expectedCharset, charset);
    }
}
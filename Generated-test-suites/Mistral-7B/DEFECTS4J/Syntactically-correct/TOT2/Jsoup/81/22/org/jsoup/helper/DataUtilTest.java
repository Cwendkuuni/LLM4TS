import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("test.html"); // assuming test.html exists in the classpath
        Document doc = DataUtil.load(file, charsetName, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        File file = new File("test.html"); // assuming test.html exists in the classpath
        try {
            Document doc = DataUtil.load(file, charsetName, null);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithValidInput() throws IOException {
        String charsetName = "UTF-8";
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><p>Test data</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test data", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><p>Test data</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.xmlParser());
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test Input Data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] expectedOutput = inputData.getBytes("UTF-8");
        byte[] actualOutput = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String charsetName = "UTF-8";
        byte[] bom = { 0xEF, 0xBB, 0xBF };
        InputStream inputStream = new ByteArrayInputStream(new byte[][] { bom, new byte[] { 'T', 'e', 's', 't', ' ', 'I', 'n', 'p', 'u', 't', ' ', 'D', 'a', 't', 'a' } });
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Data", doc.text());
    }
}
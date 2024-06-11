import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testData/validFile.html");
        Document document = DataUtil.load(file, null);
        Assert.assertNotNull(document);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testData/validFileWithCharset.html");
        Document document = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(document);
        // add assertions to check the loaded document
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNotFound() throws IOException {
        File file = new File("not_exist.html");
        DataUtil.load(file, null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testData/validFile.html");
        DataUtil.load(file, "GBK"); // unsupported encoding
    }

    // ... other test cases for load methods with InputStream input and different parsers

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        ByteBuffer byteData = readToByteBuffer(new FileInputStream("testData/validFile.html"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        // add assertions to check the loaded document
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        ByteBuffer byteData = readToByteBuffer(new FileInputStream("testData/validFileWithCharset.html"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
        // add assertions to check the loaded document
    }

    @Test
    public void testParseByteDataWithEmptyByteBuffer() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNull(document);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = readToByteBuffer(new FileInputStream("testData/validFile.html"));
        DataUtil.parseByteData(byteData, "GBK", null, Parser.htmlParser()); // unsupported encoding
    }

    // ... other test cases for parseByteData method with different parsers and edge cases

    private ByteBuffer readToByteBuffer(InputStream inStream) throws IOException {
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        int read;
        while (true) {
            read = inStream.read(buffer);
            if (read == -1) break;
            outStream.write(buffer, 0, read);
        }
        ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
        return byteData;
    }
}
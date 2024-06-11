import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html"); // replace with actual test file path
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws Exception {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws Exception {
        String source = "Source text";
        String target = "Target text";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String targetText = outputStream.toString();
        Assert.assertEquals(target, targetText);
    }
}
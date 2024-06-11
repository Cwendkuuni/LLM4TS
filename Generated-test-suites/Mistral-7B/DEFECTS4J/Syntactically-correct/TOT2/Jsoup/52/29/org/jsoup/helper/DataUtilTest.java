import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FileInputElement;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("input.html");
        Document doc = DataUtil.load(inputFile, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("input.html");
        Document doc = DataUtil.load(inputFile, "UTF-16", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-16"));
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "...</xml>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");
        Document inputDoc = DataUtil.load(inputFile, null, null);
        DataUtil.crossStreams(inputFile.toInputStream(), outputFile.toOutputStream());
        Document outputDoc = DataUtil.load(outputFile, null, null);
        Assert.assertEquals(inputDoc.body().html(), outputDoc.body().html());
    }
}
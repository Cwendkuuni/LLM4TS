import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inFile = new File("sampleFile.html");
        Document doc = DataUtil.load(inFile, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inFile = new File("sampleFile.html");
        Document doc = DataUtil.load(inFile, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        File file = new File("sampleFile.html");
        InputStream input = new FileInputStream(file);
        Document doc = DataUtil.load(input, null);
        Assert.assertNotNull(doc);
        input.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File("sampleFile.html");
        InputStream input = new FileInputStream(file);
        Document doc = DataUtil.load(input, "UTF-8");
        Assert.assertNotNull(doc);
        input.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        File file = new File("sampleFile.html");
        InputStream input = new FileInputStream(file);
        Document doc = DataUtil.load(input, null, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
        input.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inFile = new File("sampleFile.html");
        File outFile = new File("sampleFile_out.html");
        InputStream input = new FileInputStream(inFile);
        OutputStream output = new FileOutputStream(outFile);

        DataUtil.crossStreams(input, output);
        Assert.assertTrue(outFile.length() > 0);

        input.close();
        output.close();
    }
}
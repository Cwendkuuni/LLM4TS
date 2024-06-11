import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileDocument() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(input, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com/";
        try {
            Document doc = DataUtil.load(inputFile, charsetName, baseUri);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithNullInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = null;
        try {
            Document doc = DataUtil.load(input, charsetName, baseUri);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamDocument() throws IOException {
        String inputData = "<?xml encoding='UTF-8'?>\n<html><head></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamDocumentWithNullInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = null;
        try {
            Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.xmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamDocumentWithEmptyInput() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamDocumentWithNullParser() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = new ByteArrayInputStream("<html></html>".getBytes());
        Parser parser = null;
        try {
            Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, parser);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
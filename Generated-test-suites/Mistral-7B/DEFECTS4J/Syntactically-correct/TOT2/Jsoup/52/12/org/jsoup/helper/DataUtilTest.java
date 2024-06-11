package org.jsoup.helper; // make sure the package name is correct

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {

    // Test for load method with valid file and charset
    @Test
    public void testLoadValidFileWithCharset() throws IOException {
        File file = new File("path/to/valid/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    // Test for load method with valid file and default charset
    @Test
    public void testLoadValidFileWithDefaultCharset() throws IOException {
        File file = new File("path/to/valid/file.html");
        Document document = DataUtil.load(file);
        Assert.assertNotNull(document);
    }

    // Test for load method with invalid file
    @Test(expected = IOException.class)
    public void testLoadInvalidFile() throws IOException {
        File file = new File("path/to/invalid/file");
        Document document = DataUtil.load(file);
    }

    // Test for load method with null file
    @Test(expected = NullPointerException.class)
    public void testLoadNullFile() {
        File file = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
    }

    // Test for load method with empty charsetName
    @Test(expected = NullPointerException.class)
    public void testLoadEmptyCharsetName() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
    }

    // Test for load method with null baseUri
    @Test
    public void testLoadNullBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    // Test for load method with InputStream
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    // Test for load method with null InputStream
    @Test(expected = NullPointerException.class)
    public void testLoadNullInputStream() throws IOException {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
    }

    // Test for load method with empty InputStream
    @Test
    public void testLoadEmptyInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/path/to/empty.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertTrue(document.titleText().isEmpty());
    }

    // Test for load method with Parser
    @Test
    public void testLoadWithParser() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    // Test for crossStreams method
    @Test
    public void testCrossStreams() throws IOException {
        File file = new File("path/to/input.txt");
        File outputFile = new File("path/to/output.txt");
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertTrue(outputFile.length() > 0);
    }

    // Test for parseByteData method with valid byte data and charset
    @Test
    public void testParseByteDataWithValidByteDataAndCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/file.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    // Test for parseByteData method with null byteData
    @Test(expected = NullPointerException.class)
    public void testParseByteDataNullByteData() throws IOException {
        ByteBuffer byteData = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());
    }

    // Test for parseByteData method with empty byteData
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataEmptyByteData() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());
    }

    // Test for parseByteData method with invalid charsetName
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataInvalidCharsetName() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/file.html"));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());
    }
}
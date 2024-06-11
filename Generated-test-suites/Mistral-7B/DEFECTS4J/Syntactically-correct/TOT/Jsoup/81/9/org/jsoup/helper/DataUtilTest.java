package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    // Test for load method with valid file and default charset
    @org.junit.Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String testFilePath = "testFilePath.html"; // Replace with the actual path to an HTML file
        File file = new File(testFilePath);
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    // Test for load method with valid file and specified charset
    @org.junit.Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String testFilePath = "testFilePath.html"; // Replace with the actual path to an HTML file
        File file = new File(testFilePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, charsetName);
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals(charsetName, doc.outputSettings().charset());
    }

    // Test for load method with null file
    @org.junit.Test(expected = IOException.class)
    public void testLoadNullFile() {
        Document doc = DataUtil.load(null, null);
        // no need to assert anything as IOException should be thrown
    }

    // Test for load method with empty input stream
    @org.junit.Test
    public void testLoadEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new FileInputStream(new File("emptyFile.txt"));
        Document doc = DataUtil.load(emptyInputStream, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());
        emptyInputStream.close();
    }

    // Test for load method with invalid header charset and valid meta charset
    @org.junit.Test
    public void testLoadInvalidHeaderCharsetValidMetaCharset() throws IOException {
        String htmlCode = "<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes("ISO-8859-1"));
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, "ISO-8859-1");
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
        Assert.assertEquals(baseUri, doc.baseUri());
        inputStream.close();
    }

    // Test for load method with valid header charset and valid meta charset
    @org.junit.Test
    public void testLoadValidHeaderAndMetaCharset() throws IOException {
        String htmlCode = "<html>\n<head>\n<meta charset=\"UTF-8\">\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes("UTF-8"));
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
        Assert.assertEquals(baseUri, doc.baseUri());
        inputStream.close();
    }

    // Test for load method with valid header charset and no meta charset
    @org.junit.Test
    public void testLoadValidHeaderNoMetaCharset() throws IOException {
        String htmlCode = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes("UTF-8"));
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
        Assert.assertEquals(baseUri, doc.baseUri());
        inputStream.close();
    }

    // Test for load method with invalid header and meta charset
    @org.junit.Test
    public void testLoadInvalidHeaderMetaCharset() throws IOException {
        String htmlCode = "<html>\n<head>\n<meta charset=\"UTF-16\">\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
        inputStream.close();
    }

    // Test for crossStreams method
    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String testFilePath = "testFilePath.txt"; // Replace with the actual path to a file
        File inputFile = new File(testFilePath);
        File outputFile = new File("outputTestFilePath.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
        String expectedContent = "Expected content";
        String actualContent = new String(new FileInputStream(outputFile).readAllBytes(), StandardCharsets.UTF_8);
        Assert.assertEquals(expectedContent, actualContent);
    }

    // Test for parseInputStream method with valid charset and invalid encoding declaration
    @org.junit.Test
    public void testParseInputStreamInvalidEncodingDeclaration() throws IOException {
        String htmlCode = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
        inputStream.close();
    }

    // Test for parseInputStream method with valid charset and valid encoding declaration
    @org.junit.Test
    public void testParseInputStreamValidEncodingDeclaration() throws IOException {
        String htmlCode = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
        inputStream.close();
    }

    // Test for parseInputStream method with invalid charset
    @org.junit.Test(expected = IOException.class)
    public void testParseInputStreamInvalidCharset() throws IOException {
        String htmlCode = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UnknownCharset\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlCode.getBytes("UTF-8"));
        String baseUri = "http://example.com/";
        Document doc = DataUtil.parseInputStream(inputStream, "UnknownCharset", baseUri, Parser.htmlParser());
        // No need to assert anything as IOException should be thrown
        inputStream.close();
    }
}
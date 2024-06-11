package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        String expectedHtml = "expected HTML content";
        File file = new File("test.html");
        Document actualDocument = DataUtil.load(file, "UTF-8", null);
        Assert.assertEquals(expectedHtml, actualDocument.body().html());
    }

    @Test
    public void testLoadInputStreamDocumentWithDefaultCharset() throws IOException {
        String expectedHtml = "expected HTML content";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document actualDocument = DataUtil.load(inputStream, null, null);
        Assert.assertEquals(expectedHtml, actualDocument.body().html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamDocumentWithSpecifiedCharset() throws IOException {
        String expectedHtml = "expected HTML content";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertEquals(expectedHtml, actualDocument.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamDocument() throws IOException {
        String expectedHtml = "expected HTML content";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document actualDocument = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals(expectedHtml, actualDocument.body().html());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() {
        DataUtil.load(null, "UTF-8", null);
    }

    @Test(expected = IOException.class)
    public void testLoadNullInputStream() throws IOException {
        InputStream inputStream = null;
        DataUtil.load(inputStream, "UTF-8", null);
    }

    @Test(expected = IOException.class)
    public void testParseNullInputStream() throws IOException {
        InputStream inputStream = null;
        DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = getClass().getResourceAsStream("/test.html");
        OutputStream output = new FileOutputStream("output.html");
        DataUtil.crossStreams(input, output);
        input.close();
        output.close();

        String expectedHtml = "expected HTML content";
        Document actualDocument = DataUtil.load(new File("output.html"), "UTF-8", null);
        Assert.assertEquals(expectedHtml, actualDocument.body().html());
    }

    @Test
    public void testParseInputStreamWithEmptyContentType() throws IOException {
        String expectedHtml = "<html></html>";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document actualDocument = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertEquals(expectedHtml, actualDocument.body().html());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidContentType() throws IOException {
        String expectedErrorMessage = "Couldn't parse InputStream as XML";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.xmlParser());
        inputStream.close();
        Assert.assertEquals(expectedErrorMessage, DataUtil.getLastParsingErrorMessage());
    }
}
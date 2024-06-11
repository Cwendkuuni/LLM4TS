package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser.HtmlParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        // typical use case: check document is loaded correctly
        Assert.assertNotNull(doc);
        // edge case: null charsetName
        Document docNullCharset = DataUtil.load(file, null, null);
        Assert.assertNotNull(docNullCharset); // default charset is used
        Assert.assertEquals(DataUtil.defaultCharset, docNullCharset.outputSettings().charset());
        // error case: invalid charsetName
        try {
            Document docInvalidCharset = DataUtil.load(file, "gb18030", null);
            Assert.fail();
        } catch (IllegalCharsetNameException ignored) {
            // expected, as "gb18030" is not a supported charset
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, HtmlParser.htmlParser());
        // typical use case: check document is loaded correctly
        Assert.assertNotNull(doc);
        // edge case: null charsetName
        Document docNullCharset = DataUtil.load(inputStream, null, null, HtmlParser.htmlParser());
        Assert.assertNotNull(docNullCharset); // default charset is used
        Assert.assertEquals(DataUtil.defaultCharset, docNullCharset.outputSettings().charset());
        // error case: invalid charsetName
        try {
            Document docInvalidCharset = DataUtil.load(inputStream, "gb18030", null, HtmlParser.htmlParser());
            Assert.fail();
        } catch (IllegalCharsetNameException ignored) {
            // expected, as "gb18030" is not a supported charset
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8"))));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, HtmlParser.htmlParser());
        // typical use case: check document is loaded correctly
        Assert.assertNotNull(doc);
        // edge case: null charsetName
        Document docNullCharset = DataUtil.parseByteData(byteData, null, null, HtmlParser.htmlParser());
        Assert.assertNotNull(docNullCharset); // default charset is used
        Assert.assertEquals(DataUtil.defaultCharset, docNullCharset.outputSettings().charset());
        // error case: invalid charsetName
        try {
            Document docInvalidCharset = DataUtil.parseByteData(byteData, "gb18030", null, HtmlParser.htmlParser());
            Assert.fail();
        } catch (IllegalCharsetNameException ignored) {
            // expected, as "gb18030" is not a supported charset
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        // typical use case: check the content is correctly copied from inputStream to outputStream
        String output = outputStream.toString(Charset.forName("UTF-8"));
        Assert.assertEquals(input, output);
    }
}
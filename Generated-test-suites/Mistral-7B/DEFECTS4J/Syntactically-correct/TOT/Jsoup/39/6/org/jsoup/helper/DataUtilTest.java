package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

    private static Document doc;
    private static Parser parser;

    @BeforeClass
    public static void setup() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with no charset specified
        doc = DataUtil.load(file, null, null);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        // Test loading a file with an invalid charset
        File invalidFile = new File("test_invalid.html");
        Assert.assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(invalidFile, "invalid_charset", null));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with no charset specified
        doc = DataUtil.load(inputStream, null, null);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Test</title></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document doc = DataUtil.parseByteData(byteData, null, null, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with a charset specified in the content type header
        String contentType = "text/html; charset=UTF-8";
        doc = DataUtil.parseByteData(byteData, contentType, null, parser);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with a charset specified in a meta tag
        html = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><p>Test</p></body></html>";
        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        byteData = DataUtil.readToByteBuffer(inputStream);

        doc = DataUtil.parseByteData(byteData, null, null, parser);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());

        // Test with a charset specified in both the content type header and a meta tag
        contentType = "text/html; charset=GB2312";
        html = "<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"" + contentType + "\"><title>Test</title></head><body><p>Test</p></body></html>";
        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        byteData = DataUtil.readToByteBuffer(inputStream);

        doc = DataUtil.parseByteData(byteData, null, null, parser);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset()); // The content type header takes precedence

        // Test with a charset specified in a meta tag that's not supported
        html = "<html><head><meta charset=\"EUC-JP\"><title>Test</title></head><body><p>Test</p></body></html>";
        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        byteData = DataUtil.readToByteBuffer(inputStream);

        doc = DataUtil.parseByteData(byteData, null, null, parser);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }
}
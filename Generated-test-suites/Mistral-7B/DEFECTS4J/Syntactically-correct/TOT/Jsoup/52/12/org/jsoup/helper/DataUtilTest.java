package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "";
        File inputFile = new File("path/to/your/test.html");
        Document doc = DataUtil.load(inputFile, expectedCharset, expectedBaseUri);

        // Test typical usage
        Assert.assertNotNull(doc);
        Assert.assertFalse(doc.outputSettings().contentType().isEmpty());
        Assert.assertEquals("text/html", doc.outputSettings().contentType());
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());

        // Test edge case: empty file
        doc = DataUtil.load(new File("path/to/empty.html"), expectedCharset, expectedBaseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());

        // Test error case: non-existent file
        Assert.assertThrows(IOException.class, () -> DataUtil.load(new File("path/to/non-existent.html"), expectedCharset, expectedBaseUri));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expectedCharset = "ISO-8859-1";
        String expectedBaseUri = "";
        String inputStreamData = "<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName(expectedCharset)));
        Document doc = DataUtil.load(inputStream, expectedCharset, expectedBaseUri, DataUtil.Parser.htmlParser());

        // Test typical usage
        Assert.assertNotNull(doc);
        Assert.assertFalse(doc.outputSettings().contentType().isEmpty());
        Assert.assertEquals("text/html", doc.outputSettings().contentType());
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());

        // Test edge case: empty input stream
        doc = DataUtil.load(new ByteArrayInputStream(new byte[0]), expectedCharset, expectedBaseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());

        // Test error case: non-UTF-8 input stream
        inputStream = new ByteArrayInputStream("not UTF-8".getBytes());
        Assert.assertThrows(IOException.class, () -> DataUtil.load(inputStream, expectedCharset, expectedBaseUri, DataUtil.Parser.htmlParser()));
    }

    @Test
    public void testParseByteData() throws IOException {
        String expectedCharset = "ISO-8859-1";
        String expectedBaseUri = "";
        byte[] byteData = "<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><body>Test</body></html>".getBytes(Charset.forName(expectedCharset));
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
        Document doc = DataUtil.parseByteData(byteBuffer, expectedCharset, expectedBaseUri, DataUtil.Parser.htmlParser());

        // Test typical usage
        Assert.assertNotNull(doc);
        Assert.assertFalse(doc.outputSettings().contentType().isEmpty());
        Assert.assertEquals("text/html", doc.outputSettings().contentType());
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());

        // Test edge case: empty byte buffer
        byteData = new byte[0];
        byteBuffer = ByteBuffer.wrap(byteData);
        doc = DataUtil.parseByteData(byteBuffer, expectedCharset, expectedBaseUri, DataUtil.Parser.htmlParser());
        Assert.assertNull(doc);

        // Test error case: non-UTF-8 byte data
        byteData = "not UTF-8".getBytes();
        byteBuffer = ByteBuffer.wrap(byteData);
        Assert.assertThrows(IOException.class, () -> DataUtil.parseByteData(byteBuffer, expectedCharset, expectedBaseUri, DataUtil.Parser.htmlParser()));
    }
}
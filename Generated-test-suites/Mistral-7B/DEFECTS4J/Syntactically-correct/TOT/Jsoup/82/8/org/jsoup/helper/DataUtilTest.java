package org.jsoup.helper.test;

import org.apache.commons.io.FileUtils;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("input.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());

        // Test empty file
        doc = DataUtil.load(new File(""), charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());

        // Test invalid file
        File invalidFile = new File("invalidFile");
        doc = DataUtil.load(invalidFile, charsetName, baseUri);
        Assert.assertNull(doc);

        FileUtils.deleteQuietly(invalidFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        byte[] input = html.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(input);
        String charsetName = "UTF-8";
        String baseUri = null;

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset().name());

        // Test empty input stream
        doc = DataUtil.load(new ByteArrayInputStream(new byte[0]), charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());

        // Test invalid character set
        charsetName = "invalidCharset";
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNull(doc);

        // Test null input stream
        doc = DataUtil.load(null, charsetName, baseUri);
        Assert.assertNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        byte[] input = html.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(input);
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset().name());

        // Test empty input stream
        doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[0]), charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());

        // Test invalid character set
        charsetName = "invalidCharset";
        doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNull(doc);

        // Test null input stream
        doc = DataUtil.parseInputStream(null, charsetName, baseUri, parser);
        Assert.assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello World!";
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        FileWriter fileWriter = new FileWriter(outputFile);

        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = fileWriter;

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertEquals(input, FileUtils.readFileToString(outputFile, StandardCharsets.UTF_8));

        FileUtils.deleteQuietly(inputFile);
        FileUtils.deleteQuietly(outputFile);
    }
}
package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.<meta charset=\"UTF-8\">";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document document = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a test file.", document.text());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStreamUTF8() throws IOException {
        String inputStreamContent = "This is a test input stream.<meta charset=\"UTF-8\">";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a test input stream.", document.text());
    }

    @Test
    public void testLoadInputStreamUnknownCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.<meta charset=\"GBK\">";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a test input stream.", document.text());

        Assert.assertEquals("GBK", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamNoBOMUTF8() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a test input stream.", document.text());
    }

    @Test
    public void testLoadInputStreamNoBOMUnknownCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a test input stream.", document.text());

        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamBOMUTF8() throws IOException {
        String inputStreamContent = "\uFEFFThis is a test input stream.<meta charset=\"UTF-8\">";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a test input stream.", document.text());

        Assert.assertFalse(document.outputSettings().hasBOM());
    }

    @Test
    public void testLoadInputStreamBOMUnknownCharset() throws IOException {
        String inputStreamContent = "\uFEFFThis is a test input stream.<meta charset=\"GBK\">";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a test input stream.", document.text());

        Assert.assertEquals("GBK", document.outputSettings().charset());
        Assert.assertTrue(document.outputSettings().hasBOM());
    }
}
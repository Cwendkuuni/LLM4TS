package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

 private static final String HTML_WITH_UTF8_CHARSET = "<?xml version='1.0' encoding='UTF-8'?>\n" +
         "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en'>\n" +
         "<head>\n" +
         "<meta charset='UTF-8'>\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Test UTF-8 encoding</p>\n" +
         "</body>\n" +
         "</html>";

 private static final String HTML_WITH_GB2312_CHARSET = "<?xml version='1.0' encoding='GB2312'?>\n" +
         "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en'>\n" +
         "<head>\n" +
         "<meta http-equiv='Content-Type' content='text/html; charset=GB2312'>\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Test GB2312 encoding</p>\n" +
         "</body>\n" +
         "</html>";

 private static File testFileUtf8;
 private static File testFileGb2312;

 @BeforeClass
 public static void setup() {
 try {
 testFileUtf8 = File.createTempFile("testUTF8", ".html");
 testFileGb2312 = File.createTempFile("testGB2312", ".html");
 } catch (IOException e) {
 e.printStackTrace();
 }
 try {
 Files.write(testFileUtf8.toPath(), HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
 Files.write(testFileGb2312.toPath(), HTML_WITH_GB2312_CHARSET.getBytes(StandardCharsets.UTF_8));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadFromFileWithUtf8Charset() throws IOException {
 Document doc = DataUtil.load(testFileUtf8, "UTF-8", null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testLoadFromFileWithMissingCharset() throws IOException {
 Document doc = DataUtil.load(testFileUtf8, null, null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testLoadFromFileWithMissingCharsetAndDefault() throws IOException {
 Document doc = DataUtil.load(testFileUtf8, null, null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 Document docWithCharset = DataUtil.load(testFileUtf8, "UTF-8", null);
 StringWriter writerWithCharset = new StringWriter();
 docWithCharset.writePrettyPrint(writerWithCharset);
 String expectedWithCharset = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expectedWithCharset, writerWithCharset.toString());
 }

 @Test
 public void testLoadFromFileWithGb2312Charset() throws IOException {
 Document doc = DataUtil.load(testFileGb2312, "GB2312", null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\"></head><body><p>Test GB2312 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testLoadFromInputStreamWithUtf8Charset() throws IOException {
 ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamUtf8, "UTF-8", null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testLoadFromInputStreamWithMissingCharset() throws IOException {
 ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamUtf8, null, null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testLoadFromInputStreamWithMissingCharsetAndDefault() throws IOException {
 ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamUtf8, null, null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 Document docWithCharset = DataUtil.load(inputStreamUtf8, "UTF-8", null);
 StringWriter writerWithCharset = new StringWriter();
 docWithCharset.writePrettyPrint(writerWithCharset);
 String expectedWithCharset = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expectedWithCharset, writerWithCharset.toString());
 }

 @Test
 public void testLoadFromInputStreamWithGb2312Charset() throws IOException {
 ByteArrayInputStream inputStreamGb2312 = new ByteArrayInputStream(HTML_WITH_GB2312_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamGb2312, "GB2312", null);
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\"></head><body><p>Test GB2312 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testLoadWithMissingCharsetAndParser() throws IOException {
 ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamUtf8, null, null, Parser.htmlParser());
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testLoadWithMissingCharsetAndParserAndDefaultCharset() throws IOException {
 ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamUtf8, null, null, Parser.htmlParser());
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 Document docWithCharset = DataUtil.load(inputStreamUtf8, "UTF-8", null, Parser.htmlParser());
 StringWriter writerWithCharset = new StringWriter();
 docWithCharset.writePrettyPrint(writerWithCharset);
 String expectedWithCharset = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Test UTF-8 encoding</p></body></html>";
 assertEquals(expectedWithCharset, writerWithCharset.toString());
 }

 @Test
 public void testLoadWithGb2312CharsetAndParser() throws IOException {
 ByteArrayInputStream inputStreamGb2312 = new ByteArrayInputStream(HTML_WITH_GB2312_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamGb2312, "GB2312", null, Parser.htmlParser());
 StringWriter writer = new StringWriter();
 doc.writePrettyPrint(writer);
 String expected = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\"></head><body><p>Test GB2312 encoding</p></body></html>";
 assertEquals(expected, writer.toString());
 }

 @Test(expected = IOException.class)
 public void testLoadWithInvalidCharset() throws IOException {
 ByteArrayInputStream inputStreamInvalidCharset = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStreamInvalidCharset, "InvalidCharset", null);
 }
}
package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

 private static final String TEST_FILE_PATH = "path/to/your/test/file.html";
 private static final String TEST_INPUT_STREAM_CONTENT = "Your input stream content here.";

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 Document doc = DataUtil.load(testFile, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamNullCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes());
 Document doc = DataUtil.load(testInputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes());
 Document doc = DataUtil.load(testInputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes());
 Document doc = DataUtil.load(testInputStream, "UTF-8", null, Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 String testDocumentData = "Your test document data here.";
 ByteBuffer byteData = ByteBuffer.wrap(testDocumentData.getBytes());
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithCustomCharset() {
 String testDocumentData = "Your test document data here.";
 ByteBuffer byteData = ByteBuffer.wrap(testDocumentData.getBytes());
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testDetectCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("EUC-JP", detectedCharset);
 }

 @Test
 public void testDetectCharsetFromContentTypeNull() {
 String contentType = null;
 String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(detectedCharset);
 }
}
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DataUtilTest {

 private static final String CHARSET_UTF8 = "UTF-8";
 private static final String CHARSET_GB2312 = "GB2312";
 private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
 private static final String CHARSET_EUC_JP = "EUC-JP";
 private static final String DEFAULT_CHARSET = "defaultCharset";
 private static final int BUFFER_SIZE = 0x20000;
 private static final int UNICODE_BOM = 0xFEFF;
 private static final String BOUNDARY = "boundary";
 private static final String BOUNDARY_LENGTH = "boundaryLength";

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("testFile.html");
 Document doc = DataUtil.load(testFile, CHARSET_UTF8, null);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File testFile = new File("testFileDoesNotExist.html");
 Document doc = DataUtil.load(testFile, CHARSET_UTF8, null);
 assertNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream testInput = this.getClass().getResourceAsStream("/testData.html");
 Document doc = DataUtil.load(testInput, CHARSET_UTF8, null);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidFile() throws IOException {
 InputStream testInput = new ByteArrayInputStream(new byte[0]);
 Document doc = DataUtil.load(testInput, CHARSET_UTF8, null);
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithCustomParser() throws IOException {
 InputStream testInput = this.getClass().getResourceAsStream("/testData.xml");
 Document doc = DataUtil.load(testInput, CHARSET_UTF8, null, Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 InputStream input = this.getClass().getResourceAsStream("/testData.html");
 DataUtil.crossStreams(input, output);
 assertNotNull(output.toString());
 }

 @Test
 public void testParseByteDataFromBuffer() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 'H', 'e', 'l', 'l', 'o' });
 Document doc = DataUtil.parseByteData(byteData, DEFAULT_CHARSET, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataFromBufferWithCustomParser() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 'H', 'e', 'l', 'l', 'o' });
 Document doc = DataUtil.parseByteData(byteData, DEFAULT_CHARSET, null, Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataFromBufferWithBOM() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) UNICODE_BOM, 'H', 'e', 'l', 'l', 'o' });
 Document doc = DataUtil.parseByteData(byteData, DEFAULT_CHARSET, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataFromBufferWithCharsetMetaTag() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 InputStream input = this.getClass().getResourceAsStream("/testDataWithMetaTag.html");
 DataUtil.crossStreams(input, output);
 ByteBuffer byteData = ByteBuffer.wrap(output.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
 assertEquals(CHARSET_GB2312, meta.attr("charset"));
 }

 @Test
 public void testParseByteDataFromBufferWithXmlDeclaration() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 InputStream input = this.getClass().getResourceAsStream("/testDataWithXmlDeclaration.xml");
 DataUtil.crossStreams(input, output);
 ByteBuffer byteData = ByteBuffer.wrap(output.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
 assertNotNull(doc);
 XmlDeclaration xmlDeclaration = doc.childNode(0);
 assertEquals(CHARSET_ISO_8859_1, xmlDeclaration.attr("encoding"));
 }

 @Test
 public void testDetectCharsetFromBom() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) UNICODE_BOM, 'H', 'e', 'l', 'l', 'o' });
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals(CHARSET_UTF32, charset);

 byteData.rewind();
 byteData.put((byte) UNICODE_BOM);
 byteData.position(1);
 byteData.put((byte) 0xEF);
 byteData.put((byte) 0xBB);
 byteData.put((byte) 0xBF);
 charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals(CHARSET_UTF8, charset);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=GB2312";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(CHARSET_GB2312, charset);

 contentType = "text/html; charset=ISO-8859-1";
 charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(CHARSET_ISO_8859_1, charset);

 contentType = "text/html; charset=EUC-JP";
 charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(CHARSET_EUC_JP, charset);
 }

 @Test
 public void testValidateCharset() {
 String charset = "UTF-32";
 assertEquals(CHARSET_UTF32, DataUtil.validateCharset(charset));

 charset = "UTF-8";
 assertEquals(CHARSET_UTF8, DataUtil.validateCharset(charset));

 charset = "GB2312";
 assertEquals(CHARSET_GB2312, DataUtil.validateCharset(charset));

 charset = "ISO-8859-1";
 assertEquals(CHARSET_ISO_8859_1, DataUtil.validateCharset(charset));

 charset = "EUC-JP";
 assertEquals(CHARSET_EUC_JP, DataUtil.validateCharset(charset));

 charset = null;
 assertNull(DataUtil.validateCharset(charset));

 charset = "invalidCharset";
 assertNull(DataUtil.validateCharset(charset));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertTrue(boundary.length() == 32);
 }
}
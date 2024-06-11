package org.jsoup.helper;

 import static org.junit.Assert.*;
 import org.jsoup.nodes.Document;
 import org.jsoup.parser.Parser;
 import org.junit.Test;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.nio.ByteBuffer;
 import java.nio.charset.Charset;
 import java.util.Locale;

public class DataUtilTest {

 private static final String TEST_DATA = "Test data for DataUtil class.";
 private static final String DEFAULT_CHARSET = "UTF-8";
 private static final String BASE_URI = "http://example.com/";
 private static final String CHARSET_GB2312 = "GB2312";

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("testData.txt");
 Document doc = DataUtil.load(testFile, DEFAULT_CHARSET, BASE_URI);
 assertEquals(TEST_DATA, doc.body().text());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream testStream = new ByteArrayInputStream(TEST_DATA.getBytes(DEFAULT_CHARSET));
 Document doc = DataUtil.load(testStream, DEFAULT_CHARSET, BASE_URI);
 assertEquals(TEST_DATA, doc.body().text());
 }

 @Test
 public void testLoadInputStreamWithCustomCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream(TEST_DATA.getBytes(CHARSET_GB2312));
 Document doc = DataUtil.load(testStream, CHARSET_GB2312, BASE_URI);
 assertEquals(TEST_DATA, doc.body().text());
 }

 @Test
 public void testParseByteData() throws IOException {
 String testDataWithBOM = "\ufeff" + TEST_DATA;
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testDataWithBOM.getBytes(DEFAULT_CHARSET));
 InputStream testInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), DEFAULT_CHARSET, BASE_URI, Parser.htmlParser());
 assertEquals(TEST_DATA, doc.body().text());
 }

 @Test
 public void testParseByteDataWithCustomCharset() throws IOException {
 String testDataWithBOM = "\ufeff" + TEST_DATA;
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testDataWithBOM.getBytes(CHARSET_GB2312));
 InputStream testInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), CHARSET_GB2312, BASE_URI, Parser.htmlParser());
 assertEquals(TEST_DATA, doc.body().text());
 }

 @Test
 public void testParseByteDataDetectCharset() throws IOException {
 String testDataWithMetaCharset = "<html lang=\"en\" charset=\"gb2312\">" + TEST_DATA + "</html>";
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testDataWithMetaCharset.getBytes(DEFAULT_CHARSET));
 InputStream testInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), null, BASE_URI, Parser.htmlParser());
 assertEquals(TEST_DATA, doc.body().text());
 assertEquals(CHARSET_GB2312, doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithHttpEquivContentType() throws IOException {
 String testDataWithHttpEquivContentType = "<html lang=\"en\">" +
         "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"/></head>" +
         TEST_DATA + "</html>";
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testDataWithHttpEquivContentType.getBytes(DEFAULT_CHARSET));
 InputStream testInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), null, BASE_URI, Parser.htmlParser());
 assertEquals(TEST_DATA, doc.body().text());
 assertEquals(CHARSET_GB2312, doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithCharsetInContentType() throws IOException {
 String testDataWithCharsetInContentType = "<html lang=\"en\">" +
         "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + CHARSET_GB2312 + "\"/></head>" +
         TEST_DATA + "</html>";
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testDataWithCharsetInContentType.getBytes(DEFAULT_CHARSET));
 InputStream testInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), null, BASE_URI, Parser.htmlParser());
 assertEquals(TEST_DATA, doc.body().text());
 assertEquals(CHARSET_GB2312, doc.outputSettings().charset());
 }
}
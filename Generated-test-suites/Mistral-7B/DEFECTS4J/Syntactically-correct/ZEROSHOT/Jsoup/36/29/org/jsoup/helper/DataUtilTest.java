package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private Document document;
 private ByteBuffer byteData;
 private File file;
 private InputStream inputStream;
 private FileInputStream fileInputStream;

 @Before
 public void setup() throws Exception {
 MockitoAnnotations.initMocks(this);
 parser = mock(Parser.class);
 dataUtil = new DataUtil();
 file = new File("testFile.html");
 inputStream = new ByteArrayInputStream("testData".getBytes());
 fileInputStream = new FileInputStream(file);
 }

 @After
 public void tearDown() throws Exception {
 if (file != null && file.exists()) {
 file.delete();
 }
 if (fileInputStream != null) {
 fileInputStream.close();
 }
 if (inputStream != null) {
 inputStream.close();
 }
 PowerMockito.resetStaticFields();
 }

 @Test
 public void testLoadFile() throws IOException {
 byteData = dataUtil.load(file, null, null);
 assertEquals("incorrect byte data", byteData.limit(), byteData.remaining());
 document = dataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull("document should not be null", document);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 byteData = dataUtil.readToByteBuffer(inputStream);
 assertEquals("incorrect byte data", byteData.limit(), byteData.remaining());
 document = dataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull("document should not be null", document);
 }

 @Test
 public void testParseByteData() throws IOException {
 byteData = ByteBuffer.allocate(150);
 byteData.put("testData".getBytes());
 byteData.flip();
 document = dataUtil.parseByteData(byteData, "UTF-8", null, parser);
 assertNotNull("document should not be null", document);
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithNullByteData() {
 dataUtil.parseByteData(null, null, null, parser);
 fail("IOException should be thrown");
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithNullParser() {
 byteData = ByteBuffer.allocate(150);
 byteData.put("testData".getBytes());
 byteData.flip();
 dataUtil.parseByteData(byteData, null, null, null);
 fail("IOException should be thrown");
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 assertEquals("incorrect charset", "UTF-8", dataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 assertEquals("null", null, dataUtil.getCharsetFromContentType(null));
 }

}
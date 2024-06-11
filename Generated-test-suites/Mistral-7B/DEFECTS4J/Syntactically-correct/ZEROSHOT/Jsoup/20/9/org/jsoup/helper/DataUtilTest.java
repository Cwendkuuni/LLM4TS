package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

 private DataUtil dataUtil;
 private Document document;
 private Parser parser;
 private File file;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 document = mock(Document.class);
 file = mock(File.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(document);
 when(file.exists()).thenReturn(true);
 when(file.length()).thenReturn(1024);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
 outputStream.write("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>".getBytes());
 FileInputStream fileInputStream = new FileInputStream(file);
 ByteBuffer byteData = dataUtil.readToByteBuffer(fileInputStream);
 Document doc = dataUtil.load(byteData, charsetName, baseUri);

 assertThat(doc, is(notNullValue()));
 assertThat(doc.body().text(), is("Test"));
 }

 @Test(expected = IOException.class)
 public void testLoadFileDoesNotExist() throws IOException {
 String charsetName = null;
 String baseUri = "http://example.com";
 when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(document);
 when(file.exists()).thenReturn(false);

 Document doc = dataUtil.load(file, charsetName, baseUri);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>".getBytes());
 Document doc = dataUtil.load(inputStream, null, "http://example.com");
 }

 @Test
 public void testLoadInputStream() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(document);

 ByteArrayInputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>".getBytes());
 ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
 Document doc = dataUtil.load(byteData, charsetName, baseUri);

 assertThat(doc, is(notNullValue()));
 assertThat(doc.body().text(), is("Test"));
 }

 @Test
 public void testParseByteDataWithNullCharsetName() {
 String charsetName = null;
 String baseUri = "http://example.com";
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

 assertThat(doc, is(notNullValue()));
 }

 @Test
 public void testParseByteDataWithSpecifiedCharsetName() {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
 byte[] byteArray = docData.getBytes(Charset.forName(charsetName));
 byteData.put(byteArray);
 byteData.flip();
 Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

 assertThat(doc, is(notNullValue()));
 assertThat(doc.body().text(), is("Test"));
 }
}
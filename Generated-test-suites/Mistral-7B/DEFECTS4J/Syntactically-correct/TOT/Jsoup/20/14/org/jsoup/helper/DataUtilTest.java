import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private InputStream inputStream;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 // given
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";

 // when
 Document document = dataUtil.load(file, charsetName, baseUri);

 // then
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 // given
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
 String charsetName = null;
 String baseUri = "http://test.com";

 // when
 Document document = dataUtil.load(inputStream, charsetName, baseUri);

 // then
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 // given
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";

 // when
 Document document = dataUtil.load(inputStream, charsetName, baseUri);

 // then
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedParser() throws IOException {
 // given
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Parser parser = mock(Parser.class);

 when(parser.parseInput(anyString(), anyString())).thenReturn(new Document());

 // when
 Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);

 // then
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 // given
 File file = new File("not_a_file");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";

 // when, then
 dataUtil.load(file, charsetName, baseUri);
 }
}
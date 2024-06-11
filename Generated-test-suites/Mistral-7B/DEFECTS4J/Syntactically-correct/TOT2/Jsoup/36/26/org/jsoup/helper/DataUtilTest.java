import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

 // Test for load(File, String, String) method
 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://www.example.com";

 Document expectedDocument = PowerMockito.mock(Document.class);
 PowerMockito.whenNew(Document.class).withParameters(Parser.htmlParser(), charsetName, baseUri).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.load(file, charsetName, baseUri);

 Assert.assertEquals(expectedDocument, actualDocument);
 PowerMockito.verifyNew(Document.class).withParameters(Parser.htmlParser(), charsetName, baseUri);
 }

 // Test for load(InputStream, String, String) method
 @Test
 public void testLoadInputStream() throws IOException {
 byte[] testData = "<?xml version='1.0' encoding='UTF-8'?>".getBytes();
 InputStream inputStream = new ByteArrayInputStream(testData);
 String charsetName = "UTF-8";
 String baseUri = "http://www.example.com";

 Document expectedDocument = PowerMockito.mock(Document.class);
 PowerMockito.whenNew(Document.class).withParameters(Parser.htmlParser(), charsetName, baseUri).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

 Assert.assertEquals(expectedDocument, actualDocument);
 PowerMockito.verifyNew(Document.class).withParameters(Parser.htmlParser(), charsetName, baseUri);
 }

 // Test for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamWithCustomParser() throws IOException {
 byte[] testData = "<?xml version='1.0' encoding='UTF-8'?>".getBytes();
 InputStream inputStream = new ByteArrayInputStream(testData);
 String charsetName = "UTF-8";
 String baseUri = "http://www.example.com";
 Parser parser = Mockito.mock(Parser.class);

 Document expectedDocument = PowerMockito.mock(Document.class);
 PowerMockito.whenNew(Document.class).withParameters(parser, charsetName, baseUri).thenReturn(expectedDocument);
 PowerMockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);

 Assert.assertEquals(expectedDocument, actualDocument);
 PowerMockito.verifyNew(Document.class).withParameters(parser, charsetName, baseUri);
 PowerMockito.verify(parser).parseInput(Mockito.anyString(), Mockito.anyString());
 }

 // Test for parseByteData(ByteBuffer, String, String, Parser) method
 @Test
 public void testParseByteData() {
 ByteBuffer byteData = ByteBuffer.wrap("<?xml version='1.0' encoding='UTF-8'?>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://www.example.com";
 Parser parser = Parser.htmlParser();

 Document expectedDocument = PowerMockito.mock(Document.class);
 PowerMockito.whenNew(Document.class).withParameters(parser, charsetName, baseUri).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

 Assert.assertEquals(expectedDocument, actualDocument);
 PowerMockito.verifyNew(Document.class).withParameters(parser, charsetName, baseUri);
 }

 // Test for getCharsetFromContentType method with supported charset
 @Test
 public void testGetCharsetFromContentTypeSupported() {
 String contentType = "text/html; charset=UTF-8";

 String expectedCharset = "UTF-8";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);

 Assert.assertEquals(expectedCharset, actualCharset);
 }

 // Test for getCharsetFromContentType method with unsupported charset
 @Test
 public void testGetCharsetFromContentTypeUnsupported() {
 String contentType = "text/html; charset=gb2312";

 String actualCharset = DataUtil.getCharsetFromContentType(contentType);

 Assert.assertNull(actualCharset);
 }
}
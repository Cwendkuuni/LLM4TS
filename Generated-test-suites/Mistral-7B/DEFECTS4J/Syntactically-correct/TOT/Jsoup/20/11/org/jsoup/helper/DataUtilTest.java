import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

 private File testFile;
 private InputStream testInputStream;
 private Document doc;

 @Before
 public void setUp() throws Exception {
 testFile = new File("test.html"); // a valid HTML file to test with
 testInputStream = new FileInputStream(testFile);
 }

 @Test
 public void testLoadFile() throws Exception {
 doc = DataUtil.load(testFile, "UTF-8", "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamNullCharset() throws Exception {
 doc = DataUtil.load(testInputStream, null, "");
 doc = DataUtil.load(testInputStream, "", ""); // default charset UTF-8
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamSpecifiedCharset() throws Exception {
 doc = DataUtil.load(testInputStream, "UTF-16", "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamError() throws Exception {
 testInputStream = new FileInputStream(new File("non-existent-file.html"));
 try {
 doc = DataUtil.load(testInputStream, "UTF-8", "");
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }

 testInputStream.close();
 }

 @Test
 public void testLoadInputStreamWithCustomParser() throws Exception {
 doc = DataUtil.load(testInputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataNullCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
 try {
 doc = DataUtil.parseByteData(byteData, null, "", DataUtil.Parser.htmlParser());
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testParseByteDataValidData() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>'}));
 doc = DataUtil.parseByteData(byteData, "UTF-8", "", DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataInvalidCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>'}));
 try {
 doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "", DataUtil.Parser.htmlParser());
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testParseByteDataEmptyData() {
 ByteBuffer byteData = ByteBuffer.allocate(0);
 doc = DataUtil.parseByteData(byteData, "UTF-8", "", DataUtil.Parser.htmlParser());
 assertNull(doc);
 }

 @Test
 public void testParseByteDataBOM() throws Exception {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{'\\u00EF', '\\u00BB', '<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>'}));
 doc = DataUtil.parseByteData(byteData, "UTF-8", "", DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 doc.outputSettings().charset("UTF-8");
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=ISO-8859-1";
 assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeNullContentType() {
 assertNull(DataUtil.getCharsetFromContentType(null));
 }
}
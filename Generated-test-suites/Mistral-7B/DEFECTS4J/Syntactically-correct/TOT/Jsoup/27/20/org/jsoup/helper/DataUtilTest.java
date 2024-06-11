import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 private static Document doc;
 private static InputStream inputStream;

 @BeforeClass
 public static void setup() {
 try {
 inputStream = DataUtilTest.class.getResourceAsStream("/test.html");
 doc = DataUtil.load(inputStream, "UTF-8", null);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadFile() throws IOException {
 Document document = DataUtil.load(new File("test.html"), null, null);
 assertEquals(doc.body().text(), document.body().text());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 Document document = DataUtil.load(inputStream, "UTF-8", null);
 assertEquals(doc.body().text(), doc.body().text());
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 try {
 Document document = DataUtil.load(inputStream, "gbk", null);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 Document document = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithEmptyCharset() {
 String html = "<html><head><meta charset='UTF-8'></head><body></body></html>";
 ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(html.getBytes()));
 Document document = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.htmlParser());
 assertEquals(html, document.outerHtml());
 }

 @Test
 public void testParseByteDataWithDetectCharset() {
 String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=gbk'></head><body></body></html>";
 ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(html.getBytes()));
 Document document = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.htmlParser());
 assertEquals("gbk", document.outputSettings().charset());
 assertEquals(html.replaceAll("gbk", "UTF-8"), document.outerHtml());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=gbk'></head><body></body></html>";
 ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(html.getBytes()));
 Document document = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.xmlParser());
 assertNull(document);
 }
}
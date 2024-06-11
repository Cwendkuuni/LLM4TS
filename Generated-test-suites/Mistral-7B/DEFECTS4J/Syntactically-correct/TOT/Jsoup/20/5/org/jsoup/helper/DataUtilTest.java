import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 private Document doc;
 private String baseUri = "http://example.com";

 @Before
 public void setUp() {
 try {
 File inputFile = new File("path/to/your/testfile.html");
 doc = DataUtil.load(inputFile, "UTF-8", baseUri);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IO error loading file");
 }
 }

 @Test
 public void testLoadFile() {
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() {
 try (InputStream inputStream = new FileInputStream("path/to/your/testfile.html")) {
 Document docFromStream = DataUtil.load(inputStream, "UTF-8", baseUri);
 assertEquals(doc.html(), docFromStream.html());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IO error loading file");
 }
 }

 @Test
 public void testLoadInputStreamWithNullCharset() {
 try (InputStream inputStream = new FileInputStream("path/to/your/testfile.html")) {
 Document docFromStream = DataUtil.load(inputStream, null, baseUri);
 assertEquals(doc.html(), docFromStream.html());
 String charset = DataUtil.getCharsetFromContentType(doc.head().metaElementSibling(0).attr("content"));
 assertEquals(charset, docFromStream.outputSettings().charset());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IO error loading file");
 }
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() {
 try (InputStream inputStream = new FileInputStream("path/to/your/testfile.html")) {
 Document docFromStream = DataUtil.load(inputStream, "invalidCharset", baseUri);
 assertEquals(doc.html(), docFromStream.html());
 String charset = DataUtil.getCharsetFromContentType(doc.head().metaElementSibling(0).attr("content"));
 assertEquals(charset, docFromStream.outputSettings().charset());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IO error loading file");
 }
 }

 @Test
 public void testReadToByteBuffer() {
 try (FileInputStream inputStream = new FileInputStream("path/to/your/testfile.html")) {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 assertEquals(doc.bytes(), byteData.array());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IO error loading file");
 }
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream("path/to/your/testfile.html"));
 Document docFromByteData = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 assertEquals(doc.html(), docFromByteData.html());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream("path/to/your/testfile.html"));
 Document docFromByteData = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());
 assertEquals(doc.html(), docFromByteData.html());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream("path/to/your/testfile.html"));
 Document docFromByteData = DataUtil.parseByteData(byteData, "invalidCharset", baseUri, Parser.htmlParser());
 assertEquals(doc.html(), docFromByteData.html());
 }
}
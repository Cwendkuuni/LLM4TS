import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

 private File file;
 private String charsetName = "UTF-8";
 private String baseUri = "http://example.com";

 @Before
 public void setUp() {
 try {
 file = new File("path/to/your/testfile.html");
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(file, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamUTF8() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/testfile.html");
 Document doc = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamUnknownCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/testfile_gb2312.html");
 Document doc = DataUtil.load(inputStream, null, baseUri);
 assertNotNull(doc);
 assertEquals("GB2312", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamInvalidCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/testfile.html");
 Document doc = DataUtil.load(inputStream, "invalidCharset", baseUri);
 // test should fail and throw an IOException
 }

 @Test
 public void testParseByteDataUTF8() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/testfile.html");
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataGB2312() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/testfile_gb2312.html");
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 assertNotNull(doc);
 assertEquals("GB2312", doc.outputSettings().charset());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataNullByteData() {
 ByteBuffer byteData = null;
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 // test should fail and throw an IllegalArgumentException
 }
}
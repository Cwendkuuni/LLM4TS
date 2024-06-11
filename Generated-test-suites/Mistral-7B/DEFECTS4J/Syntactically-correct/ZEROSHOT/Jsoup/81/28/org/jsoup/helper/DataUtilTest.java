import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 @Test
 public void testLoadFileDocument() throws Exception {
 File inputFile = new File("path/to/your/test/file.html");
 Document document = DataUtil.load(inputFile, "UTF-8", "");
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamDocument() throws Exception {
 File inputFile = new File("path/to/your/test/file.html");
 FileInputStream inputStream = new FileInputStream(inputFile);
 Document document = DataUtil.load(inputStream, "UTF-8", "");
 assertNotNull(document);
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamDocumentWithCustomParser() throws Exception {
 File inputFile = new File("path/to/your/test/file.xml");
 FileInputStream inputStream = new FileInputStream(inputFile);
 Document document = DataUtil.load(inputStream, "UTF-8", "", DataUtil.parser().xmlParser());
 assertNotNull(document);
 inputStream.close();
 }

 @Test
 public void testCrossStreams() throws Exception {
 File inputFile = new File("path/to/your/test/input/file.html");
 File outputFile = new File("path/to/your/test/output/file.html");

 FileInputStream inputStream = new FileInputStream(inputFile);
 FileOutputStream outputStream = new FileOutputStream(outputFile);

 DataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 outputStream.close();

 // Verify that the output file contains the content from the input file.
 Document inputDocument = DataUtil.load(inputFile, "UTF-8", "");
 Document outputDocument = DataUtil.load(outputFile, "UTF-8", "");
 assertEquals(inputDocument.html(), outputDocument.html());
 }

 @Test
 public void testParseInputStreamDocument() throws Exception {
 String testInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         " <name>John Doe</name>\n" +
         "</root>";

 ByteArrayInputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
 Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.parser().xmlParser());
 assertNotNull(document);
 assertEquals("root", document.tagName());
 }

 @Test
 public void testReadToByteBuffer() throws Exception {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 assertEquals(5, byteBuffer.limit());
 byteBuffer.rewind();
 assertEquals('T', (char) byteBuffer.get());
 byteBuffer.rewind();
 assertEquals('e', (char) byteBuffer.get());
 byteBuffer.rewind();
 assertEquals('s', (char) byteBuffer.get());
 byteBuffer.rewind();
 assertEquals('t', (char) byteBuffer.get());
 byteBuffer.rewind();
 assertEquals(' ', (char) byteBuffer.get());
 }

 @Test
 public void testReadFileToByteBuffer() throws Exception {
 File file = new File("path/to/your/test/file.html");
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
 assertEquals(file.length(), byteBuffer.limit());
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, byteBuffer.limit());
 }

 @Test
 public void testGetCharsetFromContentType() {
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset=invalidCharset"));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(32, boundary.length());
 }

 @Test
 public void testDetectCharsetFromBom() throws Exception {
 ByteBuffer bomUtf32 = ByteBuffer.wrap(new byte[] { 0, 0, 0xFE, 0xFF });
 ByteBuffer bomUtf16BE = ByteBuffer.wrap(new byte[] { 0xFE, 0xFF, 0, 0 });
 ByteBuffer bomUtf16LE = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0, 0 });
 ByteBuffer bomUtf8 = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
 BomCharset utf32 = DataUtil.detectCharsetFromBom(bomUtf32);
 BomCharset utf16BE = DataUtil.detectCharsetFromBom(bomUtf16BE);
 BomCharset utf16LE = DataUtil.detectCharsetFromBom(bomUtf16LE);
 BomCharset utf8 = DataUtil.detectCharsetFromBom(bomUtf8);
 assertEquals("UTF-32", utf32.charset);
 assertEquals(false, utf32.offset);
 assertEquals("UTF-16", utf16BE.charset);
 assertEquals(false, utf16BE.offset);
 assertEquals("UTF-16", utf16LE.charset);
 assertEquals(true, utf16LE.offset);
 assertEquals("UTF-8", utf8.charset);
 assertEquals(true, utf8.offset);
 assertEquals(null, DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0])));
 }
}
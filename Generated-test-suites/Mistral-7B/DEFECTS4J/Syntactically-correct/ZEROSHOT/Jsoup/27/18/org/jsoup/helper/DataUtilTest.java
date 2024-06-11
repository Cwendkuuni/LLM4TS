import org.junit.BeforeClass;import org.junit.Test;import org.junit.runner.RunWith;import org.jsoup.helper.DataUtil;import org.jsoup.nodes.Document;import org.mockito.MockitoAnnotations;import static org.junit.Assert.assertNotNull;import static org.mockito.Mockito.mock;import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;import java.io.File;import java.io.IOException;import java.nio.charset.StandardCharsets;import java.util.regex.Pattern;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)public class DataUtilTest {

 private static final String TEST_FILE = "test.html"; private static File testFile; private static Document doc; private static InputStream inputStream; private static Parser parser;

 @BeforeClasspublic static void setUp() throws Exception { testFile = new File(TEST_FILE); parser = DataUtil.Parser.htmlParser(); }

 @Testpublic void testLoadFile() throws IOException { doc = DataUtil.load(testFile, "UTF-8", null); assertNotNull(doc); }

 @Testpublic void testLoadInputStream() throws IOException { String testData = "<html><head></head><body></body></html>"; inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8)); doc = DataUtil.load(inputStream, "UTF-8", null); assertNotNull(doc); }

 @Test(expected = IOException.class)public void testLoadInputStreamWithInvalidCharset() throws IOException { String testData = "<html><head></head><body></body></html>"; inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8)); when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenThrow(new IOException()); doc = DataUtil.load(inputStream, null, null); }

 @Testpublic void testParseByteData() throws IOException { ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8)); doc = DataUtil.parseByteData(byteData, "UTF-8", null, parser); assertNotNull(doc); }

 @Testpublic void testParseByteDataWithDetectedCharset() throws IOException { String testData = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"><html><head></head><body></body></html>"; ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8)); doc = DataUtil.parseByteData(byteData, null, null, parser); assertNotNull(doc); }

 @Test(expected = IOException.class)public void testParseByteDataWithInvalidCharset() throws IOException { String testData = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"><html><head></head><body></body></html>"; ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8)); when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenThrow(new IOException()); doc = DataUtil.parseByteData(byteData, null, null, parser); }

 @Testpublic void testReadToByteBuffer() throws IOException { String testData = "<html><head></head><body></body></html>"; InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8)); ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream); assertNotNull(byteData); }

 @Testpublic void testGetCharsetFromContentType() { String contentType = "text/html; charset=UTF-8"; String charset = DataUtil.getCharsetFromContentType(contentType); assertNotNull(charset); assertEquals("UTF-8", charset); }

 @Testpublic void testGetCharsetFromContentTypeNoCharset() { String contentType = "text/html"; String charset = DataUtil.getCharsetFromContentType(contentType); assertNull(charset); }

}
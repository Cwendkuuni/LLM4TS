import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 @Test
 public void testMainWithValidArguments() {
 String domainFileName = "domain.shp";
 String problemFileName = "problem.shp";
 JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
 assertTrue(jsJshop.getTree() != null);
 }

 @Test
 public void testMainWithInvalidArguments() {
 String[] args = {"", "problem.shp"};
 JSJshop jsJshop = new JSJshop(args);
 assertFalse(jsJshop.getTree() != null);
 }

 @Test
 public void testMainWithInvalidFileNames() {
 String domainFileName = "";
 String problemFileName = "problem.shp";
 JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
 assertFalse(jsJshop.getTree() != null);
 }

 @Test
 public void testMainWithInvalidParameter() {
 String[] args = {"domain.shp", "invalid", "one"};
 JSJshop jsJshop = new JSJshop(args);
 assertFalse(jsJshop.getTree() != null);
 }

 @Test
 public void testParserFile() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser();
 }

 @Test(expected = JSParserError.class)
 public void testParserFileWithError() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.parserFile("error.shp");
 }

 @Test
 public void testGetBufferedReader() {
 String dir = "";
 String file = "file.shp";
 JSJshop jsJshop = new JSJshop();
 BufferedReader br = jsJshop.getBufferedReader(dir, file);
 assertNotNull(br);
 }

 @Test
 public void testGetBufferedReaderWithNullFile() {
 String dir = "";
 JSJshop jsJshop = new JSJshop();
 BufferedReader br = jsJshop.getBufferedReader(dir, null);
 assertNull(br);
 }

 @Test
 public void testGetAppletURL() {
 JApplet applet = new JFrame();
 String file = "file.shp";
 URL url = JSJshop.getAppletURL(file, applet);
 assertNotNull(url);
 }

 @Test
 public void testGetAppletURLWithNullApplet() {
 String file = "file.shp";
 URL url = JSJshop.getAppletURL(file, null);
 assertNull(url);
 }

 @Test
 public void testProcessToken() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defdomain test)"));
 JSJshop jsJshop = new JSJshop();
 jsJshop.processToken(tokenizer);
 assertEquals("test", jsJshop.dom().getName());
 }
}
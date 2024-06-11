package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Test
 public void testMain() {
 JSJshop jsJshop = new JSJshop();
 String[] args = new String[]{"domainDef-file-name.shp", "problemDef-file-name.shp"};
 jsJshop.main(args);
 // Add assertions here to test the output of the main method
 }

 @Test
 public void testJSJshop() {
 jsJshop = new JSJshop();
 jsJshop.setFile("domainDef-file-name.shp", null);
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test
 public void testSetFile() {
 jsJshop = new JSJshop();
 jsJshop.setFile("domainDef-file-name.shp", new JSTaskAtom("p"));
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test
 public void testGetAddList() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("domainDef-file-name.shp", new JSTaskAtom("p"));
 List<JSLogicalAtom> addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("domainDef-file-name.shp", new JSTaskAtom("p"));
 List<JSLogicalAtom> deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTreeNullDom() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.probSet = null;
 assertNull(jsJshop.getTree());
 }

 @Test(expected = NullPointerException.class)
 public void testGetSolutionNullDom() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.probSet = null;
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testParser() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser();
 // Add assertions here to test the output of the parser
 }

 @Test
 public void testParserFile() {
 JSJshop jsJshop = new JSJshop();
 assertTrue(jsJshop.parserFile("parserTestFile.shp"));
 }

 @Test(expected = IOException.class)
 public void testGetBufferedReaderNullFile() {
 JSJshop jsJshop = new JSJshop();
 assertNull(jsJshop.getBufferedReader(null, ""));
 }

 @Test
 public void testGetBufferedReader() {
 JSJshop jsJshop = new JSJshop();
 String file = "testFile.shp";
 BufferedReader reader = jsJshop.getBufferedReader(".", file);
 assertNotNull(reader);
 }

 @Test
 public void testGetAppletURLNullApplet() {
 JSJshop jsJshop = new JSJshop();
 String file = "testFile.shp";
 assertNull(jsJshop.getAppletURL(file, null));
 }

 @Test
 public void testGetAppletURL() {
 JApplet applet = new JFrame();
 JSJshop jsJshop = new JSJshop();
 String file = "testFile.shp";
 URL url = jsJshop.getAppletURL(file, applet);
 assertNotNull(url);
 }

}
package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;

public class JSJshopTest {

 private JSJshop jsJshop;
 private JSPlanningDomain dom;
 private JSPlanningProblem prob;
 private JSPlan sol;

 @Test
 public void testJSJshopInitialization() {
 jsJshop = new JSJshop();
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopWithDomainAndProblemFiles() {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopWithDomainFileAndPredicate() {
 jsJshop = new JSJshop("domain.pl", new JSTaskAtom("p"));
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopWithInvalidNumberOfArguments() {
 new JSJshop();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopWithInvalidDomainFile() {
 jsJshop = new JSJshop("invalidDomainFile.pl", "problem.pl");
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopWithInvalidProblemFile() {
 jsJshop = new JSJshop("domain.pl", "invalidProblemFile.pl");
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopGetAddList() {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 List<JSLogicalAtom> addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testJSJshopGetDeleteList() {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 List<JSLogicalAtom> deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testJSJshopSetFile() {
 jsJshop = new JSJshop();
 jsJshop.setFile("file.pl", new JSTaskAtom("p"));
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopParser() {
 jsJshop = new JSJshop();
 jsJshop.testParser();
 }

 @Test
 public void testJSJshopParserFile() {
 jsJshop = new JSJshop();
 assertTrue(jsJshop.parserFile("validFile.pl"));
 }

 @Test(expected = IOException.class)
 public void testJSJshopParserFileFailure() {
 jsJshop = new JSJshop();
 assertFalse(jsJshop.parserFile("invalidFile.pl"));
 }

 @Test
 public void testGetBufferedReader() {
 BufferedReader reader = jsJshop.getBufferedReader(".", "validFile.pl");
 assertNotNull(reader);
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetBufferedReaderFileNotFound() {
 BufferedReader reader = jsJshop.getBufferedReader(".", "invalidFile.pl");
 assertNull(reader);
 }

 @Test
 public void testGetAppletURL() {
 JApplet applet = new JFrame();
 URL url = jsJshop.getAppletURL("validFile.pl", applet);
 assertNotNull(url);
 }

 @Test(expected = MalformedURLException.class)
 public void testGetAppletURLMalformedURL() {
 JApplet applet = new JFrame();
 URL url = jsJshop.getAppletURL("invalidURL.pl", applet);
 assertNull(url);
 }

 @Test
 public void testProcessToken() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defdomain test)"));
 jsJshop.processToken(tokenizer);
 assertEquals("test", jsJshop.dom.getName());
 }
}
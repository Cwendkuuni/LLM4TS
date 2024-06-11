package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 jsJshop = new JSJshop();
 }

 @Test
 public void testParserFile() {
 // Replace the following with your expected test case for parserFile method
 // Add the assertions for various scenarios
 jsJshop.parserFile("testFile.shp");
 assertTrue(jsJshop.dom != null);
 }

 @Test
 public void testGetBufferedReader() {
 // Replace the following with your expected test case for getBufferedReader method
 // Add the assertions for various scenarios
 String dir = "src/test/resources/";
 String file = "testFile.shp";
 BufferedReader reader = jsJshop.getBufferedReader(dir, file);
 assertNotNull(reader);
 }

 @Test
 public void testGetAppletURL() {
 // Replace the following with your expected test case for getAppletURL method
 // Add the assertions for various scenarios
 JApplet applet = new JFrame();
 URL url = jsJshop.getAppletURL("testFile.shp", applet);
 assertNotNull(url);
 }

 @Test
 public void testProcessToken() {
 // Replace the following with your expected test case for processToken method
 // Add the assertions for various scenarios
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain testDomain"));
 jsJshop.processToken(tokenizer);
 assertEquals(jsJshop.dom, new JSPlanningDomain());
 }

 @Test
 public void testSetFile() {
 // Replace the following with your expected test case for setFile method
 // Add the assertions for various scenarios
 JSTaskAtom pred = new JSATom("testPredicate");
 jsJshop.setFile("testFile.shp", pred);
 JSPlanningDomain dom = jsJshop.dom();
 JSPlanningProblem prob = jsJshop.prob();
 JSPlan sol = jsJshop.sol();
 JSJshopNode tree = jsJshop.tree();
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 JSPlan expectedSol = new JSPlan();
 // Add the assertions for various scenarios
 // For example, to check if the expected solution is same as the actual solution
 // assertEquals(expectedSol, sol);
 }
}
package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSListPairPlanTStateNodes;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 jsJshop = new JSJshop();
 }

 @Test
 public void testMain() {
 String[] args = {"testDomain.shp", "testProblem.shp"};
 JSJshop.main(args);
 // Add your assertions here to verify the expected behavior of the main method
 }

 @Test
 public void testJSJshop() {
 String domainFileName = "testDomain.shp";
 String problemFileName = "testProblem.shp";
 JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
 // Add your assertions here to verify the expected behavior of the constructor
 }

 @Test
 public void testJSJshopNodeGetTree() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("testFile.shp", null);
 JSJshopNode tree = jsJshop.getTree();
 // Add your assertions here to verify the expected behavior of the getTree method
 }

 @Test
 public void testJSJshopNodeGetSolution() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("testFile.shp", null);
 JSPairPlanTSListNodes solution = jsJshop.getSolution();
 // Add your assertions here to verify the expected behavior of the getSolution method
 }

 @Test
 public void testGetAddList() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("testFile.shp", null);
 JSListLogicalAtoms addList = jsJshop.getAddList();
 // Add your assertions here to verify the expected behavior of the getAddList method
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("testFile.shp", null);
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 // Add your assertions here to verify the expected behavior of the getDeleteList method
 }

 @Test
 public void testParser() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser();
 // Add your assertions here to verify the expected behavior of the testParser method
 }

 @Test
 public void testParserFile() {
 String libraryFile = "testFile.shp";
 assertTrue(jsJshop.parserFile(libraryFile));
 // Add your assertions here to verify the expected behavior of the parserFile method
 }

 @Test
 public void testGetBufferedReader() {
 String dir = ".";
 String file = "testFile.shp";
 BufferedReader bufferedReader = jsJshop.getBufferedReader(dir, file);
 // Add your assertions here to verify the expected behavior of the getBufferedReader method
 }

 @Test
 public void testGetAppletURL() {
 JApplet applet = new Object() {};
 String file = "testFile.shp";
 URL url = jsJshop.getAppletURL(file, applet);
 // Add your assertions here to verify the expected behavior of the getAppletURL method
 }

 @Test
 public void testProcessToken() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
 jsJshop.processToken(tokenizer);
 // Add your assertions here to verify the expected behavior of the processToken method
 }

 @Test
 public void testDom() {
 JSPlanningDomain dom = jsJshop.dom();
 // Add your assertions here to verify the expected behavior of the dom method
 }

 @Test
 public void testProb() {
 JSPlanningProblem prob = jsJshop.prob();
 // Add your assertions here to verify the expected behavior of the prob method
 }

 @Test
 public void testSol() {
 JSPlan sol = jsJshop.sol();
 // Add your assertions here to verify the expected behavior of the sol method
 }
}
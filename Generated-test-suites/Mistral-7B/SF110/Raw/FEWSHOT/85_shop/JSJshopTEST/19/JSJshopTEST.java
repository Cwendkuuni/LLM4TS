package umd.cs.shop;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSJshopTest {

 JSJshop jsJshop = null;

 @Test
 public void testJSJshopEmptyConstructor() {
 jsJshop = new JSJshop();
 assertNotNull(jsJshop.probSet);
 assertNull(jsJshop.tree);
 }

 @Test
 public void testJSJshopWithDomainAndProblemFiles() {
 jsJshop = new JSJshop("domain.shp", "problem.shp");
 assertNotNull(jsJshop.probSet);
 assertNull(jsJshop.tree);
 }

 @Test
 public void testJSJshopWithFileAndPredicate() {
 jsJshop = new JSJshop("file.shp", new JSTaskAtom("p"));
 assertNotNull(jsJshop.probSet);
 assertNull(jsJshop.tree);
 }

 @Test
 public void testGetTree() {
 jsJshop = new JSJshop("domain.shp", "problem.shp");
 jsJshop.setFile("solution.shp", null);
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test
 public void testGetSolution() {
 jsJshop = new JSJshop("domain.shp", "problem.shp");
 jsJshop.setFile("solution.shp", null);
 JSPairPlanTSListNodes solution = jsJshop.getSolution();
 assertNotNull(solution);
 }

 @Test
 public void testGetAddList() {
 jsJshop = new JSJshop("domain.shp", "problem.shp");
 jsJshop.setFile("solution.shp", null);
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteList() {
 jsJshop = new JSJshop("domain.shp", "problem.shp");
 jsJshop.setFile("solution.shp", null);
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testParser() {
 JSJshop jsJshopParser = new JSJshop();
 jsJshopParser.testParser();
 }

 @Test
 public void testParserFile() {
 JSJshop jsJshopParser = new JSJshop();
 assertTrue(jsJshopParser.parserFile("test.shp"));
 }

 @Test(expected = IOException.class)
 public void testGetBufferedReaderNullFile() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.getBufferedReader(null, null);
 }

 @Test(expected = IOException.class)
 public void testGetBufferedReaderNullApplet() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.getBufferedReader("file.shp", null);
 }

 @Test(expected = MalformedURLException.class)
 public void testGetAppletURLNullFile() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.getAppletURL(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testProcessTokenNullTokenizer() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.processToken(null);
 }
}
package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JSJshopTest {

 JSJshop jsJshop = new JSJshop();
 JSPlanningDomain dom;
 JSPlanningProblem prob;
 JSListPlanningProblem probSet;
 JSPlan sol;
 JSPairPlanTSListNodes solution;
 JSJshopNode tree;
 JSPairPlanTSListNodes pair;
 JSListLogicalAtoms addList;
 JSListLogicalAtoms deleteList;

 @Test
 public void testJSJshopConstructionWithDomainAndProblemFiles() {
 jsJshop = new JSJshop("domain.shp", "problem.shp");
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopConstructionWithFileAndPredicate() {
 jsJshop = new JSJshop("file.shp", new JSTaskAtom("p"));
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testParserFile() throws IOException {
 jsJshop.parserFile("testFile.shp");
 assertTrue(jsJshop.parserFile("testFile.shp"));
 }

 @Test(expected = JSParserError.class)
 public void testParserFileWithError() throws IOException {
 jsJshop.parserFile("testFileWithError.shp");
 fail("ParserError expected but not thrown");
 }

 @Test
 public void testGetBufferedReader() {
 BufferedReader br = jsJshop.getBufferedReader(".", "testFile.txt");
 assertNotNull(br);
 }

 @Test
 public void testSetFile() {
 dom = new JSPlanningDomain();
 prob = new JSPlanningProblem();
 probSet = new JSListPlanningProblem();
 jsJshop = new JSJshop();
 jsJshop.setFile("testFile.shp", new JSTaskAtom("p"));
 tree = jsJshop.getTree();
 solution = jsJshop.getSolution();

 assertNotNull(tree);
 assertNotNull(solution);
 addList = jsJshop.getAddList();
 deleteList = jsJshop.getDeleteList();

 assertEquals(addList.size(), 10); // Assuming some known size for test purposes
 assertEquals(deleteList.size(), 5); // Assuming some known size for test purposes

 pair = (JSPairPlanTSListNodes) solution;
 sol = pair.planS().plan();
 assertTrue(sol.isSuccess());
 // Add more assertions to check solution validity
 }

 @Test
 public void testTestParser() {
 jsJshop.testParser();
 // Add more assertions to check parser output validity
 }

}
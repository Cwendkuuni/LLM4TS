package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import umd.cs.shop.JSJshop;
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
 public void testMain() {
 String[] args = new String[]{"domainDef-file-name.shp", "problemDef-file-name.shp"};
 JSJshop.main(args);
 assertTrue(jsJshop.dom != null);
 assertTrue(jsJshop.prob != null);
 }

 @Test
 public void testParser() {
 jsJshop.testParser();
 }

 @Test
 public void testSetFile() {
 JSPlanningProblem prob = new JSPlanningProblem();
 JSPlanningDomain dom = new JSPlanningDomain();
 JSPlan sol = new JSPlan();
 JSJshopNode tree = new JSJshopNode();
 JSJshop jsJshop = new JSJshop();
 JSTaskAtom pred = new JSTaskAtom("testPredicate");
 jsJshop.setFile("testFile.shp", pred);
 assertTrue(jsJshop.getTree() != null);
 assertTrue(jsJshop.getSolution() != null);
 JSJshopNode treeNode = jsJshop.getTree();
 assertTrue(treeNode != null);
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms delList = jsJshop.getDeleteList();
 JSPlan solPlan = jsJshop.getSolution().planS().plan();
 assertFalse(solPlan.isFailure());
 assertTrue(addList != null);
 assertTrue(delList != null);
 JSJshopNode rootNode = treeNode;
 while (rootNode.getParent() != null) {
 rootNode = rootNode.getParent();
 }
 assertEquals("root", rootNode.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testSetFileNullFile() {
 jsJshop.setFile(null, new JSTaskAtom("testPredicate"));
 }

 @Test(expected = NullPointerException.class)
 public void testSetFileNullPredicate() {
 jsJshop.setFile("testFile.shp", null);
 }

 @Test
 public void testDom() {
 JSPlanningDomain dom = jsJshop.dom();
 assertNotNull(dom);
 }

 @Test
 public void testProb() {
 JSPlanningProblem prob = jsJshop.prob();
 assertNotNull(prob);
 }

 @Test
 public void testSol() {
 JSPlan sol = jsJshop.sol();
 assertNotNull(sol);
 }

 @Test
 public void testTree() {
 JSJshopNode tree = jsJshop.tree();
 assertNotNull(tree);
 }

 @Test
 public void testParserFile() {
 JSJshop jsJshop = new JSJshop();
 assertTrue(jsJshop.parserFile("testFile.shp"));
 }

 @Test(expected = IOException.class)
 public void testParserFileIOException() {
 JSJshop jsJshop = new JSJshop();
 throw new IOException();
 }

 @Test(expected = JSParserError.class)
 public void testParserFileParserError() {
 JSJshop jsJshop = new JSJshop();
 throw new JSParserError();
 }
}
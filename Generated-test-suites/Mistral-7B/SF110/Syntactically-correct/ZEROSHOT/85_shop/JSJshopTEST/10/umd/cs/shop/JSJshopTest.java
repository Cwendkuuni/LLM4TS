package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSListPairPlanTStateNodes;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

 @Test
 public void testParserFile() {
 JSJshop jsJshop = new JSJshop();
 assertEquals(true, jsJshop.parserFile("test.shp"));
 }

 @Test
 public void testGetBufferedReader() {
 JSJshop jsJshop = new JSJshop();
 String file = "test.txt";
 BufferedReader reader = jsJshop.getBufferedReader(".", file);
 assertNotNull(reader);
 }

 @Test
 public void testSetFile() {
 JSJshop jsJshop = new JSJshop();
 String file = "test.shp";
 JSTaskAtom pred = new JSTaskAtom("p1(x)", new JSAtomSet("x", new String[] {"a"}));
 JSListPairPlanTStateNodes solution = jsJshop.setFile(file, pred);
 assertNotNull(solution);
 }

 @Test
 public void testGetSolution() {
 JSJshop jsJshop = new JSJshop();
 JSPlanningDomain dom = new JSPlanningDomain();
 JSPlanningProblem prob = new JSPlanningProblem();
 jsJshop.parserFile("test.shp");
 jsJshop.probSet.addElement(prob);
 jsJshop.dom = dom;
 JSListPairPlanTStateNodes solution = jsJshop.getSolution();
 assertNotNull(solution);
 }

 @Test
 public void testGetAddList() {
 JSJshop jsJshop = new JSJshop();
 JSPlanningDomain dom = new JSPlanningDomain();
 JSPlanningProblem prob = new JSPlanningProblem();
 jsJshop.parserFile("test.shp");
 jsJshop.probSet.addElement(prob);
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsJshop = new JSJshop();
 JSPlanningDomain dom = new JSPlanningDomain();
 JSPlanningProblem prob = new JSPlanningProblem();
 jsJshop.parserFile("test.shp");
 jsJshop.probSet.addElement(prob);
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testGetTree() {
 JSJshop jsJshop = new JSJshop();
 JSPlanningDomain dom = new JSPlanningDomain();
 JSPlanningProblem prob = new JSPlanningProblem();
 jsJshop.parserFile("test.shp");
 jsJshop.probSet.addElement(prob);
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test
 public void testDom() {
 JSJshop jsJshop = new JSJshop();
 JSPlanningDomain dom = new JSPlanningDomain();
 JSPlanningDomain domTest = jsJshop.dom();
 assertEquals(dom, domTest);
 }

 @Test
 public void testProb() {
 JSJshop jsJshop = new JSJshop();
 JSPlanningProblem prob = new JSPlanningProblem();
 JSPlanningProblem probTest = jsJshop.prob();
 assertEquals(prob, probTest);
 }

 @Test
 public void testSol() {
 JSJshop jsJshop = new JSJshop();
 JSPlan sol = new JSPlan();
 JSPlan solTest = jsJshop.sol();
 assertEquals(sol, solTest);
 }

 @Test
 public void testJSJshopNodeTree() {
 JSJshop jsJshop = new JSJshop();
 JSPlanningDomain dom = new JSPlanningDomain();
 JSPlanningProblem prob = new JSPlanningProblem();
 jsJshop.parserFile("test.shp");
 jsJshop.probSet.addElement(prob);
 JSJshopNode tree = jsJshop.getTree();
 JSJshopNode treeTest = jsJshop.dom().getInitialState();
 assertEquals(tree, treeTest);
 }
}
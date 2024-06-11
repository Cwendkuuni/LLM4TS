package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 jsJshop = new JSJshop();
 }

 @Test
 public void testMain() {
 String[] args = new String[]{"domainDef-file-name", "problemDef-file-name"};
 JSJshop.main(args);
 assertTrue(jsJshop.dom() != null);
 assertTrue(jsJshop.prob() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainInvalidArguments() {
 String[] args = new String[]{"domainDef-file-name", "problemDef-file-name", "invalidArgument"};
 JSJshop.main(args);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = NumberFormatException.class)
 public void testMainInvalidIntegerArgument() {
 String[] args = new String[]{"domainDef-file-name", "problemDef-file-name", "invalidIntegerArgument"};
 JSJshop.main(args);
 fail("Should have thrown a NumberFormatException");
 }

 @Test
 public void testJSJshop() {
 JSJshop jsJshop = new JSJshop("domainDef-file-name", "problemDef-file-name");
 assertTrue(jsJshop.getTree() != null);
 assertTrue(jsJshop.getSolution() != null);
 }

 @Test
 public void testJSJshopNullFile() {
 JSJshop jsJshop = new JSJshop(null, null);
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopNullPredicate() {
 JSJshop jsJshop = new JSJshop(null, null);
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testGetAddList() {
 JSJshop jsJshop = new JSJshop("domainDef-file-name", "problemDef-file-name");
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsJshop = new JSJshop("domainDef-file-name", "problemDef-file-name");
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testSolutionPrinting() {
 JSJshop jsJshop = new JSJshop("domainDef-file-name", "problemDef-file-name");
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 JSJshopNode tree = jsJshop.getTree();
 JSPlan plan = jsJshop.sol();
 JSUtil.println("Add List:");
 addList.print();
 JSUtil.println("Delete List:");
 deleteList.print();
 JSUtil.println("Tree:");
 tree.print();
 JSUtil.println("Plan:");
 plan.print();
 }

 @Test
 public void testSetFile() {
 JSJshop jsJshop = new JSJshop();
 JSListLogicalAtoms addList = null;
 JSListLogicalAtoms deleteList = null;
 JSJshopNode tree = null;
 JSPlan plan = null;
 jsJshop.setFile("testFile.shp", new JSTaskAtom("testPredicate"));
 addList = jsJshop.getAddList();
 deleteList = jsJshop.getDeleteList();
 tree = jsJshop.getTree();
 plan = jsJshop.sol();
 assertNotNull(addList);
 assertNotNull(deleteList);
 assertNotNull(tree);
 assertNotNull(plan);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileNullFile() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile(null, new JSTaskAtom("testPredicate"));
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileNullPredicate() {
 JSJshop jsJshop = new JSJshop();jsJshop.setFile("testFile.shp", null);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testParser() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser();
 }
}
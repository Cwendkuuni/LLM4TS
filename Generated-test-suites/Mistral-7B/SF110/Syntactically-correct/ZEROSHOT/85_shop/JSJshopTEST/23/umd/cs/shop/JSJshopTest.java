package umd.cs.shop.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 jsJshop = new JSJshop();
 }

 @After
 public void tearDown() {
 jsJshop = null;
 }

 @Test
 public void testParserFile() {
 try {
 jsJshop.parserFile("validFile.shp");
 } catch (Exception e) {
 fail("Exception should not be thrown in parserFile test");
 }
 }

 @Test(expected = JSParserError.class)
 public void testParsingInvalidFile() {
 jsJshop.parserFile("invalidFile.shp");
 }

 @Test
 public void testSolvingSingleProblem() {
 jsJshop.setFile("validFile.shp", null);
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 JSPlan sol = jsJshop.sol();
 assertFalse(sol.isFailure());
 assertNotNull(addList);
 assertNotNull(deleteList);
 }

 @Test(expected = NullPointerException.class)
 public void testSolvingNullProblem() {
 jsJshop.setFile(null, null);
 }

 @Test
 public void testSolvingMultipleProblems() {
 jsJshop.setFile("validFile.shp", null);
 jsJshop.setFile("anotherValidFile.shp", null);
 JSListLogicalAtoms addList1 = jsJshop.getAddList();
 JSListLogicalAtoms deleteList1 = jsJshop.getAddList();
 JSPlan sol1 = jsJshop.sol();
 JSListLogicalAtoms addList2 = jsJshop.getAddList();
 JSListLogicalAtoms deleteList2 = jsJshop.getAddList();
 JSPlan sol2 = jsJshop.sol();
 assertFalse(sol1.isFailure());
 assertFalse(sol2.isFailure());
 assertNotSame(sol1, sol2);
 }

 @Test(expected = JSParserError.class)
 public void testSettingInvalidFile() {
 jsJshop.setFile("invalidFile.shp", null);
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullFile() {
 jsJshop.setFile(null, null);
 }

 @Test
 public void testGettingTree() {
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getAddList();
 JSPlan sol = jsJshop.sol();
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test
 public void testGettingSolution() {
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getAddList();
 JSPlan sol = jsJshop.sol();
 JSPairPlanTSListNodes solution = jsJshop.getSolution();
 assertNotNull(solution);
 }

 @Test
 public void testGettingAddList() {
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGettingDeleteList() {
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }
}
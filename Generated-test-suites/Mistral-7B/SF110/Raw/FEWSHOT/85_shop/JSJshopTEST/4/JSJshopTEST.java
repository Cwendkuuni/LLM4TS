package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

 // Test for JSJshop constructor with domain and problem file names
 @Test
 public void testJSJshopWithDomainAndProblemFiles() {
 JSJshop jsShop = new JSJshop("domain.pl", "problem.pl");
 assertNotNull(jsShop.dom);
 assertNotNull(jsShop.prob);
 assertNull(jsShop.sol);
 assertNull(jsShop.tree);
 assertNull(jsShop.solution);
 }

 // Test for JSJshop constructor with file and predicate
 @Test
 public void testJSJshopWithFileAndPredicate() {
 JSJshop jsShop = new JSJshop("file.pl", new JSTaskAtom("p"));
 assertNotNull(jsShop.dom);
 assertNotNull(jsShop.prob);
 assertNull(jsShop.sol);
 assertNull(jsShop.tree);
 assertNull(jsShop.solution);
 }

 // Test for parserFile method
 @Test
 public void testParserFile() {
 JSJshop jsShop = new JSJshop();
 jsShop.parserFile("testFile.pl");
 assertNotNull(jsShop.dom);
 assertNotNull(jsShop.prob);
 assertNull(jsShop.sol);
 assertNull(jsShop.tree);
 assertNull(jsShop.solution);
 }

 // Test for getSolution method
 @Test
 public void testGetSolution() {
 JSJshop jsShop = new JSJshop("domain.pl", "problem.pl");
 JSJshopNode tree = jsShop.getTree();
 JSPairPlanTSListNodes solution = jsShop.getSolution();
 assertNotNull(tree);
 assertNotNull(solution);
 assertNotNull(solution.planS());
 assertNotNull(solution.planS().plan());
 }

 // Test for getAddList and getDeleteList methods
 @Test
 public void testGetAddListAndGetDeleteList() {
 JSJshop jsShop = new JSJshop("domain.pl", "problem.pl");
 JSJshopNode tree = jsShop.getTree();
 JSListLogicalAtoms addList = jsShop.getAddList();
 JSListLogicalAtoms deleteList = jsShop.getDeleteList();
 assertNotNull(tree);
 assertNotNull(addList);
 assertNotNull(deleteList);
 }
}
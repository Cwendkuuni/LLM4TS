package umd.cs.shop;

 import static org.junit.Assert.*;
 import org.junit.Test;

 import umd.cs.shop.JSJshop;
 import umd.cs.shop.JSPlanningDomain;
 import umd.cs.shop.JSPlanningProblem;
 import umd.cs.shop.JSPlan;
 import umd.cs.shop.JSJshopNode;
 import umd.cs.shop.JSListLogicalAtoms;
 import umd.cs.shop.JSListPairPlanTStateNodes;
 import umd.cs.shop.JSJshopVars;

 public class JSJshopTest {

 @Test
 public void testJSJshopConstructionWithDomainAndProblemFiles() {
 JSJshop jsShop = new JSJshop("domain.txt", "problem.txt");
 assertNotNull(jsShop.getTree());
 assertNotNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopConstructionWithFileAndPredicate() {
 JSJshop jsShop = new JSJshop("file.txt", new JSTaskAtom("p"));
 assertNotNull(jsShop.getTree());
 assertNotNull(jsShop.getSolution());
 }

 @Test
 public void testGetAddList() {
 JSJshop jsShop = new JSJshop("domain.txt", "problem.txt");
 JSListLogicalAtoms addList = jsShop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsShop = new JSJshop("domain.txt", "problem.txt");
 JSListLogicalAtoms deleteList = jsShop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testMainMethodUsage() {
 String[] args = new String[]{"domain.txt", "problem.txt", "one"};
 JSJshop.main(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentInMainMethod() {
 String[] args = new String[]{"domain.txt", "problem.txt", "invalidArg"};
 JSJshop.main(args);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumberInMainMethod() {
 String[] args = new String[]{"domain.txt", "problem.txt", "invalidNumber"};
 JSJshop.main(args);
 }

 @Test
 public void testParserMethod() {
 JSJshop jsShop = new JSJshop();
 jsShop.testParser();
 }
 }
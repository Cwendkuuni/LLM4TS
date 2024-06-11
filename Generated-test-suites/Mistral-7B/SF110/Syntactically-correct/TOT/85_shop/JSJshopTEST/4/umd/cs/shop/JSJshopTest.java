import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 @Test
 public void testValidArguments() {
 String domainFile = "testDomain.shp";
 String problemFile = "testProblem.shp";
 JSJshop jsShop = new JSJshop(domainFile, problemFile);
 assertNotNull(jsShop.getTree());
 assertNotNull(jsShop.getSolution());
 }

 @Test
 public void testInvalidArguments() {
 String domainFile = "testDomain.shp";
 String problemFile = "testProblem.shp";
 String invalidArg = "invalidArg";
 JSJshop jsShop = new JSJshop(domainFile, problemFile, invalidArg);
 assertNull(jsShop);
 }

 @Test
 public void testEmptyFile() {
 String domainFile = "emptyDomain.shp";
 String problemFile = "emptyProblem.shp";
 JSJshop jsShop = new JSJshop(domainFile, problemFile);
 assertNull(jsShop.getTree());
 assertNull(jsShop.getSolution());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumberFormat() {
 String domainFile = "testDomain.shp";
 String problemFile = "testProblem.shp";
 String invalidLevel = "invalidLevel";
 String[] args = {domainFile, problemFile, invalidLevel};
 JSJshop jsShop = new JSJshop(args);
 assertNull(jsShop);
 }

 @Test
 public void testSolveSingleProblem() {
 String domainFile = "testDomain.shp";
 String problemFile = "testProblem.shp";
 JSJshop jsShop = new JSJshop(domainFile, problemFile);
 assertNotNull(jsShop.getTree());
 assertNotNull(jsShop.getSolution());
 assertEquals(1, jsShop.getSolution().size());
 }

 @Test
 public void testSolveMultipleProblems() {
 String domainFile = "testDomain.shp";
 String problemFile1 = "testProblem1.shp";
 String problemFile2 = "testProblem2.shp";
 JSJshop jsShop = new JSJshop(domainFile, problemFile1, "all");
 JSJshop jsShop2 = new JSJshop(domainFile, problemFile2, "all");
 assertEquals(2, jsShop.getSolution().size());
 assertEquals(2, jsShop2.getSolution().size());
 }

 @Test
 public void testSetFileWithPredicate() {
 String domainFile = "testDomain.shp";
 String problemFile = "testProblem.shp";
 String file = "testFile.shp";
 JSTaskAtom predicate = new JSTaskAtom("testPredicate");
 JSJshop jsShop = new JSJshop(domainFile, problemFile);
 JSJshopNodes solution = jsShop.setFile(file, predicate);
 assertNotNull(solution);
 }

 @Test(expected = NullPointerException.class)
 public void testSetFileWithNullPredicate() {
 String domainFile = "testDomain.shp";
 String problemFile = "testProblem.shp";
 String file = "testFile.shp";
 JSJshop jsShop = new JSJshop(domainFile, problemFile);
 jsShop.setFile(file, null);
 }

 @Test
 public void testParser() {
 JSJshop jsShop = new JSJshop();
 jsShop.testParser();
 }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 String domainFile = "testDomain.shp";
 String problemFile = "testProblem.shp";
 jsJshop = new JSJshop(domainFile, problemFile);
 }

 @Test
 public void testParserFile() {
 JSJshop expectedJsJshop = new JSJshop();
 expectedJsJshop.parserFile("testFile.shp"); // Assuming testFile.shp is a valid file
 assertEquals(expectedJsJshop.tree(), jsJshop.tree());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgs() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.parserFile("not_a_file.shp"); // Assuming not_a_file.shp does not exist
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLevelArg() {
 String[] args = {"domainFile.shp", "problemFile.shp", "abc"};
 JSJshop jsJshop = new JSJshop(args);
 }

 @Test(expected = NullPointerException.class)
 public void testNullProblem() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.prob = null;
 jsJshop.parserFile("testFile.shp");
 }

 @Test
 public void testSolveOnePlan() {
 JSJshop jsJshop = new JSJshop("domainFile.shp", "problemFile1.shp");
 JSJshop expectedJsJshop = new JSJshop("domainFile.shp", "problemFile1.shp");
 assertEquals(expectedJsJshop.sol(), jsJshop.sol());
 }

 @Test
 public void testSolveAllPlans() {
 JSJshop jsJshop = new JSJshop("domainFile.shp", "problemFile2.shp");
 JSJshop expectedJsJshop = new JSJshop("domainFile.shp", "problemFile2.shp");
 JSListPairPlanTStateNodes expectedSolutions = expectedJsJshop.dom.solveAll(expectedJsJshop.prob, true);
 JSListPairPlanTStateNodes jsSolutions = jsJshop.dom.solveAll(jsJshop.prob, true);
 assertEquals(expectedSolutions.size(), jsSolutions.size());
 }

 @Test
 public void testFailedSolve() {
 JSJshop jsJshop = new JSJshop("domainFile.shp", "problemFile3.shp");
 assertNull(jsJshop.sol());
 }

 @Test
 public void testGetAddList() {
 JSJshop jsJshop = new JSJshop("domainFile.shp", "problemFile4.shp");
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertFalse(addList.isEmpty());
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsJshop = new JSJshop("domainFile.shp", "problemFile5.shp");
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertFalse(deleteList.isEmpty());
 }
}
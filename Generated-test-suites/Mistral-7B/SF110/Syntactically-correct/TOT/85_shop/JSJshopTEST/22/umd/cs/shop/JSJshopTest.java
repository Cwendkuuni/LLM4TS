import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 String domainFile = "testDomainFile.shp";
 String problemFile = "testProblemFile.shp";
 jsJshop = new JSJshop(domainFile, problemFile);
 }

 @Test
 public void testParserFile() {
 try {
 jsJshop.parserFile("testFile.shp");
 } catch (IOException | JSParserError e) {
 fail("Parser file test failed: " + e.getMessage());
 }
 }

 @Test
 public void testSetFileWithValidPredicate() {
 JSTaskAtom predicate = new JSTaskAtom("testPredicate");
 jsJshop.setFile("testFile.shp", predicate);
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileWithNullFile() {
 jsJshop.setFile(null, new JSTaskAtom("testPredicate"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileWithNullPredicate() {
 JSJshop jsJshop2 = new JSJshop();
 jsJshop2.setFile("testFile.shp", null);
 }

 @Test(expected = JSParserError.class)
 public void testSetFileWithInvalidFile() {
 jsJshop.setFile("testInvalidFile.shp", new JSTaskAtom("testPredicate"));
 }

 @Test
 public void testGetAddList() {
 final List<JSLogicalAtom> addList = jsJshop.getAddList();
 assertNotNull(addList);
 assertTrue(addList.isEmpty());
 }

 @Test
 public void testGetDeleteList() {
 final List<JSLogicalAtom> deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 assertTrue(deleteList.isEmpty());
 }

 @Test
 public void testGetSolution() {
 final JSPairPlanTSListNodes solution = jsJshop.getSolution();
 assertNotNull(solution);
 assertNotNull(solution.planS());
 assertNotNull(solution.planS().plan());
 }

 @Test
 public void testGetTree() {
 final JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test
 public void testMainWithValidArguments() {
 final String[] args = new String[] {"testDomainFile.shp", "testProblemFile.shp", "one"};
 JSJshop.main(args);
 assertTrue(JSJshopVars.allPlans);
 }

 @Test
 public void testMainWithInvalidArguments() {
 final String[] args = new String[] {"testDomainFile.shp", "testProblemFile.shp", "three"};
 JSJshop.main(args);
 assertFalse(JSJshopVars.allPlans);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainWithInvalidNumberOfArguments() {
 final String[] args = new String[] {"testDomainFile.shp"};
 JSJshop.main(args);
 }

}
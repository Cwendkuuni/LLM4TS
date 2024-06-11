import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

 private JSJshop jsJshop;
 private String domainFileName = "testDomain.shp";
 private String problemFileName = "testProblem.shp";

 @Before
 public void setUp() {
 jsJshop = new JSJshop(domainFileName, problemFileName);
 }

 @Test
 public void testParsingFile() {
 FileReader fr = null;
 try {
 fr = new FileReader(domainFileName);
 String expectedContent = "Expected content of the domain file";
 String actualContent = new String(new byte[fr.available()], 0);
 assertEquals(expectedContent, actualContent);
 }
 catch (IOException e) {
 fail("Failed to read file: " + domainFileName);
 }
 finally {
 try {
 if (fr != null) {
 fr.close();
 }
 }
 catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testInvalidNumberOfArguments() {
 String[] args = new String[0];
 JSJshop.main(args);
 assertTrue(JSJshopVars.flagExit);
 }

 @Test
 public void testInvalidDomainFile() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("invalidDomainFile.shp");
 assertFalse(jsJshop.dom != null);
 }

 @Test
 public void testInvalidProblemFile() {
 jsJshop = new JSJshop(domainFileName, "");
 jsJshop.parserFile(problemFileName);
 assertFalse(jsJshop.prob != null);
 }

 @Test
 public void testSolvingProblem() {
 JSListPairPlanTStateNodes plans = jsJshop.solveAll(jsJshop.prob, JSJshopVars.allPlans);
 assertTrue(plans.size() > 0);
 JSPairPlanTSListNodes solution = plans.elementAt(0);
 JSPlan sol = solution.planS().plan();
 assertFalse(sol.isFailure());
 }

 @Test
 public void testSolvingProblemWithNoPlans() {
 jsJshop = new JSJshop(domainFileName, problemFileName);
 jsJshop.probSet = new JSListPlanningProblem();
 JSListPairPlanTStateNodes plans = jsJshop.dom.solveAll(jsJshop.prob, JSJshopVars.allPlans);
 assertFalse(plans.size() > 0);
 }

 @Test
 public void testSolvingProblemWithInvalidArgument() {
 String invalidArgument = "invalidArgument";
 jsJshop = new JSJshop();
 jsJshop.JSJshopVars.flagLevel = Integer.parseInt(invalidArgument);
 jsJshop.parserFile(domainFileName);
 jsJshop.parserFile(problemFileName);
 JSListPairPlanTStateNodes plans = jsJshop.dom.solveAll(jsJshop.prob, JSJshopVars.allPlans);
 assertFalse(plans.size() > 0);
 }

 @Test
 public void testGetTree() {
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test
 public void testGetSolution() {
 JSPairPlanTSListNodes solution = jsJshop.getSolution();
 assertNotNull(solution);
 }

 @Test
 public void testGetAddList() {
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteList() {
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 try {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception while creating JSJshop instance");
 }
 }

 @Test
 public void testParserFile() {
 try {
 jsJshop.parserFile("control.pl");
 } catch (JSParserError e) {
 fail("Error parsing control file: " + e.getMessage());
 }
 }

 @Test
 public void testSolveOneProblem() {
 JSPlanningProblem problem = jsJshop.prob();
 assertEquals("Expecting problem name to be set", "test_problem", problem.Name());
 JSListPairPlanTStateNodes solutions = jsJshop.dom().solveAll(problem, false);
 assertFalse("No solutions found", solutions.isEmpty());
 JSPairPlanTSListNodes pair = solutions.elementAt(0);
 JSPlan sol = pair.planS().plan();
 assertFalse("Solution is failure", sol.isFailure());
 }

 @Test
 public void testSolveMultipleProblems() {
 JSJshop jsJshop2 = new JSJshop("domain.pl", "problem1.pl");
 JSPlanningProblem problem1 = jsJshop2.prob();
 assertEquals("Expecting problem1 name to be set", "test_problem1", problem1.Name());
 JSListPairPlanTStateNodes solutions1 = jsJshop2.dom().solveAll(problem1, false);
 assertFalse("No solutions found for problem1", solutions1.isEmpty());
 JSPairPlanTSListNodes pair1 = solutions1.elementAt(0);
 JSPlan sol1 = pair1.planS().plan();
 assertFalse("Solution1 is failure", sol1.isFailure());

 JSJshop jsJshop3 = new JSJshop("domain.pl", "problem2.pl");
 JSPlanningProblem problem2 = jsJshop3.prob();
 assertEquals("Expecting problem2 name to be set", "test_problem2", problem2.Name());
 JSListPairPlanTStateNodes solutions2 = jsJshop3.dom().solveAll(problem2, false);
 assertFalse("No solutions found for problem2", solutions2.isEmpty());
 JSPairPlanTSListNodes pair2 = solutions2.elementAt(0);
 JSPlan sol2 = pair2.planS().plan();
 assertFalse("Solution2 is failure", sol2.isFailure());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgument() {
 try {
 new JSJshop("", "");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumberFormat() {
 try {
 new JSJshop("args", "123");
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testGetTree() {
 JSJshop jsJshop4 = new JSJshop("domain.pl", "problem.pl");
 JSJshopNode tree = jsJshop4.getTree();
 assertNotNull("Tree should not be null", tree);
 }

 @Test
 public void testGetSolution() {
 JSJshop jsJshop5 = new JSJshop("domain.pl", "problem.pl");
 JSPairPlanTSListNodes solution = jsJshop5.getSolution();
 assertNotNull("Solution should not be null", solution);
 }

 @Test
 public void testGetAddList() {
 JSJshop jsJshop6 = new JSJshop("domain.pl", "problem.pl");
 JSListLogicalAtoms addList = jsJshop6.getAddList();
 assertNotNull("AddList should not be null", addList);
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsJshop7 = new JSJshop("domain.pl", "problem.pl");
 JSListLogicalAtoms deleteList = jsJshop7.getDeleteList();
 assertNotNull("DeleteList should not be null", deleteList);
 }
}
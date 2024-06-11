import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setup() {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 }

 @Test
 public void testParsingDomainAndProblemFiles() {
 assertNotNull(jsJshop.dom());
 assertNotNull(jsJshop.prob());
 }

 @Test
 public void testMainMethodWithValidArguments() {
 String[] args = {"domain.pl", "problem.pl", "one"};
 JSJshop expectedJsJshop = new JSJshop("domain.pl", "problem.pl");
 JSUtil.initParseTable(new StreamTokenizer(new StringReader(""))); // Initiate JSUtil
 JSJshop actualJsJshop = new JSJshop(args);
 assertEquals(expectedJsJshop.dom(), actualJsJshop.dom());
 assertEquals(expectedJsJshop.prob(), actualJsJshop.prob());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainMethodWithInvalidArguments() {
 String[] args = {"domain.pl", "problem.pl", "invalidArg"};
 JSJshop jsJshop = null;
 try {
 jsJshop = new JSJshop(args);
 }
 catch (IllegalArgumentException e) {
 // Expected exception
 }
 assertNull(jsJshop);
 }

 @Test
 public void testSolvingProblem() {
 assertEquals(1, jsJshop.probSet.size());
 JSListPairPlanTStateNodes solutions = jsJshop.dom.solveAll(jsJshop.prob, JSJshopVars.allPlans);
 assertTrue(solutions.size() > 0);
 JSPairPlanTSListNodes solution = solutions.elementAt(0);
 JSPlan plan = solution.planS().plan();
 assertFalse(plan.isFailure());
 }

 @Test
 public void testSettingFileWithValidParameters() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("file.pl", new JSTaskAtom("p(x)"));
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test(expected = NullPointerException.class)
 public void testSettingFileWithNullPredicate() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("file.pl", null);
 }

 @Test
 public void testGettingAddListAndDeleteList() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("file.pl", new JSTaskAtom("p(x)"));
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(addList);
 assertNotNull(deleteList);
 }
}
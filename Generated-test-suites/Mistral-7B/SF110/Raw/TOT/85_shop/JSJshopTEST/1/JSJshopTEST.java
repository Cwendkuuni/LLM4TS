package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Test
 public void testMain() {
 String domainFileName = "testDomain.shp";
 String problemFileName = "testProblem.shp";

 JSJshop.main(new String[]{"domainFileName", "problemFileName"});

 // Check if the correct message is printed when the wrong number of arguments is passed
 JSJshop.main(new String[]{"domainFileName", "problemFileName", "invalidArgument"});
 assert(JSJshopVars.flagExit);

 // Check if the correct message is printed when an invalid domain file is given
 JSJshop jsJshop1 = new JSJshop(domainFileName, problemFileName);
 jsJshop1.parserFile(domainFileName);
 assert(jsJshop1.dom == null);
 JSJshopVars.flagExit = false;

 JSJshop.main(new String[]{"domainFileName", "problemFileName"});

 // Check if the correct message is printed when an invalid problem file is given
 JSJshop jsJshop2 = new JSJshop(domainFileName, problemFileName);
 jsJshop2.parserFile(problemFileName);
 assert(jsJshop2.probSet.isEmpty());
 JSJshopVars.flagExit = false;

 JSJshop.main(new String[]{"domainFileName", "problemFileName"});
 }

 @Test
 public void testJSJshop() {
 String domainFileName = "testDomain.shp";
 String problemFileName = "testProblem.shp";

 JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);

 // Check if the correct message is printed when the parse table is not initialized
 JSUtil.initParseTable(null);
 JSJshop jsJshop1 = new JSJshop(domainFileName, problemFileName);
 assert(jsJshop1.probSet.isEmpty());

 // Check if the correct number of problems is loaded
 assertEquals(2, jsJshop.probSet.size());

 // Check if the correct plans are found for each problem
 JSListPairPlanTStateNodes plans = jsJshop.solveAll(jsJshop.probSet.elementAt(0), false);
 assertEquals(2, plans.size());
 JSPlan plan1 = plans.elementAt(0).planS().plan();
 JSPlan plan2 = plans.elementAt(1).planS().plan();

 // Check if the correct message is printed when no plans are found
 JSJshop jsJshop1 = new JSJshop(domainFileName, problemFileName, "noPlans");
 JSListPairPlanTStateNodes plans1 = jsJshop1.solveAll(jsJshop1.probSet.elementAt(0), false);
 assertEquals(0, plans1.size());
 }

 @Test
 public void testSetFile() {
 String domainFileName = "testDomain.shp";
 String problemFileName = "testProblem.shp";
 String file = "testFile.shp";
 JSTaskAtom predicate = new JSTaskAtom("testPredicate");

 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile(file, predicate);

 // Check if the correct number of list nodes are in the tree
 JSJshopNode tree = jsJshop.getTree();
 assertEquals(4, tree.listNodes().size());

 // Check if the correct plan is found
 JSPlan plan = jsJshop.sol();
 assertFalse(plan.isFailure());
 plan.printPlan();
 }

 @Test(expected = JSParserError.class)
 public void testParser() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser();
 }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 String domainFileName = "domain.shp";
 String problemFileName = "problem.shp";
 jsJshop = new JSJshop(domainFileName, problemFileName);
 }

 @Test
 public void testParsingFileWithValidArguments() {
 String expectedOutput = "Domain file parsed successfully\n" +
 "Problem file parsed successfully\n";
 String actualOutput = "";
 try {
 JSUtil.initStdOut();
 jsJshop.main(new String[]{"domain.shp", "problem.shp"});
 actualOutput = JSUtil.getStdOut();
 } finally {
 JSUtil.initStdOut(); // Reset stdout
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParsingFileWithInvalidArguments() {
 jsJshop = new JSJshop(new String[]{"domain.shp", "problem.shp", "three"});
 }

 @Test(expected = NumberFormatException.class)
 public void testParsingFileWithInvalidNumberArgument() {
 jsJshop = new JSJshop(new String[]{"domain.shp", "problem.shp", "invalid"});
 }

 @Test
 public void testSolvingProblemWithOnePlan() {
 // Assuming the test cases for parsing the file have passed
 // Set up the expectation for the number of plans
 JSJshopVars.allPlans = false;
 String expectedOutput = "Solving Problem :[Problem1]\n" +
 "0 plans found\n";
 String actualOutput = "";
 try {
 JSUtil.initStdOut();
 jsJshop.main(new String[]{"domain.shp", "problem1.shp"});
 actualOutput = JSUtil.getStdOut();
 } finally {
 JSUtil.initStdOut(); // Reset stdout
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSolvingProblemWithAllPlans() {
 // Assuming the test cases for parsing the file have passed
 // Set up the expectation for the number of plans
 JSJshopVars.allPlans = true;
 String expectedOutput = "Solving Problem :[Problem1]\n" +
 "2 plans found.\n" +
 "********* PLANS *******\n" +
 "Plan # 1\n" +
 "Action1(x1)\n" +
 "Action2(x2)\n" +
 "Plan # 2\n" +
 "Action3(x1)\n" +
 "Action4(x2)\n" +
 "********* list-tree FINAL SOLUTION*******\n";
 String actualOutput = "";
 try {
 JSUtil.initStdOut();
 jsJshop = new JSJshop(new String[]{"domain.shp", "problem1.shp", "all"});
 actualOutput = JSUtil.getStdOut();
 } finally {
 JSUtil.initStdOut(); // Reset stdout
 }
 assertEquals(expectedOutput, actualOutput);
 }

}
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 try {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize JSJshop instance");
 }
 }

 @After
 public void tearDown() {
 if (jsJshop != null) {
 jsJshop.dispose();
 jsJshop = null;
 }
 }

 @Test
 public void testMainWithValidArguments() {
 String[] args = new String[]{"domain.pl", "problem.pl", "one"};
 JSJshop.main(args);
 // Check if the correct message is printed
 JSUtil.assertEquals("Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
 "java JSJshop <domainDef-file-name> <problemDef-file-name> one", JSUtil.getOutput());
 }

 @Test
 public void testMainWithInvalidArguments() {
 String[] args = new String[]{"domain.pl", "problem.pl", "three", "four"};
 JSJshop.main(args);
 // Check if the correct error message is printed
 JSUtil.assertEquals("Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
 "java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
 "Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
 "java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
 "Invalid parameter\n", JSUtil.getOutput());
 }

 @Test
 public void testJSJshopWithValidFileNamesAndOnePlan() {
 String[] args = new String[]{"domain.pl", "problem.pl", "one"};
 try {
 JSJshop.main(args);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize JSJshop instance");
 }
 JSPlan plan = jsJshop.sol();
 // Check if a plan is found
 assertTrue(plan != null);
 // Check if the plan is not a failure
 assertFalse(plan.isFailure());
 // Print the plan
 System.out.println(plan.toString());
 }

 @Test
 public void testJSJshopWithValidFileNamesAndAllPlans() {
 String[] args = new String[]{"domain.pl", "problem.pl", "all"};
 try {
 JSJshop.main(args);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize JSJshop instance");
 }
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms delList = jsJshop.getDeleteList();
 // Check if more than one plan is found
 assertTrue(jsJshop.probSet.size() > 1);
 // Check if the addition list and deletion list are not empty
 assertTrue(!addList.isEmpty());
 assertTrue(!delList.isEmpty());
 // Print the plans
 for (int i = 0; i < jsJshop.probSet.size(); i++) {
 JSPlan plan = ((JSListPairPlanTStateNodes) jsJshop.getSolution()).elementAt(i).planS().plan();
 // Check if the plan is not a failure
 assertFalse(plan.isFailure());
 // Print the plan
 System.out.println("Plan " + (i + 1) + ": " + plan.toString());
 }
 }

 @Test
 public void testJSJshopWithInvalidFileNames() {
 String[] args = new String[]{"invalidDomain.pl", "problem.pl", "one"};
 try {
 JSJshop.main(args);
 } catch (Exception e) {
 // Check if the correct error message is printed
 assertTrue(e.getMessage().contains("Could not open file"));
 }
 }

}
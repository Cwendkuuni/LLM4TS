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
 fail("Failed to initialize JSJshop instance");
 }
 }

 @Test
 public void testParsingAndSolving() {
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());

 assertEquals(1, jsJshop.getAddList().size());
 assertEquals(0, jsJshop.getDeleteList().size());

 JSPlan sol = jsJshop.getSol();
 assertTrue(sol.isSatisfiable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments() {
 String[] args = {"domain.pl", "problem.pl", "three"};
 try {
 new JSJshop(args);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumberFormat() {
 String[] args = {"domain.pl", "problem.pl", "five"};
 try {
 JSJshopVars.flagLevel = "abc";
 new JSJshop(args);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // pass
 }
 }

 @Test
 public void testSolvingAllPlans() {
 jsJshop = new JSJshop("domain.pl", "problem.pl", "all");
 assertEquals(jsJshop.probSet.size(), jsJshop.solution.planS().planList().size());
 }

 @Test
 public void testSolvingOnePlan() {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 assertTrue(jsJshop.probSet.size() > 0);
 JSPlan sol = jsJshop.getSol();
 assertTrue(sol.isSatisfiable());
 }

 @Test(expected = RuntimeException.class)
 public void testDomainParsingError() {
 try {
 jsJshop = new JSJshop("invalidDomain.pl", "problem.pl");
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // pass
 }
 }

 @Test(expected = RuntimeException.class)
 public void testProblemParsingError() {
 try {
 jsJshop = new JSJshop("domain.pl", "invalidProblem.pl");
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // pass
 }
 }
}
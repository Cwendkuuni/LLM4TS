import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

 @Test
 public void testJSJshopWithValidArguments() {
 JSJshop jsShop = new JSJshop("domain.txt", "problem.txt");
 Assert.assertNotNull(jsShop.getTree());
 Assert.assertNotNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopWithInvalidArguments() {
 JSJshop jsShop = new JSJshop();
 Assert.assertNull(jsShop.getTree());
 Assert.assertNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopWithFileNotFoundException() {
 JSJshop jsShop = new JSJshop("invalidFile.txt", "problem.txt");
 JSUtil.flagExit = true; // Simulate file not found exception
 Assert.assertNull(jsShop.getTree());
 Assert.assertNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopWithInvalidDomainFileFormat() {
 JSJshop jsShop = new JSJshop("invalidDomainFile.txt", "problem.txt");
 JSUtil.flagExit = false; // Reset flagExit
 Assert.assertNull(jsShop.getTree());
 Assert.assertNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopWithInvalidProblemFileFormat() {
 JSJshop jsShop = new JSJshop("domain.txt", "invalidProblemFile.txt");
 JSUtil.flagExit = false; // Reset flagExit
 Assert.assertNull(jsShop.getTree());
 Assert.assertNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopWithInvalidNumberOfPlans() {
 JSJshop jsShop = new JSJshop("domain.txt", "problem.txt", "invalidNumber");
 JSUtil.flagExit = false; // Reset flagExit
 Assert.assertNull(jsShop.getTree());
 Assert.assertNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopWithSuccessfulSolution() {
 JSJshop jsShop = new JSJshop("domain.txt", "problem.txt");
 JSUtil.flagLevel = 1; // Set flagLevel to 1 for displaying one plan
 Assert.assertNotNull(jsShop.getTree());
 Assert.assertNotNull(jsShop.getSolution());
 }

 @Test
 public void testJSJshopWithAllPlans() {
 JSJshop jsShop = new JSJshop("domain.txt", "problem.txt");
 JSUtil.flagLevel = 10; // Set flagLevel to 10 for displaying all plans
 Assert.assertNotNull(jsShop.getTree());
 Assert.assertNotNull(jsShop.getSolution());
 }
}
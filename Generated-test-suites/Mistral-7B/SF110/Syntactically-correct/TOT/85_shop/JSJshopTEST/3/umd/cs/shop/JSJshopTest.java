import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSJshopVars;

public class JSJshopTest {

 @Test
 public void testMain() {
 String domainFileName = "testDomain.shp";
 String problemFileName = "testProblem.shp";
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
 assertEquals("Expected number of plans found: 1", 1, jsShop.getSolution().size());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testInvalidArguments() {
 JSJshop jsShop = new JSJshop("", "");
 }

 @Test
 public void testInvalidDomainFile() {
 String domainFileName = "invalidDomain.shp";
 JSJshop jsShop = new JSJshop(domainFileName, "testProblem.shp");
 assertNull(jsShop.getTree());
 assertNull(jsShop.getSolution());
 }

 @Test
 public void testInvalidProblemFile() {
 String domainFileName = "testDomain.shp";
 String problemFileName = "invalidProblem.shp";
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
 assertNull(jsShop.getTree());
 assertNull(jsShop.getSolution());
 }

 @Test
 public void testSolveAllOnePlan() {
 String domainFileName = "testDomainOnePlan.shp";
 String problemFileName = "testProblemOnePlan.shp";
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
 assertEquals("Expected number of plans found: 1", 1, jsShop.getSolution().size());
 JSPlan sol = jsShop.sol();
 assertTrue("Expected plan not to be failure", !sol.isFailure());
 }

 @Test
 public void testSolveAllMultiplePlans() {
 String domainFileName = "testDomainMultiplePlans.shp";
 String problemFileName = "testProblemMultiplePlans.shp";
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
 assertEquals("Expected number of plans found: 2", 2, jsShop.getSolution().size());
 }

 @Test
 public void testSolveAllEmptyPlans() {
 String domainFileName = "testDomainEmptyPlans.shp";
 String problemFileName = "testProblemEmptyPlans.shp";
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
 assertEquals("Expected number of plans found: 0", 0, jsShop.getSolution().size());
 }

 @Test
 public void testSolveAllInvalidNumberOfPlans() {
 String domainFileName = "testDomainInvalidNumberOfPlans.shp";
 String problemFileName = "testProblemInvalidNumberOfPlans.shp";
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName, "all");
 assertEquals("Expected number of plans found: 0", 0, jsShop.getSolution().size());
 }

 @Test
 public void testSetFileValid() {
 String domainFileName = "testDomainSetFile.shp";
 String problemFileName = "testProblemSetFile.shp";
 String filePath = "testFile.shp";
 JSTaskAtom pred = new JSTaskAtom("testPredicate");
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
 JSJshopNode tree = jsShop.setFile(filePath, pred);
 assertNotNull(tree);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileInvalidFile() {
 String domainFileName = "testDomainSetFile.shp";
 String problemFileName = "testProblemSetFile.shp";
 String filePath = "invalidFile.shp";
 JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
 jsShop.setFile(filePath, null);
 }

 @Test(expected = JSParserError.class)
 public void testParserError() {
 JSJshop jsShop = new JSJshop();
 jsShop.testParser();
 }

 @Test
 public void testBufferedReaderNullFile() {
 JSJshop jsShop = new JSJshop();
 BufferedReader br = jsShop.getBufferedReader(null, null);
 assertNull(br);
 }

 @Test
 public void testBufferedReaderNullApplet() {
 JSJshop jsShop = new JSJshop();
 BufferedReader br = jsShop.getBufferedReader("testFile.shp", null);
 assertNotNull(br);
 }

 @Test
 public void testBufferedReaderValidFile() {
 JSJshop jsShop = new JSJshop();
 BufferedReader br = jsShop.getBufferedReader("testFile.shp", null);
 assertNotNull(br);
 }

 @Test
 public void testGetAppletURLNullFile() {
 JApplet applet = new JFrame();
 BufferedReader br = jsShop.getBufferedReader("testFile.shp", applet);
 assertNull(jsShop.getAppletURL("testFile.shp", applet));
 }

 @Test
 public void testGetAppletURLValidFile() {
 JApplet applet = new JFrame();
 BufferedReader br = jsShop.getBufferedReader("testFile.shp", applet);
 URL url = jsShop.getAppletURL("testFile.shp", applet);
 assertNotNull(url);
 }
}
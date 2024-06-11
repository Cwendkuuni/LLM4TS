import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSListPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSPairPlanTSListNodes;
import umd.cs.shop.JSListLogicalAtoms;

public class JSJshopTest {

 private JSJshop jsShop;

 @Before
 public void setUp() {
 try {
 jsShop = new JSJshop("testDomain.pl", "testProblem.pl");
 } catch (Exception e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testMain() {
 String[] args = new String[]{"testDomain.pl", "testProblem.pl", "one"};
 JSJshop.main(args);
 assertTrue(jsShop.getTree() != null);
 }

 @Test
 public void testParserFile() {
 assertTrue(jsShop.parserFile("testParser.pl"));
 }

 @Test(expected = NullPointerException.class)
 public void testParserFileNull() {
 jsShop = new JSJshop();
 assertNull(jsShop.parserFile(null));
 }

 @Test(expected = NumberFormatException.class)
 public void testMainInvalidArgs() {
 String[] args = new String[]{"testDomain.pl", "testProblem.pl", "three"};
 JSJshop.main(args);
 fail();
 }

 @Test(expected = JSParserError.class)
 public void testProcessTokenError() {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("%%%"));
 jsShop.processToken(tokenizer);
 fail();
 }

 @Test
 public void testGetBufferedReader() {
 String filePath = "testFile.txt";
 BufferedReader br = jsShop.getBufferedReader(".", filePath);
 assertNotNull(br);
 try {
 String line = br.readLine();
 assertNotNull(line);
 br.close();
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetBufferedReaderNullFile() {
 BufferedReader br = jsShop.getBufferedReader(null, null);
 assertNull(br);
 }

 @Test
 public void testGetAppletURL() {
 JApplet applet = new JFrame();
 URL url = jsShop.getAppletURL("testFile.txt", applet);
 assertNotNull(url);
 }

 @Test(expected = MalformedURLException.class)
 public void testGetAppletURLInvalidURL() {
 JApplet applet = new JFrame();
 URL url = jsShop.getAppletURL(null, applet);
 assertNull(url);
 }

 @Test
 public void testSolvePlan() {
 JSListLogicalAtoms addList = jsShop.getAddList();
 JSListLogicalAtoms deleteList = jsShop.getDeleteList();
 JSPlan sol = jsShop.sol();
 JSJshopNode tree = jsShop.getTree();
 JSPairPlanTSListNodes solution = jsShop.getSolution();
 assertTrue(sol != null);
 assertTrue(solution != null);
 assertTrue(tree != null);
 assertTrue(addList != null);
 assertTrue(deleteList != null);
 }

 @Test
 public void testSolvePlanEmpty() {
 JSListPlanningProblem probSet = jsShop.probSet;
 JSPlanningProblem prob = jsShop.prob;
 JSPlan sol = new JSPlan();
 sol.assignFailure();
 JSJshopNode tree = null;
 JSPairPlanTSListNodes solution = new JSPairPlanTSListNodes(new JSPairPlanTState(sol, new JSTState()), new Vector<>());
 JSJshop jsShopEmpty = new JSJshop();
 jsShopEmpty.probSet = probSet;
 jsShopEmpty.tree = tree;
 jsShopEmpty.solution = solution;
 JSListLogicalAtoms addList = jsShopEmpty.getAddList();
 JSListLogicalAtoms deleteList = jsShopEmpty.getDeleteList();
 JSPlan solEmpty = jsShopEmpty.sol();
 assertTrue(solEmpty.isFailure());
 assertTrue(addList == null);
 assertTrue(deleteList == null);
 }

}
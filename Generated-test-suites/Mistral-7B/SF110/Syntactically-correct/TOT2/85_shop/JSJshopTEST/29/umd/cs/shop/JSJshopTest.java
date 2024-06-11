import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSListPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSPairPlanTSListNodes;
import umd.cs.shop.JSListLogicalAtoms;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSJshopTest {

 @Test
 public void testJSJshopConstructorWithDomainAndProblemFiles() {
 JSJshop jsJshop = new JSJshop("domain.txt", "problem.txt");
 Assert.assertNotNull(jsJshop.getTree());
 Assert.assertNotNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopConstructorWithInvalidArguments() {
 new JSJshop(new String[]{"", "domain.txt", "3"});
 }

 @Test
 public void testJSJshopParser() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser();
 }

 @Test
 public void testJSJshopSetFile() {
 JSJshop jsJshop = new JSJshop();
 JSListLogicalAtoms addList = new ArrayList<>();
 JSListLogicalAtoms deleteList = new ArrayList<>();
 JSJshopNode tree = new JSJshopNode(new JSJshopNode("root", addList, deleteList), new ArrayList<>());
 JSPlan sol = new JSPlan();
 JSPairPlanTSListNodes solution = new JSPairPlanTSListNodes(new JSPairPlanTState(sol, new JSTState()), addList, deleteList);
 jsJshop.setFile("file.txt", new JSTaskAtom("p"));
 Assert.assertEquals(tree, jsJshop.getTree());
 Assert.assertEquals(solution, jsJshop.getSolution());
 }

 @Test
 public void testJSJshopGetAddListAndDeleteList() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("file.txt", new JSTaskAtom("p"));
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 Assert.assertNotNull(addList);
 Assert.assertNotNull(deleteList);
 }

 @Test
 public void testJSJshopBufferedReader() throws IOException {
 File file = new File("test.txt");
 BufferedReader br = JSUtil.getBufferedReader(file.getAbsolutePath(), null);
 Assert.assertNotNull(br);
 br.close();
 }

 @Test
 public void testJSJshopGetAppletURL() {
 JFrame frame = new JFrame();
 frame.setVisible(true);
 JSApplet applet = new JSApplet();
 frame.getContentPane().add(applet);
 String file = "test.txt";
 URL url = JSUtil.getAppletURL(file, applet);
 Assert.assertNotNull(url);
 }

 @Test
 public void testJSJshopParserFile() {
 JSJshop jsJshop = new JSJshop();
 Assert.assertTrue(jsJshop.parserFile("domain.txt"));
 }

 @Test(expected = FileNotFoundException.class)
 public void testJSJshopParserFileNotFound() {
 JSJshop jsJshop = new JSJshop();
 Assert.assertFalse(jsJshop.parserFile("nonexistentfile.txt"));
 }

 @Test(expected = JSParserError.class)
 public void testJSJshopParserError() {
 JSJshop jsJshop = new JSJshop();
 Assert.assertFalse(jsJshop.parserFile("errorfile.txt"));
 }
}
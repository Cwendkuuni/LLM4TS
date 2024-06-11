package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;

public class JSJshopTest {

 @Test
 public void testJSJshop() {
 JSJshop jsJshop = new JSJshop();
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());

 JSJshop jsJshop1 = new JSJshop("domain.shp", "problem.shp");
 assertNotNull(jsJshop1.getTree());
 assertNotNull(jsJshop1.getSolution());
 }

 @Test
 public void testSetFile() {
 JSJshop jsJshop = new JSJshop();
 JSJshopNode tree = new JSJshopNode(null, null);
 JSPlan sol = new JSPlan();
 JSPairPlanTSListNodes solution = new JSPairPlanTSListNodes(new JSPairPlanTState(sol, new JSTState()), new Vector<>());

 JSJshop jsJshop1 = new JSJshop();
 jsJshop1.setFile("file.shp", new JSTaskAtom("p"));
 assertEquals(tree, jsJshop1.getTree());
 assertEquals(solution, jsJshop1.getSolution());
 }

 @Test
 public void testParser() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser(); // This method is not tested here as it's a debugging method
 }

 @Test(expected = JSParserError.class)
 public void testParserError() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.parserFile("error.shp"); // This method is not tested here as it's a debugging method
 }

 @Test
 public void testBufferedReader() {
 JSJshop jsJshop = new JSJshop();
 BufferedReader bufferedReader = jsJshop.getBufferedReader("dir", "file.shp");
 assertNotNull(bufferedReader);
 }

 @Test
 public void testGetAppletURL() {
 JSJshop jsJshop = new JSJshop();
 JApplet applet = new JFrame();
 URL url = jsJshop.getAppletURL("file.shp", applet);
 assertNotNull(url);
 }

 @Test
 public void testDomAndProb() {
 JSJshop jsJshop = new JSJshop("domain.shp", "problem.shp");
 JSPlanningDomain dom = jsJshop.dom();
 JSPlanningProblem prob = jsJshop.prob();
 assertEquals(dom, jsJshop.dom());
 assertEquals(prob, jsJshop.prob());
 }

 @Test
 public void testSol() {
 JSJshop jsJshop = new JSJshop("domain.shp", "problem.shp");
 JSPlan sol = jsJshop.sol();
 assertNull(sol);
 }

 @Test
 public void testTree() {
 JSJshop jsJshop = new JSJshop("domain.shp", "problem.shp");
 JSJshopNode tree = jsJshop.tree();
 assertNull(tree);
 }
}
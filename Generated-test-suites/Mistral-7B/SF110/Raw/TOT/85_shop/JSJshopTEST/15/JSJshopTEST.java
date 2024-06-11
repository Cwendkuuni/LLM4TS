import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 jsJshop = new JSJshop("domain.pl", "problem.pl");
 }

 @Test
 public void testMain() {
 String[] args = new String[]{"domain.pl", "problem.pl", "one"};
 JSJshop.main(args);
 assertEquals(JSUtil.SUCCESS, JSJshopVars.flagExit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgs() {
 String[] args = new String[]{"domain.pl", "problem.pl", "three"};
 JSJshop.main(args);
 assertFalse(JSJshopVars.flagExit);
 }

 @Test
 public void testSolveOneProblem() {
 JSJshop jsJshop = new JSJshop("domain.pl", "problem1.pl");
 JSPairPlanTSListNodes solution = jsJshop.solve();
 assertNotNull(solution);
 }

 @Test
 public void testSolveMultipleProblems() {
 JSJshop jsJshop = new JSJshop("domain.pl", "problem1.pl");
 jsJshop.solveOne();
 JSUtil.println();
 JSJshop jsJshop2 = new JSJshop("domain.pl", "problem2.pl");
 jsJshop2.solveOne();
 }

 @Test
 public void testSetFile() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("file.pl", new JSTaskAtom("p"));
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFile() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("nonexistentfile.pl", new JSTaskAtom("p"));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParser() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser();
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetBufferedReaderNullFile() {
 JSJshop jsJshop = new JSJshop();
 BufferedReader br = jsJshop.getBufferedReader(null, "file.pl");
 fail("Expected FileNotFoundException");
 }

 @Test
 public void testGetBufferedReader() {
 JSJshop jsJshop = new JSJshop();
 BufferedReader br = jsJshop.getBufferedReader(".", "file.pl");
 assertNotNull(br);
 }

 @Test
 public void testGetAppletURLNullFileApplet() {
 JSJshop jsJshop = new JSJshop();
 URL url = jsJshop.getAppletURL(null, null);
 assertNull(url);
 }

 @Test
 public void testGetAppletURL() {
 JSJshop jsJshop = new JSJshop();
 JApplet applet = new JFrame();
 URL url = jsJshop.getAppletURL("file.pl", applet);
 assertNotNull(url);
 }
}
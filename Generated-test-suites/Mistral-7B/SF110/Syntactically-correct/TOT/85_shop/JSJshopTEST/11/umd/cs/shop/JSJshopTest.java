import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Before
 public void setUp() {
 try {
 jsJshop = new JSJshop("testDomain.pl", "testProblem.pl");
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize JSJshop instance");
 }
 }

 @Test
 public void testParserFileSuccess() {
 assertTrue(jsJshop.parserFile("testFile.pl"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParserFileFailureWithInvalidFile() {
 assertFalse(jsJshop.parserFile("nonexistentFile.pl"));
 }

 @Test
 public void testGetBufferedReaderSuccess() {
 BufferedReader reader = jsJshop.getBufferedReader("testDir", "testFile.txt");
 assertNotNull(reader);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBufferedReaderFailure() {
 jsJshop.getBufferedReader(null, null);
 }

 @Test
 public void testGetAppletURLSuccess() {
 JApplet applet = new JFrame();
 URL url = jsJshop.getAppletURL("/testFile.txt", applet);
 assertNotNull(url);
 }

 @Test(expected = NullPointerException.class)
 public void testGetAppletURLFailure() {
 jsJshop.getAppletURL(null, null);
 }

 @Test
 public void testProcessTokenSuccess() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain mydomain"));
 jsJshop.processToken(tokenizer);
 assertEquals("mydomain", jsJshop.dom().getName());
 }

 @Test(expected = JSParserError.class)
 public void testProcessTokenFailure() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("invalid syntax"));
 jsJshop.processToken(tokenizer);
 fail("Should have thrown JSParserError");
 }

 @Test
 public void testMainSuccess() {
 String[] args = new String[]{"testDomain.pl", "testProblem.pl", "one"};
 JSJshop.main(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainFailureWithInvalidNumberOfArguments() {
 String[] args = new String[]{"testDomain.pl", "testProblem.pl"};
 jsJshop.main(args);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = NumberFormatException.class)
 public void testMainFailureWithInvalidArgument() {
 String[] args = new String[]{"testDomain.pl", "testProblem.pl", "invalidArgument"};
 jsJshop.main(args);
 fail("Should have thrown NumberFormatException");
 }

 @Test
 public void testSolveProblemSuccess() {
 JSListPairPlanTStateNodes solutions = jsJshop.solveAll(jsJshop.prob(), false);
 assertFalse(solutions.isEmpty());
 }

 @Test
 public void testSolveProblemFailure() {
 JSListPairPlanTStateNodes solutions = jsJshop.solveAll(jsJshop.prob(), true);
 assertTrue(solutions.isEmpty());
 }

 @Test
 public void testGetAddListSuccess() {
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteListSuccess() {
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testSetFileSuccess() {
 JSJshopNode tree = jsJshop.setFile("testFile.pl", new JSTaskAtom("p"));
 assertNotNull(tree);
 }

 @Test(expected = NullPointerException.class)
 public void testSetFileFailure() {
 jsJshop.setFile(null, null);
 }
}
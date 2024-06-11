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
 fail("Failed to initialize JSJshop");
 }
 }

 @Test
 public void testReadingFiles() {
 assertTrue(jsJshop.getTree() != null);
 assertTrue(jsJshop.getSolution() != null);
 }

 @Test
 public void testInvalidArguments() {
 String[] args = {"domain.pl", "problem.pl", "5"};
 try {
 new JSJshop(args);
 fail("Expected IllegalArgumentException with wrong number of arguments");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParsingFileFailure() {
 JSJshop jsJshop2 = null;
 try {
 jsJshop2 = new JSJshop("invalidFile.pl");
 fail("Expected FileNotFoundException or JSParserError with invalid file");
 } catch (FileNotFoundException | JSParserError e) {
 // Expected
 }
 assertTrue(jsJshop2 == null);
 }

 @Test
 public void testPlanningSuccess() {
 assertEquals(1, jsJshop.getAddList().size());
 assertEquals(1, jsJshop.getDeleteList().size());
 }

 @Test
 public void testPlanningFailure() {
 JSJshop jsJshop3 = null;
 try {
 jsJshop3 = new JSJshop("domain.pl", "problem_with_no_solution.pl");
 } catch (Exception e) {
 // Expected
 }
 assertTrue(jsJshop3 == null);
 }

 @Test
 public void testParser() {
 JSJshop jsJshop4 = null;
 try {
 jsJshop4 = new JSJshop();
 jsJshop4.testParser();
 fail("Expected NoSuchElementException or other exceptions with parser test");
 } catch (NoSuchElementException | IOException | JSParserError e) {
 // Expected
 }
 assertTrue(jsJshop4 == null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLevelArgument() {
 String[] args = {"domain.pl", "problem.pl", "abc"};
 new JSJshop(args);
 }
}
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit 4 test file for CommandLine class.
 */
public class CommandLineTest extends TestCase {

 private CommandLine commandLine;
 private Options options;

 /**
 * Set up the test environment.
 *
 * @throws Exception
 */
 @Before
 public void setUp() throws Exception {
 options = new Options();
 options.addOption("a", false, false, "Option A");
 options.addOption("b", true, "argB", "Option B");
 commandLine = new CommandLine();
 }

 /**
 * Test hasOption method with valid options.
 */
 public void testHasOption() {
 Assert.assertTrue("Option A should be present.", commandLine.hasOption("a"));
 Assert.assertTrue("Option B should be present.", commandLine.hasOption("b"));
 Assert.assertFalse("Option C should not be present.", commandLine.hasOption("c"));
 }

 /**
 * Test getOptionValue method with valid options.
 */
 public void testGetOptionValue() {
 commandLine.parse(options, new String[]{"-a", "-b", "argValue"});
 Assert.assertNull("Option A should not have a value.", commandLine.getOptionValue("a"));
 Assert.assertEquals("Option B should have a value.", "argValue", commandLine.getOptionValue("b"));
 }

 /**
 * Test getOptionValues method with valid options.
 */
 public void testGetOptionValues() {
 commandLine.parse(options, new String[]{"-b", "val1", "-b", "val2"});
 String[] values = commandLine.getOptionValues("b");
 Assert.assertNotNull("Option B should have a value.", values);
 Assert.assertEquals("Option B should have two values.", 2, values.length);
 Assert.assertEquals("First value of Option B should be val1.", "val1", values[0]);
 Assert.assertEquals("Second value of Option B should be val2.", "val2", values[1]);
 }

 /**
 * Test getOptionValue method with invalid option.
 */
 public void testGetOptionValueInvalidOption() {
 commandLine.parse(options, new String[]{"-c", "argValue"});
 Assert.assertNull("Invalid option should not have a value.", commandLine.getOptionValue("c"));
 }

 /**
 * Test getArgs method.
 */
 public void testGetArgs() {
 commandLine.parse(options, new String[]{"-a", "-b", "argValue", "arg1", "arg2"});
 String[] args = commandLine.getArgs();
 Assert.assertNotNull("Args should not be null.", args);
 Assert.assertEquals("Number of args should be 3.", 3, args.length);
 Assert.assertEquals("First arg should be arg1.", "arg1", args[0]);
 Assert.assertEquals("Second arg should be arg2.", "arg2", args[1]);
 Assert.assertEquals("Last arg should be empty.", "", args[2]);
 }

 /**
 * Test parsing with unknown option.
 */
 public void testParseUnknownOption() {
 commandLine.parse(options, new String[]{"-x", "argValue"});
 Assert.assertEquals("Unknown option should not be added.", 0, commandLine.getOptions().length);
 Assert.assertEquals("Number of args should be 1.", 1, commandLine.getArgList().size());
 Assert.assertEquals("Unknown option should be in the args.", "-x", commandLine.getArgList().get(0));
 }

 /**
 * Test parsing with multiple unknown options.
 */
 public void testParseMultipleUnknownOptions() {
 commandLine.parse(options, new String[]{"-x", "-y", "argValue"});
 Assert.assertEquals("Number of options should be 1.", 1, commandLine.getOptions().length);
 Assert.assertEquals("Number of args should be 2.", 2, commandLine.getArgList().size());
 Assert.assertEquals("x should be in the args.", "-x", commandLine.getArgList().get(0));
 Assert.assertEquals("y should be in the args.", "-y", commandLine.getArgList().get(1));
 }

 /**
 * Test parsing with valid and invalid options.
 */
 public void testParseValidAndInvalidOptions() {
 commandLine.parse(options, new String[]{"-a", "-b", "argValue1", "-c", "argValue2"});
 Assert.assertEquals("Number of options should be 2.", 2, commandLine.getOptions().length);
 Assert.assertEquals("Number of args should be 2.", 2, commandLine.getArgList().size());
 String[] args = commandLine.getArgs();
 Assert.assertEquals("First arg should be argValue1.", "argValue1", args[0]);
 Assert.assertEquals("Second arg should be argValue2.", "argValue2", args[1]);
 }

 /**
 * Test parsing with multiple values for the same option.
 */
 public void testParseMultipleValuesForSameOption() {
 options.addOption("c", true, "argC", "Option C with multiple values");
 commandLine.parse(options, new String[]{"-c", "val1", "-c", "val2", "-c", "val3"});
 String[] values = commandLine.getOptionValues("c");
 Assert.assertNotNull("Option C should have a value.", values);
 Assert.assertEquals("Option C should have 3 values.", 3, values.length);
 Assert.assertEquals("First value of Option C should be val1.", "val1", values[0]);
 Assert.assertEquals("Second value of Option C should be val2.", "val2", values[1]);
 Assert.assertEquals("Third value of Option C should be val3.", "val3", values[2]);
 }

 /**
 * Tear down the test environment.
 */
 @After
 public void tearDown() {
 commandLine = null;
 options = null;
 }

 /**
 * Main method to run the tests.
 *
 * @param args command line arguments
 */
 public static void main(String[] args) {
 TestSuite suite = new TestSuite(CommandLineTest.class);
 junit.textui.TestRunner.run(suite);
 }
}
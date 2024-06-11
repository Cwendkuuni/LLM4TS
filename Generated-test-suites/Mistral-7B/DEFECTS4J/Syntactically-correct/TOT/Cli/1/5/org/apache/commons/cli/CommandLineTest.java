import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link CommandLine}.
 */
public class CommandLineTest {

 private CommandLine commandLine;
 private CommandLineParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C with argument");
 parser = new DefaultParser();
 }

 /**
 * Test hasOption method with valid option.
 */
 @Test
 public void testHasOption() {
 commandLine = parser.parse(options.parse(new String[]{"--a"}));
 assertTrue(commandLine.hasOption("a"));
 }

 /**
 * Test hasOption method with invalid option.
 */
 @Test
 public void testHasOptionFalse() {
 commandLine = parser.parse(options.parse(new String[]{"--x"}));
 assertFalse(commandLine.hasOption("a"));
 }

 /**
 * Test getOptionValue method with valid option without argument.
 */
 @Test
 public void testGetOptionValueWithoutArgument() {
 commandLine = parser.parse(options.parse(new String[]{"--a"}));
 assertTrue(commandLine.hasOption("a"));
 assertEquals("true", commandLine.getOptionValue("a"));
 }

 /**
 * Test getOptionValue method with valid option with argument.
 */
 @Test
 public void testGetOptionValueWithArgument() {
 commandLine = parser.parse(options.parse(new String[]{"--c", "abc"}));
 assertTrue(commandLine.hasOption("c"));
 assertEquals("abc", commandLine.getOptionValue("c"));
 }

 /**
 * Test getOptionValue method with invalid option.
 */
 @Test
 public void testGetOptionValueInvalidOption() {
 commandLine = parser.parse(options.parse(new String[]{}));
 assertNull(commandLine.getOptionValue("x"));
 }

 /**
 * Test getOptionValues method with valid option without argument.
 */
 @Test
 public void testGetOptionValuesWithoutArgument() {
 commandLine = parser.parse(options.parse(new String[]{"--a"}));
 assertNull(commandLine.getOptionValues("a"));
 }

 /**
 * Test getOptionValues method with valid option with argument.
 */
 @Test
 public void testGetOptionValuesWithArgument() {
 commandLine = parser.parse(options.parse(new String[]{"--c", "abc", "--a"}));
 assertArrayEquals(new String[]{"abc"}, commandLine.getOptionValues("c"));
 }

 /**
 * Test getOptionValues method with invalid option.
 */
 @Test
 public void testGetOptionValuesInvalidOption() {
 commandLine = parser.parse(options.parse(new String[]{"--x"}));
 assertNull(commandLine.getOptionValues("x"));
 }

 /**
 * Test getArgs method.
 */
 @Test
 public void testGetArgs() {
 commandLine = parser.parse(options.parse(new String[]{"--a", "arg1", "--b", "arg2"}));
 String[] args = commandLine.getArgs();
 assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();

 // add options
 options.addOption("a", "alpha", true, "an option with a short name and a boolean value");
 options.addOption("b", "beta", false, "an option with a short name and no value");
 options.addOption("c", null, false, "an option with a short name and no value or description");
 options.addOption("d", null, true, "an option with a short name and a value by position");
 options.addOption("e", "echo", false, "an option with a short name and a value by name");

 // add long options
 options.addOption("foo", "full-option", false, "a long option with a boolean value and no value");
 options.addOption("bar", null, true, "a long option with a value by position");
 options.addOption("baz", "base", false, "a long option with no value or description");
 options.addOption("qux", "quiet", false, "a long option with a value by name");

 return options;
 }

 @Test
 public void testParsing_AllOptionsProvided() throws ParseException {
 String[] args = {"-a", "--alpha", "-b", "-c", "--d", "valueD", "-e", "--quiet"};
 Options options = createTestOptions();
 CommandLine parsedArgs = options.parse(args);

 // test options with values
 assertTrue(parsedArgs.hasOption("a"));
 assertTrue(parsedArgs.hasOption("d"));
 assertEquals("valueD", parsedArgs.getOptionValue("d"));

 // test options without values
 assertTrue(parsedArgs.hasOption("b"));
 assertFalse(parsedArgs.hasOption("c"));

 // test long options
 assertTrue(parsedArgs.hasOption("foo"));
 assertFalse(parsedArgs.hasOption("bar"));
 assertFalse(parsedArgs.hasOption("baz"));
 assertTrue(parsedArgs.hasOption("qux"));
 }

 @Test(expected = ParseException.class)
 public void testParsing_MissingRequiredOption() throws ParseException {
 String[] args = {"-a", "--alpha", "-b"};
 Options options = createTestOptions();
 options.addOptionGroup(new OptionGroup("group1", "group options", true, "group options"));
 options.addOption("g", "group", false, "an option in the group", new Option("g", "group", true, "an option in the group", new OptionGroup("group1", "group options", true, "group options")));
 options.addRequiredOption("r", "required", true, "a required option");
 CommandLine parsedArgs = options.parse(args);
 fail("Expected ParseException, but no exception was thrown.");
 }
}
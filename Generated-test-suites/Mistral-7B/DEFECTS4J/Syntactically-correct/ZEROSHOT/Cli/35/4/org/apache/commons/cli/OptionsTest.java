import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @After
 public void tearDown() {
 options = null;
 }

 @Test
 public void testAddOption() {
 Option opt = new Option("a", "optionA", false, "This is option A");
 options.addOption(opt);
 assertTrue(options.hasOption("a"));
 assertEquals("This is option A", options.getOption("a").getDescription());
 }

 @Test
 public void testAddOptionWithArgument() {
 Option opt = new Option("b", "optionB", true, "This is option B");
 options.addOption(opt);
 assertTrue(options.hasOption("b"));
 assertEquals("This is option B", options.getOption("b").getDescription());
 }

 @Test
 public void testAddOptionWithLongName() {
 Option opt = new Option("c", "optionC", false, "This is option C");
 options.addOption(opt);
 assertTrue(options.hasOption("c"));
 assertTrue(options.hasOption("--optionC"));
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group1", "Group 1");
 Option opt1 = new Option("g1a", "group1OptionA", true, "This is group1 option A");
 Option opt2 = new Option("g1b", "group1OptionB", false, "This is group1 option B");
 group.addOption(opt1);
 group.addOption(opt2);
 options.addOptionGroup(group);

 assertTrue(options.hasOptionGroup("group1"));
 assertTrue(options.hasOption("g1a"));
 assertTrue(options.hasOption("g1b"));
 }

 @Test
 public void testGetRequiredOptions() {
 Option opt1 = new Option("r1", "requiredOption1", true, "This is required option 1");
 Option opt2 = new Option("r2", "requiredOption2", true, "This is required option 2");
 OptionGroup group = new OptionGroup("group1", "Group 1");
 group.addOption(opt1);
 group.addOption(opt2);
 options.addOptionGroup(group);
 options.addOption(new Option("o", "option", false, "This is an option"));

 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(2, requiredOptions.size());
 assertTrue(requiredOptions.contains(opt1));
 assertTrue(requiredOptions.contains(opt2));
 }

 @Test
 public void testParse() {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "argA", "--optionB", "argB"};

 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("argA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("optionB"));
 assertEquals("argB", cmd.getOptionValue("optionB"));
 } catch (ParseException e) {
 fail("Expected no ParseException");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidOption() {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--invalidOption"};

 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 assertEquals("[ Options: [ short {a=}, {} ] [ long {}--optionC= ]]", options.toString());
 }

}
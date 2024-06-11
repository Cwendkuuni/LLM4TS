import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] TEST_ARGUMENTS = {"-a", "--optionB", "--optionC=value", "arg1", "--optionD=value2"};

 @Test
 public void testParseNormalCase() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", true, "Option C");
 options.addOption("d", true, "Option D");

 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 Assert.assertEquals("Option A should be set", true, cmd.hasOption("a"));
 Assert.assertEquals("Option B should be set", true, cmd.hasOption("b"));
 Assert.assertEquals("Option C value should be set", "value", cmd.getOptionValue("c"));
 Assert.assertEquals("Option D value should be set", "value2", cmd.getOptionValue("d"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");

 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);
 }

 @Test
 public void testMissingArgForOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B", true, "B argument");

 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 Assert.assertEquals("Option A should be set", true, cmd.hasOption("a"));
 Assert.assertNull("B argument should not be set", cmd.getOptionValue("b"));
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");

 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", true, "Option C");
 options.addOptions(new Options() {{
 setOptionals(false);
 addOption("p", true, "Property", true, "Property value");
 }});

 CommandLine cmd = parser.parse(options, new String[]{"--property=value3"}, false);

 Options parsedOptions = cmd.getOptions();
 Assert.assertEquals("Property should be present", 1, parsedOptions.getOptions().size());
 Assert.assertEquals("Property value should be set", "value3", parsedOptions.getOptions().get(0).getValue());
 }

 @Test
 public void testParseWithOptionGroup() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 group.setRequired(false);
 group.addOption("a", true, "Option A");
 group.addOption("b", true, "Option B");
 options.addOptionGroup(group);
 options.addOption("c", true, "Option C");

 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 Assert.assertEquals("Option A should be present", true, cmd.hasOption("a"));
 Assert.assertEquals("Option B should be present", true, cmd.hasOption("b"));
 Assert.assertEquals("Option C should be present", true, cmd.hasOption("c"));
 }

 @Test
 public void testParseWithRequiredOptionGroup() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 group.setRequired(true);
 group.addOption("a", true, "Option A");
 group.addOption("b", true, "Option B");
 options.addOptionGroup(group);
 options.addOption("c", true, "Option C");

 try {
 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);
 Assert.fail("Required group should fail to parse");
 } catch (MissingOptionException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", true, "Option C");

 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, true);

 Assert.assertEquals("Option A should be set", true, cmd.hasOption("a"));
 Assert.assertEquals("Option B should be set", true, cmd.hasOption("b"));
 Assert.assertEquals("Option C should not be set", false, cmd.hasOption("c"));
 Assert.assertEquals("Remaining arguments should be set as args", Arrays.asList("arg1", "--optionD=value2"), cmd.getArgs());
 }
}
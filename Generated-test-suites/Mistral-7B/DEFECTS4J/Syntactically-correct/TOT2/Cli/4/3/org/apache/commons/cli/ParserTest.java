import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGS = new String[]{"-a", "--optionA", "1",
         "--optionB", "2", "--optionC=value", "-d", "--doubleDash", "--",
         "argument1", "argument2"};

 @Test
 public void testParseWithRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", "optionA", false, "A value is required");
 options.addRequiredOption("b", "optionB", false, "B value is required");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, true);

 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("b"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", "optionA", false, "A value is required");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, true);

 Assert.fail("ParseException should have been thrown");
 }

 @Test
 public void testParseWithOptionalOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "An optional option A");
 options.addOption("b", "optionB", true, "An optional option B");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, false);

 Assert.assertFalse(cmd.hasOption("a"));
 Assert.assertFalse(cmd.hasOption("b"));
 }

 @Test
 public void testParseWithValueOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "An option with a value");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, false);

 Assert.assertEquals("1", cmd.getOptionValue("a"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "An option with a value");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, false);

 Assert.fail("Invalid option should have been rejected");
 }

 @Test
 public void testParseWithDoubleDash() throws ParseException {
 Options options = new Options();
 options.addOption("doubleDash", "double-dash", false, "Double dash option");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, false);

 Assert.assertTrue(cmd.hasOption("doubleDash"));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "An option with a value");

 Properties properties = new Properties();
 properties.setProperty("optionA", "value");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, null, properties, false);

 Assert.assertEquals("value", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseWithGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 group.setRequired(true);
 group.addOption("a", "optionA", false, "Option A");
 group.addOption("b", "optionB", false, "Option B");
 options.addOptionGroup(group);

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, true);

 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("b"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 group.setRequired(true);
 group.addOption("a", "optionA", false, "Option A");
 options.addOptionGroup(group);

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS, true);

 Assert.fail("Invalid group should have been rejected");
 }
}
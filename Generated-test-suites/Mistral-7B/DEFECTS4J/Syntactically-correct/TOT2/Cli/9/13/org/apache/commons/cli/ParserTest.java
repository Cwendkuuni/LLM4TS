import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

 private static final String[] ARGS_1 = {"-a", "1", "--optionB", "2"};
 private static final String[] ARGS_2 = {"-a", "1", "--optionB", "3", "--unrecognizedOption"};
 private static final String[] ARGS_3 = {"--optionA", "valueA", "--optionB", "valueB"};
 private static final String[] ARGS_4 = {"--optionA", "--optionB"};

 @Test
 public void testParseOptionsWithArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", true, "This is an option with an argument");
 options.addOption("b", "anotherOption", true, "This is another option with an argument");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_1);

 assertEquals("1", cmd.getOptionValue("a"));
 assertEquals("2", cmd.getOptionValue("b"));
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsWithMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", "anOption", true, "This is a required option");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_2);
 }

 @Test
 public void testParseOptionsWithoutArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", true, "This is an option with an argument");
 options.addOption("b", "anotherOption", true, "This is another option with an argument");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_3);

 assertEquals("valueA", cmd.getOptionValue("a"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", true, "This is an option with an argument");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_4);
 }

 @Test
 public void testParseOptionsWithLongOptions() throws ParseException {
 Options options = new Options();
 options.addLongOption("longOptionA", "longOptionDescription", true, "This is a long option with an argument");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_1);

 assertEquals("1", cmd.getOptionValue("longOptionA"));
 }

 @Test
 public void testParseOptionsWithGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("groupName", "This is a group");
 group.addOption("a", "anOption", true, "This is an option in the group");
 group.addOption("b", "anotherOption", true, "This is another option in the group");
 options.addOptionGroup(group);
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_3);

 assertEquals("valueA", cmd.getOptionValue("a"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 }

 @Test
 public void testParseOptionsWithMixedLongAndShortOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", true, "This is an option with an argument");
 options.addLongOption("longOptionA", "longOptionDescription", true, "This is a long option with an argument");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_1);

 assertEquals("1", cmd.getOptionValue("a"));
 assertEquals("1", cmd.getOptionValue("longOptionA"));
 }

 @Test
 public void testParseOptionsWithMultipleValues() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", true, "This is an option with multiple values");
 options.addOption("b", "anotherOption", true, "This is an option with multiple values");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_1);

 assertEquals(2, cmd.getValuesForOption("a").size());
 assertEquals("2", cmd.getValuesForOption("b").get(0));
 }

 @Test
 public void testParseOptionsWithValueSeparatedByEquals() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", true, "This is an option with an argument separated by equals");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"-a", "key=value"});
 assertEquals("value", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseOptionsWithBooleanValue() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", false, "This is an option with a boolean value");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_1);
 assertEquals(true, cmd.hasOption("a"));
 }

 @Test
 public void testParseOptionsWithNoArg() throws ParseException {
 Options options = new Options();
 options.addOption("a", "anOption", false, "This is an option with no argument");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_1);
 assertEquals(true, cmd.hasOption("a"));
 }
}
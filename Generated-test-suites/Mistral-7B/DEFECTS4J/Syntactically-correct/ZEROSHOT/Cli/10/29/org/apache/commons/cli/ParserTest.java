import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParsingWithNoOptionsAndNoArguments() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 CommandLine commandLine = parser.parse(options, arguments);
 assertEquals(0, commandLine.getArgs().size());
 }

 @Test
 public void testParsingWithSingleOptionAndNoArguments() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Option value");
 options.addOption(option);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"-o", "value"};
 CommandLine commandLine = parser.parse(options, arguments);
 assertEquals(1, commandLine.getArgs().size());
 assertEquals("value", commandLine.getArgs().get(0));
 assertTrue(commandLine.hasOption("option"));
 assertSame(option, commandLine.getOption("option"));
 }

 @Test
 public void testParsingWithSingleOptionAndOneArgument() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Option value");
 options.addOption(option);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"-o", "value1", "arg"};
 CommandLine commandLine = parser.parse(options, arguments);
 assertEquals(2, commandLine.getArgs().size());
 assertEquals("arg", commandLine.getArgs().get(1));
 assertEquals("value1", commandLine.getOptionValue("option"));
 }

 @Test(expected = ParseException.class)
 public void testParsingWithUnrecognizedOption() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"-x", "value"};
 //unrecognized option 'x'
 parser.parse(options, arguments);
 }

 @Test
 public void testParsingWithRequiredOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Option value")
 .isRequired(true);
 options.addOption(option);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{};
 //missing required option 'option'
 assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
 String[] argumentsWithOption = new String[]{"-o", "value"};
 CommandLine commandLine = parser.parse(options, argumentsWithOption);
 assertEquals(1, commandLine.getArgs().size());
 assertEquals("value", commandLine.getArgs().get(0));
 assertTrue(commandLine.hasOption("option"));
 assertSame(option, commandLine.getOption("option"));
 }

 @Test
 public void testParsingWithMultipleOptions() throws ParseException {
 Options options = new Options();
 Option option1 = new Option("o1", "option1", true, "Option1 value");
 Option option2 = new Option("o2", "option2", true, "Option2 value");
 options.addOption(option1);
 options.addOption(option2);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"-o1", "value1", "-o2", "value2"};
 CommandLine commandLine = parser.parse(options, arguments);
 assertEquals(2, commandLine.getArgs().size());
 assertEquals("value2", commandLine.getArgs().get(1));
 assertTrue(commandLine.hasOption("option1"));
 assertSame(option1, commandLine.getOption("option1"));
 assertTrue(commandLine.hasOption("option2"));
 assertSame(option2, commandLine.getOption("option2"));
 }

 @Test
 public void testParsingWithOptionGroup() throws ParseException {
 Options options = new Options();
 Option option1 = new Option("g", "group")
 .setRequired(false)
 .setLongOpt("mygroup")
 .setValueSeparator(' ')
 .addOption(new Option("o1", true, "Option1 value"));
 Option option2 = new Option("g", "group")
 .setRequired(false)
 .setLongOpt("mygroup")
 .addOption(new Option("o2", true, "Option2 value"));
 Option optionGroup = new OptionGroup("group-name", "Group")
 .addOption(option1)
 .addOption(option2);
 options.addOption(optionGroup);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"--group", "o1", "value1", "--group", "o2", "value2"};
 CommandLine commandLine = parser.parse(options, arguments);
 assertEquals(2, commandLine.getArgs().size());
 assertEquals("value2", commandLine.getArgs().get(1));
 assertTrue(commandLine.hasOption("o1"));
 assertSame(option1, commandLine.getOption("o1"));
 assertTrue(commandLine.hasOption("o2"));
 assertSame(option2, commandLine.getOption("o2"));
 assertSame(optionGroup, commandLine.getOptionGroup("group-name"));
 }

 @Test
 public void testParsingWithProperties() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Option value");
 options.addOption(option);
 Parser parser = new DefaultParser();
 Properties properties = new Properties();
 properties.setProperty("option", "value");
 CommandLine commandLine = parser.parse(options, null, properties);
 assertEquals(1, commandLine.getArgs().size());
 assertEquals("value", commandLine.getArgs().get(0));
 assertTrue(commandLine.hasOption("option"));
 assertSame(option, commandLine.getOption("option"));
 }

 @Test
 public void testParsingWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Option value");
 options.addOption(option);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"-o", "value1", "-n", "nonOption", "arg"};
 CommandLine commandLine = parser.parse(options, arguments, true);
 assertEquals(3, commandLine.getArgs().size());
 assertEquals("arg", commandLine.getArgs().get(2));
 assertFalse(commandLine.hasOption("nonOption"));
 }

 @Test
 public void testParsingWithLongOptions() throws ParseException {
 Options options = new Options();
 Option option = new Option("longOption", "long option", true, "Option value")
 .setLongOpt("mylongoption");
 options.addOption(option);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"--mylongoption", "value"};
 CommandLine commandLine = parser.parse(options, arguments);
 assertEquals(1, commandLine.getArgs().size());
 assertEquals("value", commandLine.getArgs().get(0));
 assertTrue(commandLine.hasOption("longOption"));
 assertSame(option, commandLine.getOption("longOption"));
 }

 @Test
 public void testParsingWithLongOptionGroup() throws ParseException {
 Options options = new Options();
 Option option1 = new Option("longOption1", "long option group1", true, "Option value1")
 .setLongOpt("mylongoptiongroup1");
 Option option2 = new Option("longOption2", "long option group2", true, "Option value2")
 .setLongOpt("mylongoptiongroup2");
 Option optionGroup = new OptionGroup("longOptionGroup", "Long Option Group")
 .setLongOpt("mylongoptiongroup")
 .addOption(option1)
 .addOption(option2);
 options.addOption(optionGroup);
 Parser parser = new DefaultParser();
 String[] arguments = new String[]{"--mylongoptiongroup", "--mylongoptiongroup1", "value1", "--mylongoptiongroup2", "value2"};
 CommandLine commandLine = parser.parse(options, arguments);
 assertEquals(2, commandLine.getArgs().size());
 assertEquals("value2", commandLine.getArgs().get(1));
 assertTrue(commandLine.hasOption("longOption1"));
 assertSame(option1, commandLine.getOption("longOption1"));
 assertTrue(commandLine.hasOption("longOption2"));
 assertSame(option2, commandLine.getOption("longOption2"));
 assertSame(optionGroup, commandLine.getOptionGroup("longOptionGroup"));
 }
}
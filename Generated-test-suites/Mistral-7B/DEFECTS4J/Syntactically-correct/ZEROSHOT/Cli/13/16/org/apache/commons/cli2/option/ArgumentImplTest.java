import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 @Test
 public void testArgumentImpl() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 3,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1", "value2"),
 1);

 assertEquals("arg", argument.getPreferredName());
 assertEquals("An example argument", argument.getDescription());
 assertEquals(1, argument.getMinimum());
 assertEquals(3, argument.getMaximum());
 assertEquals(':', argument.getInitialSeparator());
 assertEquals(';', argument.getSubsequentSeparator());
 assertEquals(true, argument.isRequired());
 assertEquals(3, argument.getDefaultValues().size());
 assertEquals("value1", argument.getDefaultValues().get(0));
 assertEquals("value2", argument.getDefaultValues().get(1));

 ArgumentImpl argumentOptional = new ArgumentImpl(
 "arg",
 "An example optional argument",
 0,
 3,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1", "value2"),
 1);

 assertEquals("arg", argumentOptional.getPreferredName());
 assertEquals("An example optional argument", argumentOptional.getDescription());
 assertEquals(0, argumentOptional.getMinimum());
 assertEquals(3, argumentOptional.getMaximum());
 assertEquals(':', argumentOptional.getInitialSeparator());
 assertEquals(';', argumentOptional.getSubsequentSeparator());
 assertEquals(false, argumentOptional.isRequired());
 assertEquals(3, argumentOptional.getDefaultValues().size());
 assertEquals("value1", argumentOptional.getDefaultValues().get(0));
 assertEquals("value2", argumentOptional.getDefaultValues().get(1));
 }

 @Test
 public void testProcessValues() throws Exception {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 3,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1", "value2"),
 1);

 CommandLine commandLine = new CommandLine();
 List<String> args = Arrays.asList("--arg", "value1", "value2", "value3");
 ArgumentParser parser = new ArgumentParser();
 parser.parseArgument(argument, commandLine, args);
 assertEquals("value1", commandLine.getValue(argument, 0));
 assertEquals("value2", commandLine.getValue(argument, 1));
 assertEquals("value3", commandLine.getValue(argument, 2));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesInvalid() throws Exception {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 1,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1"),
 1);

 CommandLine commandLine = new CommandLine();
 List<String> args = Arrays.asList("--arg", "value1");
 ArgumentParser parser = new ArgumentParser();
 parser.parseArgument(argument, commandLine, args);
 fail();
 }

 @Test
 public void testProcessValuesConsumeAll() throws Exception {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 Integer.MAX_VALUE,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1", "value2"),
 1);

 CommandLine commandLine = new CommandLine();
 List<String> args = Arrays.asList("--arg", "--consume-remaining", "value1", "value2", "value3");
 ArgumentParser parser = new ArgumentParser();
 parser.parseArgument(argument, commandLine, args);
 assertEquals(3, commandLine.getValues(argument).size());
 assertEquals("value1", commandLine.getValue(argument, 0));
 assertEquals("value2", commandLine.getValue(argument, 1));
 assertEquals("value3", commandLine.getValue(argument, 2));
 }

 @Test
 public void testValidate() throws Exception {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 1,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1"),
 1);

 CommandLine commandLine = new CommandLine();
 List<String> args = Arrays.asList("--arg", "value1");
 ArgumentParser parser = new ArgumentParser();
 try {
 parser.parseArgument(argument, commandLine, args);
 fail();
 } catch (OptionException e) {
 // expected
 }
 }

 @Test
 public void testValidateMultiple() throws Exception {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 3,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1", "value2"),
 1);

 CommandLine commandLine = new CommandLine();
 List<String> args = Arrays.asList("--arg", "value1", "value2", "value3");
 ArgumentParser parser = new ArgumentParser();
 parser.parseArgument(argument, commandLine, args);
 ArgumentValidator validator = new ArgumentValidator();
 argument.validate(commandLine, argument);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testValidateInvalid() throws Exception {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 1,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1"),
 1);

 CommandLine commandLine = new CommandLine();
 List<String> args = Arrays.asList("--arg", "value1", "invalidvalue");
 ArgumentParser parser = new ArgumentParser();
 try {
 parser.parseArgument(argument, commandLine, args);
 argument.validate(commandLine, argument);
 fail();
 } catch (OptionException e) {
 // expected
 } catch (InvalidArgumentException ive) {
 // expected
 assertEquals("invalidvalue", ive.getArgument());
 assertEquals("Invalid argument: invalidvalue", ive.getMessage());
 }
 }

 @Test
 public void testHelpLines() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "An example argument",
 1,
 1,
 ':',
 ';',
 null,
 "--",
 Arrays.asList("value1"),
 1);

 HelpFormatter formatter = new BasicHelpFormatter();
 List<HelpLine> lines = argument.helpLines(0, TestUtils.getDefaultHelpSettings(), null);
 String help = formatter.printHelp("Test", lines.get(0).getHelp());
 System.out.println(help);
 }
}
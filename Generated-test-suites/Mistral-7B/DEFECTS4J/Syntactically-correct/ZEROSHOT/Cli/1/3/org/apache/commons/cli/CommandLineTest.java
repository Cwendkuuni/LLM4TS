import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 @Test
 public void testNewCommandLine() {
 CommandLine commandLine = new CommandLine();
 assertNull(commandLine.getArgs());
 assertEmpty(commandLine.getArgList());
 assertEmpty(commandLine.getOptions());
 }

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Test option a"));
 assertTrue(commandLine.hasOption("a"));
 assertTrue(commandLine.hasOption('a'));
 assertFalse(commandLine.hasOption("banana"));
 assertFalse(commandLine.hasOption('b'));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Test option a"));
 commandLine.addOption(new Option("a", "orange", false, "Test option a with different value"));
 commandLine.addArg("test");

 assertEquals("Test option a is set to true", "true", commandLine.getOptionValue("a"));
 assertEquals("Test option a is set to true", 'T', commandLine.getOptionValue('a'));
 assertEquals("Test option a is set to true with default value", "true", commandLine.getOptionValue("a", "false"));
 assertEquals("Test option a is set to true with default value", 'T', commandLine.getOptionValue('a', 'F'));
 assertEquals("Test option a is set to false", null, commandLine.getOptionValue("b"));
 assertEquals("Test option a is set to false", null, commandLine.getOptionValue('b'));
 assertEquals("Test option a is set to 'orange'", "orange", commandLine.getOptionValue("a", "orange"));
 assertEquals("Test option a is set to 'orange'", 'O', commandLine.getOptionValue('a', 'O'));
 assertEquals("Test command line arguments", new String[]{"test"}, commandLine.getArgs());
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", null, true, "Test option a with multiple values"));
 commandLine.addOption(new Option("b", "color", true, "Test option b with single value"));
 commandLine.addArg("--a 1 --a 2 --b red");

 String[] values1 = commandLine.getOptionValues("a");
 assertEquals("Test option a has two values", 2, values1.length);
 assertEquals("Test option a first value", "1", values1[0]);
 assertEquals("Test option a second value", "2", values1[1]);

 String valueB = commandLine.getOptionValue("b");
 assertEquals("Test option b has value", "red", valueB);
 }

 @Test
 public void testGetOptions() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Test option a"));
 commandLine.addOption(new Option("b", "banana", false, "Test option b"));
 commandLine.addArg("--apple --banana test");

 Option[] options = commandLine.getOptions();
 assertEquals("Test command line has 2 options", 2, options.length);
 Option optionA = options[0];
 Option optionB = options[1];

 assertEquals("Test option a key", "a", optionA.getKey());
 assertEquals("Test option a long name", "apple", optionA.getLongOpt());
 assertTrue("Test option a is setable", optionA.isSetable());
 assertEquals("Test option a has argument", false, optionA.hasArg());
 assertEquals("Test option a description", "Test option a", optionA.getDescription());

 assertEquals("Test option b key", "b", optionB.getKey());
 assertEquals("Test option b long name", "banana", optionB.getLongOpt());
 assertFalse("Test option b is not setable", optionB.isSetable());
 assertEquals("Test option b has argument", false, optionB.hasArg());
 assertEquals("Test option b description", "Test option b", optionB.getDescription());
 }
}
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private static final int TEST_ID = 1;
 private static final String TEST_PREFERRED_NAME = "testOption";
 private static final String TEST_DESCRIPTION = "Test Option";
 private static final String[] TEST_PREFIXES = {"-t", "--test"};
 private static final String[] TEST_TRIGGERS = {"test", "trigger"};

 @Test
 public void testCreateOption() {
 Option option = new OptionBuilder()
 .withID(TEST_ID)
 .withRequired(false)
 .withLongOpt("test")
 .withDesc(TEST_DESCRIPTION)
 .create();

 OptionImpl optionImpl = (OptionImpl) option;

 assertEquals(TEST_ID, optionImpl.getId());
 assertFalse(optionImpl.isRequired());
 assertEquals(TEST_DESCRIPTION, optionImpl.getDescription());
 assertArrayEquals(TEST_PREFIXES, optionImpl.getPrefixes().toArray());
 assertArrayEquals(TEST_TRIGGERS, optionImpl.getTriggers().toArray());
 }

 @Test
 public void testCanProcess() {
 Option option = new OptionBuilder()
 .withID(TEST_ID)
 .withRequired(false)
 .create();

 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

 assertFalse(option.canProcess(commandLine, arguments));

 arguments.next(); // skip the command name
 String testArgument = "test";
 arguments.next(); // skip the option prefix
 arguments.previous(); // put the argument back
 arguments.set(arguments.nextIndex(), testArgument); // replace next argument with the test argument

 assertTrue(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testFindOption() {
 Option option1 = new OptionBuilder()
 .withID(1)
 .withRequired(false)
 .withLongOpt("test1")
 .create();
 Option option2 = new OptionBuilder()
 .withID(2)
 .withRequired(false)
 .withLongOpt("test2")
 .create();

 Option testOption = new OptionBuilder()
 .withID(1)
 .withRequired(false)
 .withLongOpt("test")
 .create();

 Option[] options = {option1, testOption, option2};
 CommandLineParser parser = new DefaultParser();

 WriteableCommandLine commandLine = new WriteableCommandLine();
 parser.parse(options, commandLine, new String[]{"test", "--test", "arg"});

 Option foundOption = testOption.findOption(commandLine, "test");
 Option expectedOption = testOption;

 assertEquals(expectedOption, foundOption);
 }
}
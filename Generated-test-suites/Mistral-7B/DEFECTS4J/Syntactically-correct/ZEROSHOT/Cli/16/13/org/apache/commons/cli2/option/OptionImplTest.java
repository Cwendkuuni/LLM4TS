import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.Default;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class OptionImplTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new OptionBuilder("optionName")
 .withLongOpt("longOptionName")
 .withDescription("A test option")
 .withRequiredArg()
 .create();
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testOptionImplConstructor() {
 Option newOption = new OptionImpl(1, true);
 assertEquals(1, newOption.getId());
 assertTrue(newOption.isRequired());
 }

 @Test
 public void testToString() {
 assertEquals(option.toString(), "Option [optionName, --optionName, -o, --longOptionName]\n" +
 "-o OPTION_VALUE --longOptionName OPTION_VALUE --optionName OPTION_VALUE --longOptionName OPTION_VALUE\n" +
 "Option description: A test option\n");
 }

 @Test
 public void testCanProcess() {
 // test canProcess with valid arguments
 commandLine.parseArgument(-1, "validArgument");
 assertTrue(option.canProcess(commandLine, commandLine.getArgs().listIterator()));

 // test canProcess with invalid arguments
 commandLine.parseArgument(-1, "");
 assertFalse(option.canProcess(commandLine, commandLine.getArgs().listIterator()));
 }

 @Test
 public void testEquals() {
 Option equalOption = new OptionBuilder("optionName")
 .withLongOpt("longOptionName2")
 .withDescription("A test option")
 .withRequiredArg()
 .create();
 Option notEqualOption = new OptionBuilder("anotherOptionName")
 .withLongOpt("anotherLongOptionName")
 .withDescription("Another test option")
 .withRequiredArg()
 .create();
 Option sameOption = option;

 assertTrue(option.equals(sameOption));
 assertTrue(option.equals(option));
 assertFalse(option.equals(notEqualOption));
 assertFalse(option.equals(null));
 assertFalse(option.equals("not an Option"));
 assertTrue(option.equals(equalOption));
 }

 @Test
 public void testHashCode() {
 Option option1 = new OptionBuilder("optionName1")
 .withLongOpt("longOptionName1")
 .withDescription("Option 1")
 .withRequiredArg()
 .create();
 Option option2 = new OptionBuilder("optionName2")
 .withLongOpt("longOptionName2")
 .withDescription("Option 2")
 .withRequiredArg()
 .create();

 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();

 assertNotEquals(hashCode1, hashCode2);

 Option option3 = new OptionBuilder("optionName1")
 .withLongOpt("longOptionName1")
 .withDescription("Option 1")
 .withRequiredArg()
 .create();

 assertEquals(hashCode1, option3.hashCode());
 }

 @Test
 public void testFindOption() {
 Option testOption = new OptionBuilder("testOptionName")
 .withLongOpt("longTestOptionName")
 .withDescription("A test option")
 .withRequiredArg()
 .withValueSeparator(';')
 .create();

 List<Option> options = Arrays.asList(option, testOption);
 Option foundOption = option.findOption("optionName");
 Option expectedOption = option;

 assertEquals(expectedOption, foundOption);

 foundOption = option.findOption("longOptionName");
 assertEquals(expectedOption, foundOption);

 foundOption = option.findOption("testOptionName");
 assertEquals(testOption, foundOption);

 foundOption = option.findOption("longTestOptionName");
 assertEquals(testOption, foundOption);

 foundOption = option.findOption("invalidTrigger");
 assertNull(foundOption);
 }

 @Test
 public void testIsRequired() {
 assertTrue(option.isRequired());
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue(option.getLongOpt()));
 }

 @Test
 public void testCheckPrefixes() {
 Set<String> prefixes = Arrays.asList("--test", "-t").asList();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidPrefix() {
 Set<String> prefixes = Arrays.asList("--test", "-o");
 option.checkPrefixes(prefixes);
 }
}
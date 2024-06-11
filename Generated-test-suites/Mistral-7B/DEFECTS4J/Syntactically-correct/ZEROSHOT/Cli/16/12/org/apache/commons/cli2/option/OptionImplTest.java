import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private static final int ID = 1;
 private static final boolean REQUIRED = true;
 private static final String PREFERRED_NAME = "testOption";
 private static final String DESCRIPTION = "This is a test option";
 private static final String[] PREFIXES = {"-t", "--test"};
 private static final String[] TRIGGERS = {"test", "option"};

 @Test
 public void testOptionImplConstructor() {
 OptionImpl option = new OptionImpl(ID, REQUIRED);
 assertEquals(ID, option.getId());
 assertEquals(REQUIRED, option.isRequired());
 assertNull(option.getPreferredName());
 assertNull(option.getDescription());
 assertNull(option.getPrefixes());
 assertNull(option.getTriggers());
 }

 @Test
 public void testOptionImplWithName() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 Option parsedOption = OptionBuilder.withLongOpt(PREFERRED_NAME)
 .withDescription(DESCRIPTION)
 .create();
 assertEquals(ID, option.getId());
 assertEquals(REQUIRED, option.isRequired());
 assertEquals(PREFERRED_NAME, option.getPreferredName());
 assertEquals(DESCRIPTION, option.getDescription());
 assertArrayEquals(PREFIXES, option.getPrefixes().toArray());
 assertArrayEquals(TRIGGERS, option.getTriggers().toArray());
 assertEquals(parsedOption, option);
 }

 @Test
 public void testCanProcess() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 WriteableCommandLine commandLine = new CommandLine();
 CommandLine arguments = new CommandLine();
 arguments.addArgument("testArgument");

 assertFalse(option.canProcess(commandLine, arguments.iterator()));

 commandLine.addOption(OptionBuilder.withLongOpt(PREFERRED_NAME)
 .withDescription(DESCRIPTION)
 .create());

 assertTrue(option.canProcess(commandLine, arguments.listIterator()));

 arguments.add("testArgument");
 arguments.add("--test");

 assertTrue(option.canProcess(commandLine, arguments.listIterator()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithEmptySet() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 Set<String> prefixes = java.util.Collections.emptySet();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidPrefix() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 Set<String> prefixes = java.util.Arrays.asList("test");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testToString() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 assertEquals(OptionBuilder.withLongOpt(PREFERRED_NAME)
 .withDescription(DESCRIPTION)
 .create().toString(), option.toString());
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 OptionImpl option2 = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 OptionImpl option3 = new OptionImpl(ID + 1, REQUIRED, PREFERRED_NAME);
 OptionImpl option4 = new OptionImpl(ID, !REQUIRED, PREFERRED_NAME);
 OptionImpl option5 = new OptionImpl(ID, REQUIRED, "TestOption");

 assertEquals(option1, option1);
 assertEquals(option1, option2);
 assertNotEquals(option1, option3);
 assertNotEquals(option1, option4);
 assertNotEquals(option1, option5);
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 OptionImpl option2 = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 OptionImpl option3 = new OptionImpl(ID + 1, REQUIRED, PREFERRED_NAME);
 OptionImpl option4 = new OptionImpl(ID, !REQUIRED, PREFERRED_NAME);
 OptionImpl option5 = new OptionImpl(ID, REQUIRED, "TestOption");

 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 int hashCode3 = option3.hashCode();
 int hashCode4 = option4.hashCode();
 int hashCode5 = option5.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 assertNotEquals(hashCode1, hashCode5);
 }

 @Test
 public void testFindOption() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 Set<Option> options = java.util.Collections.singleton(option);

 Option foundOption = option.findOption("test");
 assertSame(option, foundOption);

 foundOption = option.findOption("option");
 assertSame(option, foundOption);

 foundOption = option.findOption("invalid");
 assertNull(foundOption);
 }

 @Test
 public void testIsRequired() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 assertTrue(option.isRequired());

 option = new OptionImpl(ID, !REQUIRED, PREFERRED_NAME);
 assertFalse(option.isRequired());
 }

 @Test
 public void testDefaults() {
 OptionImpl option = new OptionImpl(ID, REQUIRED, PREFERRED_NAME);
 WriteableCommandLine commandLine = new WriteableCommandLine();
 option.defaults(commandLine);
 assertFalse(commandLine.hasOption(PREFERRED_NAME));
 }
}
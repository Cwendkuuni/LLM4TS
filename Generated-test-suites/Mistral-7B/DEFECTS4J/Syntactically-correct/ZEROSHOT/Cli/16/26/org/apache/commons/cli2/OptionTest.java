import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionTest {

 @Test
 public void testProcess() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = new java.util.ArrayList<>(List.of("test", "arg")).listIterator();
 option.process(commandLine, args);
 assertEquals("test=arg", commandLine.getOptionValues("test").toString());
 }

 @Test
 public void testProcessThrowsOptionException() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = new java.util.ArrayList<>(List.of("test")).listIterator();
 assertThrows(OptionException.class, () -> option.process(commandLine, args));
 }

 @Test
 public void testDefaults() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValues("test"));
 }

 @Test
 public void testCanProcessString() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 assertTrue(option.canProcess(commandLine, "test arg"));
 }

 @Test
 public void testCanProcessListIteratorFalse() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = new java.util.ArrayList<>(List.of("test")).listIterator();
 assertFalse(option.canProcess(commandLine, args));
 }

 @Test
 public void testCanProcessListIteratorTrue() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> args = new java.util.ArrayList<>(List.of("test", "arg")).listIterator();
 assertTrue(option.canProcess(commandLine, args));
 }

 @Test
 public void testGetTriggers() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 Set<String> triggers = option.getTriggers();
 assertNotNull(triggers);
 assertEquals(1, triggers.size());
 assertEquals("test", triggers.iterator().next());
 }

 @Test
 public void testGetPrefixes() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 Set<String> prefixes = option.getPrefixes();
 assertNotNull(prefixes);
 assertEquals(1, prefixes.size());
 assertEquals("-", prefixes.iterator().next());
 }

 @Test
 public void testValidate() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 commandLine.setOptionValues("test", "arg");
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateThrowsOptionException() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 List helpLines = option.helpLines(1, null, null);
 assertNotNull(helpLines);
 assertEquals(1, helpLines.size());
 }

 @Test
 public void testAppendUsage() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, null, null);
 assertNotNull(buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 String preferredName = option.getPreferredName();
 assertEquals("test", preferredName);
 }

 @Test
 public void testGetDescription() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 String description = option.getDescription();
 assertEquals("A test option", description);
 }

 @Test
 public void testGetId() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 int id = option.getId();
 assertEquals(0, id);
 }

 @Test
 public void testFindOptionNull() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 Option foundOption = option.findOption(null);
 assertNull(foundOption);
 }

 @Test
 public void testFindOptionMatch() {
 Option option1 = OptionBuilder.withLongOpt("test1")
 .hasArg()
 .desc("A test option 1")
 .build();
 Option option2 = OptionBuilder.withLongOpt("test2")
 .hasArg()
 .desc("A test option 2")
 .build();
 Option testOption = option1;
 Option foundOption = option2.findOption("test1");
 assertEquals(testOption, foundOption);
 }

 @Test
 public void testFindOptionMismatch() {
 Option option1 = OptionBuilder.withLongOpt("test1")
 .hasArg()
 .desc("A test option 1")
 .build();
 Option option2 = OptionBuilder.withLongOpt("test2")
 .hasArg()
 .desc("A test option 2")
 .build();
 Option testOption = option1;
 Option foundOption = option2.findOption("test3");
 assertNull(foundOption);
 }

 @Test
 public void testIsRequiredFalse() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 assertFalse(option.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .required(true)
 .build();
 assertTrue(option.isRequired());
 }

 @Test
 public void testGetParentNull() {
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 assertNull(option.getParent());
 }

 @Test
 public void testSetParent() {
 Option parentOption = OptionBuilder.withLongOpt("parent")
 .hasArg()
 .desc("A parent option")
 .build();
 Option option = OptionBuilder.withLongOpt("test")
 .hasArg()
 .desc("A test option")
 .build();
 option.setParent(parentOption);
 assertEquals(parentOption, option.getParent());
 }
}
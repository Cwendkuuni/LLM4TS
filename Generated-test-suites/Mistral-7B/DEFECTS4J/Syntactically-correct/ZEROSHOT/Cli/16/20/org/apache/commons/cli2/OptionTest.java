import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

public class OptionTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new MyOption();
 commandLine = new DefaultCommandLine();
 }

 @Test
 public void testProcess() throws OptionException {
 option.process(commandLine, commandLine.iterator());
 Assert.assertEquals("Expected argument not processed", 1, commandLine.getArgs().size());
 }

 @Test(expected = OptionException.class)
 public void testProcessInvalidArgument() throws OptionException {
 option.process(commandLine, commandLine.iterator());
 commandLine.addOption(Option.builder("t").hasArg().desc("test option").build());
 option.process(commandLine, commandLine.iterator());
 commandLine.add("-t");
 option.process(commandLine, commandLine.iterator());
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 Assert.assertNull("Expected null for uninitialized option", option.getValue());
 }

 @Test
 public void testCanProcess() {
 Set triggers = new HashSet();
 triggers.add("t");
 option.setTriggers(triggers);

 Set prefixes = new HashSet();
 prefixes.add("-");
 option.setPrefixes(prefixes);

 Assert.assertTrue("Expected true for valid argument", option.canProcess(commandLine, commandLine.iterator()));
 Assert.assertFalse("Expected false for invalid argument", option.canProcess(commandLine, Arrays.asList("invalid").listIterator()));
 }

 @Test
 public void testGetTriggers() {
 Set triggers = option.getTriggers();
 Set expectedTriggers = new HashSet();
 expectedTriggers.add("t");
 Assert.assertEquals("Expected triggers to be equal", expectedTriggers, triggers);
 }

 @Test
 public void testGetPrefixes() {
 Set prefixes = option.getPrefixes();
 Set expectedPrefixes = new HashSet();
 expectedPrefixes.add("-");
 Assert.assertEquals("Expected prefixes to be equal", expectedPrefixes, prefixes);
 }

 @Test
 public void testValidate() throws OptionException {
 option.defaults(commandLine);
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testInvalidValidate() throws OptionException {
 option.defaults(commandLine);
 commandLine.addOption(Option.builder("t").hasArg().desc("test option").build());
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 int depth = 0;
 Set helpSettings = new HashSet();
 Comparator comp = null;
 List helpLines = option.helpLines(depth, helpSettings, comp);
 Assert.assertNotNull("Expected non-null helpLines", helpLines);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 Set helpSettings = new HashSet();
 Comparator comp = null;
 option.appendUsage(buffer, helpSettings, comp);
 Assert.assertNotNull("Expected non-empty usage string", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 String preferredName = option.getPreferredName();
 Assert.assertNotNull("Expected non-null preferredName", preferredName);
 }

 @Test
 public void testGetDescription() {
 String description = option.getDescription();
 Assert.assertNotNull("Expected non-null description", description);
 }

 @Test
 public void testGetId() {
 int id = option.getId();
 Assert.assertNotEquals("Expected non-zero id", 0, id);
 }

 @Test
 public void testFindOption() {
 Option foundOption = option.findOption("t");
 Assert.assertNotNull("Expected foundOption to be non-null", foundOption);
 }

 @Test
 public void testIsRequired() {
 Assert.assertFalse("Expected option to not be required", option.isRequired());
 }

 @Test
 public void testSetParent() {
 Option parentOption = new MyOption();
 option.setParent(parentOption);
 Option childOption = new MyOption();
 parentOption.addOption(childOption);
 Option foundOption = parentOption.findOption("t");
 Assert.assertSame("Expected option to be parent's child", childOption, foundOption);
 }
}

 class MyOption extends AbstractOption {
 MyOption() {
 super("t", "test", false, "test");
 }
 }
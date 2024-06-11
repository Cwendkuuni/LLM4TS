import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 private Option testOption;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 testOption = new MyTestOption();
 }

 @Test
 public void testProcess() throws OptionException {
 CommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> arguments = new MyTestListIterator();
 testOption.process(commandLine, arguments);
 assertEquals("expected argument", commandLine.getOptionValue("testOption"), "expectedValue");
 }

 @Test(expected = OptionException.class)
 public void testProcessInvalidArgument() throws OptionException {
 CommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> arguments = new MyTestListIterator();
 testOption.process(commandLine, arguments);
 arguments.next(); // consume the option trigger
 testOption.process(commandLine, arguments); // should fail
 }

 @Test
 public void testDefaults() {
 testOption.defaults(new WriteableCommandLine());
 // verify default values have been set
 }

 @Test
 public void testCanProcess() {
 assertFalse("cannot process null commandline", testOption.canProcess(null, null));
 assertTrue("can process valid argument", testOption.canProcess(new WriteableCommandLine(), new MyTestListIterator()));
 }

 @Test(expected = OptionException.class)
 public void testCannotProcess() {
 assertFalse("should not be able to process", testOption.canProcess(new WriteableCommandLine(), new MyTestListIterator()));
 testOption.process(new WriteableCommandLine(), new MyTestListIterator()); // consume the invalid argument
 assertFalse("still cannot process", testOption.canProcess(new WriteableCommandLine(), new MyTestListIterator()));
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = testOption.getTriggers();
 assertNotNull("triggers should be non null", triggers);
 assertTrue("triggers should contain the expected values", triggers.contains("testTrigger"));
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = testOption.getPrefixes();
 assertNotNull("prefixes should be non null", prefixes);
 assertTrue("prefixes should contain the expected values", prefixes.contains("-"));
 }

 @Test
 public void testValidate() throws OptionException {
 testOption.validate(new WriteableCommandLine());
 }

 @Test(expected = OptionException.class)
 public void testValidateInvalidCommandLine() throws OptionException {
 testOption.validate(new WriteableCommandLine()); // validate once to clear any issues
 testOption.validate(new WriteableCommandLine()); // should fail
 }

 @Test
 public void testHelpLines() {
 Set<Object> helpSettings = new java.util.HashSet<>();
 List<HelpLine> helpLines = testOption.helpLines(0, helpSettings, null);
 assertNotNull("helpLines should be non null", helpLines);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 testOption.appendUsage(buffer, helpSettings, null);
 assertEquals("usage string equals expected usage string", "expectedUsageString", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("preferredName", testOption.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("description", testOption.getDescription());
 }

 @Test
 public void testGetId() {
 assertEquals(1, testOption.getId());
 }

 @Test
 public void testFindOption() {
 Option option = testOption.findOption("otherOption");
 assertNull("findOption should return null for non-matching options", option);
 Option matchingOption = testOption.findOption("testOption");
 assertSame("findOption should return the same option instance", testOption, matchingOption);
 }

 @Test
 public void testIsRequired() {
 assertFalse("option is not required", testOption.isRequired());
 }

 @Test
 public void testGetParent() {
 Option parentOption = new MyParentOption();
 testOption.setParent(parentOption);
 Option childOption = new MyChildOption();
 parentOption.addOption(childOption);
 Option result = testOption.findOption("testOption");
 assertSame("findOption should return the same option instance", testOption, result);
 Option parent = testOption.getParent();
 assertSame("getParent should return the parent option instance", parentOption, parent);
 }
}

class MyTestOption extends AbstractOption {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator<String> arguments)
 throws OptionException {
 super.process(commandLine, arguments);
 commandLine.putStringValue("testOption", arguments.next());
 }
}

class MyTestListIterator implements ListIterator<String> {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public String next() {
 return "expectedArgument";
 }

 @Override
 public boolean hasPrevious() {
 return false;
 }

 @Override
 public int nextIndex() {
 return 0;
 }

 @Override
 public int previousIndex() {
 return -1;
 }
}
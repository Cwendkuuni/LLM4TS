import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

 private Option testOption;
 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 testOption = new TestOption();
 commandLine = new DefaultCommandLine();
 }

 @Test
 public void testProcess() throws OptionException {
 testOption.process(commandLine, commandLine.iterator());
 Assert.assertEquals("expected argument not processed", "expectedArg", commandLine.getArgument(0));
 }

 @Test(expected = OptionException.class)
 public void testProcessInvalidArgument() throws OptionException {
 testOption.process(commandLine, commandLine.iterator());
 testOption.process(commandLine, commandLine.iterator());
 testOption.process(commandLine, commandLine.iterator());
 testOption.process(commandLine, commandLine.iterator());
 testOption.process(commandLine, commandLine.iterator());
 testOption.process(commandLine, commandLine.iterator()); // should throw exception
 }

 @Test
 public void testDefaults() {
 testOption.defaults(commandLine);
 Assert.assertEquals("default value not set", "defaultValue", commandLine.getOptionValue("testOption"));
 }

 @Test
 public void testCanProcess() {
 Set triggers = new HashSet(Arrays.asList("--test", "-t"));
 Set prefixes = new HashSet(Arrays.asList("--", "-"));
 testOption.setTriggers(triggers);
 testOption.setPrefixes(prefixes);

 Assert.assertTrue("expected true for valid argument", testOption.canProcess(commandLine, "testArg"));
 Assert.assertTrue("expected true for valid argument iterator", testOption.canProcess(commandLine, commandLine.iterator()));
 Assert.assertFalse("expected false for non-valid argument", testOption.canProcess(commandLine, "nonValidArg"));
 }

 @Test
 public void testFindOption() {
 Option option1 = new TestOption("option1");
 Option option2 = new TestOption("option2");
 testOption.addOption(option1);
 testOption.addOption(option2);

 Option foundOption = testOption.findOption("option1");
 Assert.assertNotNull("expected option1 to be found", foundOption);
 }

 @Test
 public void testValidate() throws OptionException {
 testOption.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateInvalidCommandLine() throws OptionException {
 testOption.validate(commandLine);
 commandLine.putString("testOption", "invalidValue");
 testOption.validate(commandLine); // should throw exception
 }

 @Test
 public void testHelpLines() {
 Set helpSettings = new HashSet();
 HelpFormatter formatter = new HelpFormatter();
 List helpLines = testOption.helpLines(0, helpSettings, null);
 formatter.printHelp("test", helpLines.toArray(new HelpLine[helpLines.size()]));
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 testOption.appendUsage(buffer, helpSettings, null);
 }

 @Test
 public void testGetPreferredName() {
 Assert.assertEquals("expected preferred name", "testOption", testOption.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 Assert.assertEquals(
 "expected description",
 "This is a test option",
 testOption.getDescription());
 }

 @Test
 public void testGetId() {
 Assert.assertEquals("expected id", 1, testOption.getId());
 }

 @Test
 public void testGetTriggers() {
 Set triggers = testOption.getTriggers();
 Assert.assertEquals("expected number of triggers", 2, triggers.size());
 Assert.assertTrue("expected trigger to be present", triggers.contains("--test"));
 Assert.assertTrue("expected trigger to be present", triggers.contains("-t"));
 }

 @Test
 public void testGetPrefixes() {
 Set prefixes = testOption.getPrefixes();
 Assert.assertEquals("expected number of prefixes", 2, prefixes.size());
 Assert.assertTrue("expected prefix to be present", prefixes.contains("--"));
 Assert.assertTrue("expected prefix to be present", prefixes.contains("-"));
 }

 @Test
 public void testIsRequired() {
 Assert.assertFalse("expected option to be not required", testOption.isRequired());
 }

 @Test
 public void testGetParent() {
 Option parentOption = new TestOption("parentOption");
 testOption.setParent(parentOption);
 Assert.assertEquals("expected parent option", parentOption, testOption.getParent());
 }

 @Test
 public void testAddOption() {
 Option childOption = new TestOption("childOption");
 testOption.addOption(childOption);
 Option foundOption = testOption.findOption("childOption");
 Assert.assertEquals("expected child option to be added", childOption, foundOption);
 }

 @Test
 public void testParseOptions() throws ParseException {
 DefaultParser parser = new DefaultParser();
 ParseContext context = new ParseContext();
 Option testOption = new TestOption();
 testOption.setRequired(true);
 testOption.setArgs(0);
 parser.parse(testOption, Arrays.asList("--test", "testArg"), context);
 Assert.assertEquals("expected test option to be present", testOption, context.getOption("testOption"));
 }
}

 class TestOption extends Option {
 private static final long serialVersionUID = 1L;
 private String preferredName;
 private String description;
 private int id;
 private String defaultValue;
 private boolean required;
 private Option parent;
 private List<Option> options;

 public TestOption() {
 this.preferredName = "testOption";
 this.description = "This is a test option";
 this.id = 1;
 this.defaultValue = "defaultValue";
 this.required = false;
 this.options = new ArrayList();
 }

 public TestOption(String name) {
 this();
 this.preferredName = name;
 }

 @Override
 public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
 super.process(commandLine, args);
 String arg = (String) args.next();
 commandLine.putString("testOption", arg);
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.putString("testOption", defaultValue);
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, String argument) {
 return Arrays.asList("testArg").contains(argument);
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
 return Arrays.asList("testArg").contains(arguments.next());
 }

 @Override
 public Set<String> getTriggers() {
 return new HashSet<>(Arrays.asList("--test", "-t"));
 }

 @Override
 public Set<String> getPrefixes() {
 return new HashSet<>(Arrays.asList("--", "-"));
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // no validation required
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 List<HelpLine> helpLines = new ArrayList<>();
 helpLines.add(new HelpLine(0, null, getPreferredName(), getDescription(), null, null, null));
 return helpLines;
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 buffer.append("--test <testArg>\n");
 }

 @Override
 public String getPreferredName() {
 return preferredName;
 }

 @Override
 public String getDescription() {
 return description;
 }

 @Override
 public int getId() {
 return id;
 }

 @Override
 public Option findOption(String trigger) {
 return this;
 }

 @Override
 public boolean isRequired() {
 return required;
 }

 @Override
 public Option getParent() {
 return parent;
 }

 @Override
 public void setParent(Option parent) {
 this.parent = parent;
 }

 @Override
 public void addOption(Option option) {
 options.add(option);
 }

 @Override
 public List<Option> getOptions() {
 return options;
 }
 }
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class OptionTest {

 private WriteableCommandLine cmd;
 private Options options;

 @Before
 public void setUp() {
 cmd = new WriteableCommandLine();
 options = new Options();
 }

 @Test
 public void testProcess() throws Exception {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 option.process(cmd, cmd.iterator());
 Assert.assertEquals("Help option", cmd.getOptionValue("o"));
 }

 @Test(expected = OptionException.class)
 public void testProcessInvalidArgument() throws Exception {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 cmd.addArgument("invalid");
 option.process(cmd, cmd.iterator());
 }

 @Test
 public void testDefaults() throws Exception {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 option.defaults(cmd);
 Assert.assertEquals("defaultValue", cmd.getOptionValue("o"));
 }

 @Test
 public void testCanProcess() throws Exception {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 Assert.assertTrue(option.canProcess(cmd, Arrays.asList("--option").iterator()));
 Assert.assertTrue(option.canProcess(cmd, Arrays.asList("--option", "value").iterator()));
 Assert.assertFalse(option.canProcess(cmd, Arrays.asList("--arg").iterator()));
 }

 @Test
 public void testGetTriggers() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 Assert.assertNotNull(option.getTriggers());
 Set triggers = option.getTriggers();
 Assert.assertEquals(1, triggers.size());
 Assert.assertEquals("--option", triggers.iterator().next().toString());
 }

 @Test
 public void testGetPrefixes() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 Assert.assertNotNull(option.getPrefixes());
 Set prefixes = option.getPrefixes();
 Assert.assertEquals(1, prefixes.size());
 Assert.assertEquals("-", prefixes.iterator().next().toString());
 }

 @Test
 public void testValidate() throws Exception {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 option.validate(cmd);
 }

 @Test(expected = OptionException.class)
 public void testValidateInvalidCommandLine() throws Exception {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 cmd.addArgument("invalid");
 option.validate(cmd);
 }

 @Test
 public void testHelpLines() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 List helpLines = option.helpLines(1, new HashSet(), null);
 Assert.assertNotNull(helpLines);
 Assert.assertEquals(1, helpLines.size());
 String helpLine = (String) helpLines.get(0);
 Assert.assertTrue(helpLine.contains("--option, -o, h, Help option"));
 }

 @Test
 public void testAppendUsage() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, new HashSet(), null);
 Assert.assertEquals("--option <value>...\n", buffer.toString());
 }

 @Test
 public void testPreferredName() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 Assert.assertEquals("option", option.getPreferredName());
 }

 @Test
 public void testDescription() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 Assert.assertEquals("Help option", option.getDescription());
 }

 @Test
 public void testId() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 Assert.assertEquals(1, option.getId());
 }

 @Test
 public void testFindOption() {
 Option option1 = new MockOption("option1", "o1", "h1", "Help option 1", true);
 Option option2 = new MockOption("option2", "o2", "h2", "Help option 2", true);
 options.addOption(option1);
 options.addOption(option2);
 Option result = options.findOption("o1");
 Assert.assertEquals(option1, result);
 }

 @Test
 public void testIsRequired() {
 Option option = new MockOption("option", "o", "h", "Help option", true);
 Assert.assertFalse(option.isRequired());
 }

 @Test
 public void testHasParent() {
 Option parentOption = new MockOption("parent", "p", "h", "Parent option", true);
 Option option = new MockOption("option", "o", "h", "Help option", true);
 option.setParent(parentOption);
 Assert.assertEquals(parentOption, option.getParent());
 }

 @Test
 public void testProcessWithParent() throws Exception {
 Option parentOption = new MockOption("parent", "p", "h", "Parent option", true);
 Option option = new MockOption("option", "o", "h", "Help option", true);
 option.setParent(parentOption);
 option.process(cmd, cmd.iterator());
 Assert.assertEquals("Help option", cmd.getOptionValue("option"));
 Assert.assertEquals("Parent value", cmd.getOptionValue("p"));
 }

 @Test(expected = OptionException.class)
 public void testProcessWithParentInvalidArgument() throws Exception {
 Option parentOption = new MockOption("parent", "p", "h", "Parent option", true);
 Option option = new MockOption("option", "o", "h", "Help option", true);
 option.setParent(parentOption);
 cmd.addArgument("invalid");
 option.process(cmd, cmd.iterator());
 }

 private static class MockOption implements Option {
 private String name;
 private String shortOption;
 private String longOption;
 private String description;
 private boolean required;
 private Option parent;

 public MockOption(String name, String shortOption, String longOption, String description, boolean required) {
 this.name = name;
 this.shortOption = shortOption;
 this.longOption = longOption;
 this.description = description;
 this.required = required;
 }

 public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
 // Empty implementation for test purposes
 }

 public void defaults(WriteableCommandLine commandLine) {
 // Empty implementation for test purposes
 }

 public boolean canProcess(WriteableCommandLine commandLine, String argument) {
 return true;
 }

 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }

 public Set getTriggers() {
 return new LinkedHashSet() {{
 add(longOption);
 add(shortOption);
 }};
 }

 public Set getPrefixes() {
 return new LinkedHashSet() {{
 add("-");
 }};
 }

 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // Empty implementation for test purposes
 }

 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return Collections.emptyList();
 }

 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // Empty implementation for test purposes
 }

 public String getPreferredName() {
 return name;
 }

 public String getDescription() {
 return description;
 }

 public int getId() {
 return 0;
 }

 public Option findOption(String trigger) {
 return null;
 }

 public boolean isRequired() {
 return required;
 }

 public Option getParent() {
 return parent;
 }

 public void setParent(Option parent) {
 this.parent = parent;
 }
 }

}
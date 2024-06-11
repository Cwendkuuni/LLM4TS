import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

 private Options options;
 private CommandLine commandLine;

 @Before
 public void setUp() {
 options = new Options();
 commandLine = new CommandLine();

 // create a simple GroupImpl
 List<Option> optionsList = new ArrayList<>();
 optionsList.add(TestUtils.createOption("option1", false, "o1", "desc1"));
 optionsList.add(TestUtils.createOption("option2", true, "o2", "desc2"));
 optionsList.add(TestUtils.createOption("arg1", true, null, "descarg1"));
 GroupImpl group = new GroupImpl(optionsList, "group1", "descgroup1", 1, 2, false);
 options.addOption(group);
 }

 @Test
 public void testCanProcess() {
 // test with valid options
 assertTrue(group.canProcess(commandLine, "option1"));
 assertTrue(group.canProcess(commandLine, "option2"));
 // test with triggers
 assertTrue(group.canProcess(commandLine, "o1"));
 assertTrue(group.canProcess(commandLine, "o2"));
 // test with argument
 assertTrue(group.canProcess(commandLine, "arg1"));
 // test with unknown option
 assertFalse(group.canProcess(commandLine, "unknown"));
 // test with empty command line
 assertFalse(group.canProcess(commandLine, null));
 }

 @Test
 public void testGetName() {
 assertEquals("group1", group.getName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("descgroup1", group.getDescription());
 }

 @Test
 public void testGetMinimum() {
 assertEquals(1, group.getMinimum());
 }

 @Test
 public void testGetMaximum() {
 assertEquals(2, group.getMaximum());
 }

 @Test
 public void testGetOptions() {
 List<Option> optionsList = group.getOptions();
 assertEquals(3, optionsList.size());
 assertEquals("option1", optionsList.get(0).getLongOpt());
 assertEquals("option2", optionsList.get(1).getLongOpt());
 assertEquals("arg1", optionsList.get(2).getLongOpt());
 }

 @Test
 public void testGetAnonymous() {
 List<Option> anonymousList = group.getAnonymous();
 assertEquals(1, anonymousList.size());
 assertEquals("arg1", anonymousList.get(0).getLongOpt());
 }

 @Test
 public void testFindOption() {
 Option option1 = group.findOption("option1");
 Option option2 = group.findOption("option2");
 Option arg1 = group.findOption("arg1");
 Option unknown = group.findOption("unknown");

 assertTrue(option1 instanceof Option);
 assertTrue(option2 instanceof Option);
 assertTrue(arg1 instanceof Argument);
 assertNull(unknown);
 }

 @Test
 public void testProcess() {
 // setup command line
 commandLine.parse(options.toArray(new Option[]{}), new String[]{"--option1", "--option2", "--arg1"});

 // test with valid options
 group.process(commandLine, commandLine.getArgs().listIterator());
 // test with triggers
 group.process(commandLine, commandLine.getArgs().listIterator());
 // test with argument
 group.process(commandLine, commandLine.getArgs().listIterator());
 // test with empty command line
 group.process(commandLine, commandLine.getArgs().listIterator());
 }

 @Test
 public void testValidate() throws OptionException {
 commandLine.parse(options.toArray(new Option[]{}), new String[]{});
 group.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithNoOption() throws OptionException {
 commandLine.parse(options.toArray(new Option[]{}), new String[]{"--unknown"});
 group.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithTooManyOptions() throws OptionException {
 commandLine.parse(options.toArray(new Option[]{}), new String[]{"--option1", "--option1", "--option2"});
 group.validate(commandLine);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 Set<DisplaySetting> helpSettings = new HashSet<>();
 helpSettings.add(DisplaySetting.DISPLAY_OPTION);
 group.appendUsage(buffer, helpSettings, null);
 String expected = "--group1\n" +
 "--option1 desc1\n" +
 "--option2 desc2\n" +
 "--arg1\n";
 assertEquals(expected, buffer.toString());
 }

 @Test
 public void testHelpLines() {
 List<HelpLine> helpLines = group.helpLines(0, null, null);
 String expected = "--group1\n" +
 "  group1: descgroup1\n" +
 "  Options:\n" +
 "    --option1 OPTION_KEY\n" +
 "      Option description1\n" +
 "    --option2 OPTION_KEY\n" +
 "      Option description2\n" +
 "    --arg1 ARG_KEY\n" +
 "      Argument descriptionarg1\n" +
 "--help\n";
 assertEquals(expected, TestUtils.toString(helpLines));
 }
}
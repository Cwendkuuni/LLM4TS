import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class OptionTest {

 private Option option;
 private WriteableCommandLine commandLine;
 private List<String> args;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLine();
 args = new ArrayList<>();
 }

 @Test
 public void testProcess() throws OptionException {
 String optionName = "option";
 String optionValue = "value";
 Option option = OptionBuilder.withLongOpt(optionName)
 .hasArg()
 .valueSeparator('=')
 .create();
 option.process(commandLine, args.listIterator());
 args.add(optionName + ":value");
 commandLine.parse();
 assertEquals(optionName, commandLine.getOptionValues(optionName).get(0));
 }

 @Test
 public void testProcessWithNoValue() throws OptionException {
 String optionName = "option";
 Option option = OptionBuilder.withLongOpt(optionName)
 .noArg()
 .create();
 option.process(commandLine, args.listIterator());
 args.add(optionName);
 commandLine.parse();
 assertTrue(commandLine.hasOption(optionName));
 }

 @Test
 public void testProcessWithInvalidValue() throws OptionException {
 String optionName = "option";
 Option option = OptionBuilder.withLongOpt(optionName)
 .hasArg()
 .valueSeparator('=')
 .create();
 args.add("--" + optionName + ":invalid");
 assertThrows(OptionException.class, () -> option.process(commandLine, args.listIterator()));
 }

 @Test
 public void testDefaults() {
 Option option = OptionBuilder.withLongOpt("option")
 .valueSeparator('=')
 .hasArg()
 .create();
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue("option"));
 }

 @Test
 public void testCanProcess() throws OptionException {
 String optionName = "option";
 Option option = OptionBuilder.withLongOpt(optionName)
 .valueSeparator('=')
 .hasArg()
 .create();
 assertTrue(option.canProcess(commandLine, args.listIterator()));
 args.add("--" + optionName + ":value");
 args.listIterator().previous();
 assertTrue(option.canProcess(commandLine, args.listIterator()));
 }

 @Test
 public void testCanProcessWithMultipleArguments() {
 String optionName = "option";
 Option option = OptionBuilder.withLongOpt(optionName)
 .hasArgs(2)
 .valueSeparator(' ')
 .create();
 assertTrue(option.canProcess(commandLine, args.listIterator()));
 args.add("--" + optionName + ":value1 value2");
 args.listIterator().previous();
 assertTrue(option.canProcess(commandLine, args.listIterator()));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidArgument() {
 String optionName = "option";
 Option option = OptionBuilder.withLongOpt(optionName)
 .hasArg()
 .valueSeparator('=')
 .create();
 args.add("--" + optionName + ":value:");
 option.canProcess(commandLine, args.listIterator());
 }

 @Test
 public void testGetTriggers() {
 Option option = OptionBuilder.withLongOpt("option")
 .create();
 Set<String> triggers = option.getTriggers();
 assertEquals(Collections.singleton("option"), triggers);
 }

 @Test
 public void testGetPrefixes() {
 Option option = OptionBuilder.withLongOpt("option")
 .create();
 Set<String> prefixes = option.getPrefixes();
 assertEquals(Collections.singleton("--"), prefixes);
 }

 @Test
 public void testValidate() {
 Option option = OptionBuilder.withLongOpt("option")
 .create();
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidCommandLine() {
 Option option = OptionBuilder.withLongOpt("option")
 .create();
 commandLine.putUnknownOption("invalid");
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 Option option = OptionBuilder.withLongOpt("option")
 .withDescription("An option")
 .create();
 List<HelpLine> helpLines = option.helpLines(1, new HashSet<>(), null);
 assertEquals(1, helpLines.size());
 String expectedHelpLine = "--option\n" +
 "\tAn option\n";
 assertEquals(expectedHelpLine, helpLines.get(0).getShortOptionUsage());
 assertEquals(expectedHelpLine, helpLines.get(0).getLongOptionUsage());
 }

 @Test
 public void testAppendUsage() {
 Option option = OptionBuilder.withLongOpt("option")
 .withDescription("An option")
 .create();
 String expectedUsage = "--option\n" +
 "\tAn option\n";
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, new HashSet<>(), null);
 assertEquals(expectedUsage, buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 Option option = OptionBuilder.withLongOpt("option")
 .create();
 assertEquals("option", option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 Option option = OptionBuilder.withLongOpt("option")
 .withDescription("An option")
 .create();
 assertEquals("An option", option.getDescription());
 }

 @Test
 public void testGetId() {
 Option option = OptionBuilder.withLongOpt("option")
 .create();
 assertEquals(0, option.getId());
 }

 @Test
 public void testFindOption() {
 Option option1 = OptionBuilder.withLongOpt("option1")
 .create();
 Option option2 = OptionBuilder.withLongOpt("option2")
 .create();
 Option option3 = OptionBuilder.withLongOpt("option1")
 .create();
 List<Option> options = Arrays.asList(option1, option2, option3);
 Option foundOption = option3.findOption("option1");
 assertSame(option3, foundOption);
 }

 @Test
 public void testIsRequired() {
 Option option = OptionBuilder.withLongOpt("option")
 .isRequired(true)
 .create();
 assertTrue(option.isRequired());
 }

 @Test
 public void testGetParent() {
 Option option1 = OptionBuilder.withLongOpt("option1")
 .create();
 Option option2 = OptionBuilder.withLongOpt("option2")
 .create();
 Option option3 = OptionBuilder.withLongOpt("option3")
 .create();
 option3.setParent(option2);
 option2.setParent(option1);
 Option foundOption = option3.getParent();
 assertSame(option2, foundOption);
 }
}
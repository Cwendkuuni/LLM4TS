import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLine();
 option = OptionBuilder.withLongOpt("testOption")
 .hasArg()
 .isRequired()
 .build();
 }

 @Test
 public void testProcess() throws OptionException {
 List<String> args = Arrays.asList("--testOption", "testArg");
 option.process(commandLine, args.listIterator());

 assertEquals("testArg", commandLine.getOptionValues("testOption")[0]);
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValues("testOption"));
 }

 @Test
 public void testCanProcessArg() {
 assertTrue(option.canProcess(commandLine, "--testOption"));
 assertFalse(option.canProcess(commandLine, "testArg"));
 }

 @Test
 public void testCanProcessArgs() throws OptionException {
 List<String> args = Arrays.asList("--testOption", "testArg");
 assertTrue(option.canProcess(commandLine, args.listIterator()));
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = option.getTriggers();
 assertEquals(1, triggers.size());
 assertEquals("--testOption", triggers.iterator().next());
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = option.getPrefixes();
 assertEquals(1, prefixes.size());
 assertEquals("--", prefixes.iterator().next());
 }

 @Test
 public void testValidate() {
 Set<String> helpSettings = new LinkedHashSet<>();
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateInvalid() throws OptionException {
 Set<String> helpSettings = new LinkedHashSet<>();
 commandLine.parse(new DefaultParser(), Arrays.asList("--testOption"));
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 Set<String> helpSettings = new LinkedHashSet<>();
 List<Option> options = Arrays.asList(option);
 Comparator<Option> comp = new Comparator<Option>() {
 @Override
 public int compare(Option o1, Option o2) {
 return o1.getPreferredName().compareTo(o2.getPreferredName());
 }
 };
 List<HelpLine> helpLines = option.helpLines(1, helpSettings, comp);
 assertEquals(1, helpLines.size());
 assertTrue(helpLines.get(0).getShortName().isEmpty());
 assertTrue(helpLines.get(0).getLongName().equals("--testOption"));
 assertTrue(helpLines.get(0).getDescription().startsWith("testOption"));
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 Set<String> helpSettings = new LinkedHashSet<>();
 option.appendUsage(buffer, helpSettings, comp);
 assertEquals("-" + OptionBuilder.LONG_OPT_PREFIX + "testOption", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("testOption", option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("", option.getDescription());
 }

 @Test
 public void testGetId() {
 assertEquals(0, option.getId());
 }

 @Test
 public void testFindOption() {
 Option testOption = OptionBuilder.withLongOpt("testOption2")
 .hasArg()
 .isRequired()
 .build();
 commandLine.parse(new DefaultParser(), Arrays.asList("--testOption", "--testOption2"));
 Option foundOption = option.findOption("--testOption2");
 assertEquals(testOption, foundOption);
 }

 @Test
 public void testIsRequired() {
 assertTrue(option.isRequired());
 }

 @Test
 public void testGetParent() {
 assertNull(option.getParent());
 }

 @Test
 public void testSetParent() {
 Option parentOption = OptionBuilder.withLongOpt("parentOption")
 .build();
 option.setParent(parentOption);
 assertEquals(parentOption, option.getParent());
 }
}
import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Options options;
 private Parser parser;

 @Before
 public void setUp() {
 options = new Options();
 parser = new BaseParser();
 commandLine = new CliCommandLine();
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "output", true, "Output file");
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 Option option = new Option("o", "output", true, "Output file");
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 Option option = new Option("o", "output", true, "Output file");
 commandLine.addOption(option);
 commandLine.addValue(option, "test.txt");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = new Option("o", "output", true, "Output file");
 commandLine.addOption(option);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertNull(values);
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("o", "output", true, "Output file");
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("default.txt");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertNull(values);
 assertEquals("default.txt", commandLine.getOptionValue(option));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("d", "debug", false, "Enable debug mode");
 commandLine.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = new Option("d", "debug", false, "Enable debug mode");
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("d", "debug", false, "Enable debug mode");
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 Option option = new Option("p", "prefix", true, "Prefix");
 commandLine.addProperty(option, "my", "prefix");
 assertEquals("my", commandLine.getOptionValue(option, "prefix"));
 }

 @Test
 public void testAddPropertyDefault() {
 commandLine.addProperty("prefix", "default");
 assertEquals("default", commandLine.getOptionValue("prefix"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("o"));
 assertFalse(commandLine.looksLikeOption("test"));
 }

 @Test
 public void testParseCommandLine() {
 parser.parse(options, new String[]{"-o", "test.txt", "--output", "test2.txt", "--prefix=my", "--debug"});
 commandLine.parse(parser.getCommandLine(), options);
 List<String> values = commandLine.getUndefaultedValues(options.findOption("output"));
 assertEquals(2, values.size());
 assertEquals("test2.txt", values.get(1));
 assertEquals("my", commandLine.getOptionValue("prefix"));
 assertTrue(commandLine.getOption("debug").isSet());
 }

}
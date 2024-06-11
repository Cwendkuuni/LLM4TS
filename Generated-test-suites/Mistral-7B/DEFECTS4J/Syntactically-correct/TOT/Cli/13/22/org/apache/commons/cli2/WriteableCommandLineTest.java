import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new CliBuilder().build();
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").valueSeparator(':').build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").valueSeparator(':').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 fail("Should not be able to add the same switch twice");
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 List<String> defaultValues = java.util.Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0));
 assertEquals("default2", values.get(1));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("--value"));
 assertFalse(commandLine.looksLikeOption("-v"));
 }

 @Test
 public void testParse() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 parser.parse(commandLine, new String[]{"--option", "value1", "--option:value2"});
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 parser.parse(commandLine, new String[]{"value1"});
 fail("Should fail to parse if an option is missing its value");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 parser.parse(commandLine, new String[]{"--invalid"});
 fail("Should fail to parse if an invalid option is provided");
 }
}
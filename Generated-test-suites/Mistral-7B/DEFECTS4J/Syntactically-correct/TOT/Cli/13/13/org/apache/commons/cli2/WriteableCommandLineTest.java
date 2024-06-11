import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("-o,--option");
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertEquals(option, commandLine.getOption("o"));
 }

 @Test
 public void testAddValue() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("-o,--option value");
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "test");
 assertEquals("test", commandLine.getOptionValues("o").get(0));
 }

 @Test
 public void testGetValues() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option value1 value2 value3");
 Option option = Option.builder("o").longOpt("option").valueSeparator(' ').build();
 commandLine.addOption(option);
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(3, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 assertEquals("value3", values.get(2));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
 Option option = Option.builder("t").longOpt("test").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(2, values.size());
 assertTrue(values.contains("default1"));
 assertTrue(values.contains("default2"));
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
 Option option = Option.builder("t").longOpt("test").build();
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.hasOption("t"));
 assertTrue(commandLine.isSet("t"));
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
 assertTrue(commandLine.looksLikeOption("--option"));
 assertFalse(commandLine.looksLikeOption("value"));
 }

 @Test
 public void testLooksLikeOptionWithDash() {
 WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("value"));
 }
}
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("optionName");
 assertEquals(values.size(), 2);
 assertEquals(values.get(0), "value1");
 assertEquals(values.get(1), "value2");
 }

 @Test
 public void testGetValues() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List<String> values = commandLine.getOptionValues("optionName");
 assertEquals(values.size(), 1);
 assertEquals(values.get(0), "value1");
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testAddSwitch() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 assertTrue(option.getValue().get());
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new CliCommandLine();
 Option option = Option.builder("optionName").build();
 commandLine.setDefaultValues(option, Arrays.asList("value1", "value2"));
 List<String> values = commandLine.getOptionValues("optionName");
 assertEquals(values.size(), 2);
 assertEquals(values.get(0), "value1");
 assertEquals(values.get(1), "value2");
 }

 @Test
 public void testLooksLikeOption() {
 commandLine = new CliCommandLine();
 assertTrue(commandLine.looksLikeOption("--optionName"));
 assertTrue(commandLine.looksLikeOption("-o"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 commandLine = new CliCommandLine();
 assertFalse(commandLine.looksLikeOption("value"));
 }
}
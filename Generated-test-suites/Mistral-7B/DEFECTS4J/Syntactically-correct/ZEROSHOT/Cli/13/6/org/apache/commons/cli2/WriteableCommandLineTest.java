import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import java.util.ArrayList;
import static java.util.Arrays.asList;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionNull() {
 CommandLine commandLine = new DefaultCommandLine();
 commandLine.addOption(null);
 }

 @Test
 public void testAddValue() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = Option.builder("o").longOpt("option")
 .required(true)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 assertEquals("value1", commandLine.getOptionValue("option"));
 commandLine.addValue(option, "value2");
 assertEquals(asList("value1", "value2"), commandLine.getValues("option"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWrongOption() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = Option.builder("o").longOpt("option")
 .required(true)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(new Option("x"), "value");
 }

 @Test
 public void testSetDefaultValues() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = Option.builder("o").longOpt("option")
 .required(true)
 .build();
 commandLine.addOption(option);
 List defaultValues = asList("value1", "value2");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(defaultValues, commandLine.getValues("option"));
 assertNull(commandLine.getOptionValue("option"));
 }

 @Test
 public void testAddSwitch() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 assertTrue(option.getValue().get());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 assertTrue(option.getValue().get());
 }

 @Test
 public void testAddProperty() {
 CommandLine commandLine = new DefaultCommandLine();
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 CommandLine commandLine = new DefaultCommandLine();
 commandLine.addOption(Option.builder("o").longOpt("option").build());
 assertTrue(commandLine.looksLikeOption("o"));
 assertTrue(commandLine.looksLikeOption("option"));
 assertFalse(commandLine.looksLikeOption("value"));
 }
}
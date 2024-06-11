import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private CommandLine commandLine;

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").desc("Test option").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").desc("Test option").valueSeparator(':').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").longOpt("option").desc("Test option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getValues(option.getLongOpt());
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").desc("Test option").build();
 List defaultValues = java.util.Arrays.asList("def1", "def2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues(option);
 assertEquals(0, values.size());
 assertEquals(defaultValues.get(0), commandLine.getValue(option.getLongOpt()));
 assertEquals(defaultValues.get(1), commandLine.getValue(option.getLongOpt()));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").desc("Test option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").desc("Test option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").desc("Test option").build();
 commandLine.setDefaultSwitch(option, true);
 assertFalse(option.isSet());
 assertTrue(option.getFlag());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("prop1", "value1");
 assertEquals("value1", commandLine.getProperty("prop1"));
 }

 @Test
 public void testLooksLikeOption() {
 commandLine.addOption(Option.builder("opt").desc("Test option").build());
 assertTrue(commandLine.looksLikeOption("opt"));
 }

 @Test
 public void testLooksLikeOptionWrong() {
 commandLine.addOption(Option.builder("opt").desc("Test option").build());
 assertFalse(commandLine.looksLikeOption("arg"));
 }

 @Test
 public void testCreateWriteableCommandLine() {
 commandLine = new DefaultWriteableCommandLine();
 assertNull(commandLine.getValue(null));
 assertNull(commandLine.getValues(null));
 assertNull(commandLine.getOptionValues(null));
 assertNull(commandLine.getOption(null));
 assertNull(commandLine.getOption(Option.builder("opt").build().getLongOpt()));
 assertNull(commandLine.getOption(Option.builder("opt").build().getOpt()));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").build()));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").build().aliases("aliases")));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").build().desc("Test option description")));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").valueSeparator(';').build()));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").required(true).build()));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").hasArg(true).build()));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").build().addValues("value1", "value2")));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").build().addValues("value1", "value2").addValues("value3")));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").build().addValues("value1", "value2").addValues("value3").required(true)));
 assertSame(commandLine, commandLine.addOption(Option.builder("opt").build().addValues("value1", "value2").addValues("value3").required(true).hasArg(true)));
 }

}
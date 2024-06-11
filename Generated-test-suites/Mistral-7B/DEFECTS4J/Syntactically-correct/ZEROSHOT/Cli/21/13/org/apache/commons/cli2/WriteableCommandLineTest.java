import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(option, commandLine.getOptions().get(0));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getValuesForOption("o");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 List<String> defaults = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaults);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(0, values.size());
 List<String> defaultsValues = commandLine.getValuesForOption("o");
 assertEquals(2, defaultsValues.size());
 assertEquals("default1", defaultsValues.get(0));
 assertEquals("default2", defaultsValues.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.getValues().isEmpty());
 assertTrue(option.isPresent());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchDuplicate() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.isPresent());
 assertTrue(option.getValue() == Boolean.TRUE);
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value1");
 List<String> values = commandLine.getValuesForOption("o");
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddPropertyDefault() {
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addProperty("property", "value1");
 List<String> values = commandLine.getValuesForOption("property");
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("value"));
 }

 @Test
 public void testGetCurrentOption() {
 Option option1 = Option.builder("o1").longOpt("option1").build();
 Option option2 = Option.builder("o2").longOpt("option2").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 Mockito.when(commandLine.getCurrentOption()).thenReturn(option1);
 Option currentOption = commandLine.getCurrentOption();
 assertEquals(option1, currentOption);
 }

 @Test
 public void testSetCurrentOption() {
 Option option1 = Option.builder("o1").longOpt("option1").build();
 Option option2 = Option.builder("o2").longOpt("option2").build();
 WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.setCurrentOption(option2);
 Option currentOption = commandLine.getCurrentOption();
 assertEquals(option2, currentOption);
 }
}
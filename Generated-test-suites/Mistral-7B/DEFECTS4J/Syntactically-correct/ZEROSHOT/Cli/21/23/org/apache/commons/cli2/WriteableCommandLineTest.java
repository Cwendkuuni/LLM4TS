import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setup() {
 commandLine = Mockito.mock(WriteableCommandLine.class);
 option = new Option("o", "option", true, "Option description");
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 List<Object> values = new ArrayList<>();
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 values.add("value1");
 values.add("value2");
 assertEquals(values, commandLine.getUndefaultedValues(option));
 }

 @Test
 public void testGetUndefaultedValuesWhenNoValues() {
 assertEquals(new ArrayList<Object>(), commandLine.getUndefaultedValues(option));
 }

 @Test
 public void testSetDefaultValues() {
 List<Object> defaultValues = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(defaultValues, commandLine.getUndefaultedValues(option));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 assertEquals(true, option.getValue().get());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 assertEquals(true, option.getValue().get());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty(option, "property", "propertyValue");
 assertEquals("propertyValue", commandLine.getProperty(option.getLongOpt(), "property"));
 }

 @Test
 public void testAddPropertyToDefaultProperties() {
 commandLine.addProperty("defaultProperty", "defaultPropertyValue");
 assertEquals("defaultPropertyValue", commandLine.getProperty("defaultProperty"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(commandLine.looksLikeOption("argument"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 Option optionWithShortArg = new Option("a", "argument", false, "Short argument option");
 commandLine.addOption(optionWithShortArg);
 assertTrue(commandLine.looksLikeOption("a"));
 }

 @Test
 public void testGetCurrentOptionIsNull() {
 assertNull(commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 Option option = new Option("o", "option", true, "Option description");
 commandLine.setCurrentOption(option);
 assertSame(option, commandLine.getCurrentOption());
 }
}
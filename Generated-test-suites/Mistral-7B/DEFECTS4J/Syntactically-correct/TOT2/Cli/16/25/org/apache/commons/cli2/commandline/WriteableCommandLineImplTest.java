package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Before
 public void setUp() {
 Optional<Option> option1 = Option.builder("o").longOpt("option1")
 .required(false)
 .valueSeparator('=')
 .build();
 Option option2 = Option.builder("e").longOpt("option2")
 .required(true)
 .build();
 Option option3 = Option.builder("s").longOpt("option3")
 .required(false)
 .withDefaultValue("defaultValue")
 .build();
 Option option4 = Option.builder("p").longOpt("option4")
 .required(false)
 .values(new String[]{"value1", "value2"})
 .build();
 List<Option> options = new ArrayList<>();
 options.add(option1);
 options.add(option2);
 options.add(option3);
 options.add(option4);
 commandLine = new WriteableCommandLineImpl(option1.get(), Arrays.asList("arg1", "arg2", "arg3"));
 commandLine.addOptions(options);
 commandLine.addProperty(option1.get(), "property1", "propertyValue1");
 commandLine.addProperty(option3.get(), "property2", "propertyValue2");
 commandLine.addSwitch(option1.get(), true);
 commandLine.addValue(option2.get(), "value");
 commandLine.addValue(option3.get(), "overriddenValue");
 commandLine.addSwitch(option3.get(), false);
 commandLine.addProperty(option4.get(), "property3", "propertyValue3");
 commandLine.addValue(option4.get(), Arrays.asList("value3", "value4"));
 }

 @Test
 public void testAddOption() {
 Option option = commandLine.getOption("o");
 assertTrue(commandLine.hasOption(option));
 assertEquals("option1", option.getLongOpt());
 assertEquals(false, option.isRequired());
 assertEquals("value", option.getValueDescription());
 assertEquals("propertyValue1", commandLine.getProperty("property1"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine.addOption(option1.get());
 commandLine.addOption(option1.get());
 }

 @Test
 public void testAddValue() {
 Option option = commandLine.getOption("o");
 List values = commandLine.getValues(option, null);
 assertEquals("value", values.get(0));
 }

 @Test
 public void testAddValueWithDefaultValue() {
 Option option = commandLine.getOption("s");
 List values = commandLine.getValues(option, null);
 assertTrue(values.isEmpty());
 assertEquals("defaultValue", commandLine.getSwitch(option, null));
 }

 @Test
 public void testAddValueWithProvidedValue() {
 Option option = commandLine.getOption("s");
 commandLine.addValue(option, "newValue");
 List values = commandLine.getValues(option, null);
 assertEquals("newValue", values.get(0));
 assertEquals(false, commandLine.getSwitch(option, null));
 }

 @Test
 public void testAddSwitch() {
 Option option = commandLine.getOption("o");
 assertTrue(commandLine.getSwitch(option, null) == null);
 commandLine.addSwitch(option, true);
 assertEquals(true, commandLine.getSwitch(option, null));
 }

 @Test
 public void testGetValues() {
 Option option = commandLine.getOption("o");
 List values = commandLine.getValues(option, null);
 List defaultValues = commandLine.getDefaultValues(option);
 assertEquals(1, values.size());
 assertEquals("value", values.get(0));
 assertEquals(Arrays.asList("value"), values);
 assertEquals(null, defaultValues);
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = commandLine.getOption("o");
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("value", values.get(0));
 }

 @Test
 public void testGetProperty() {
 Option option1 = commandLine.getOption("o");
 Option option3 = commandLine.getOption("p");
 assertEquals("propertyValue1", commandLine.getProperty(option1, "defaultValue"));
 assertEquals("propertyValue2", commandLine.getProperty(option3, "defaultValue3"));
 }

 @Test
 public void testGetProperties() {
 Option option1 = commandLine.getOption("o");
 Option option3 = commandLine.getOption("p");
 Set properties = commandLine.getProperties(option1);
 assertEquals(1, properties.size());
 assertTrue(properties.contains("property1"));
 Set properties2 = commandLine.getProperties(option3);
 assertEquals(1, properties2.size());
 assertTrue(properties2.contains("property3"));
 }

 @Test
 public void testToString() {
 assertEquals("-o option1 --option1 propertyValue1 -s defaultValue --p propertyValue3 value3 value4 arg1 arg2 arg3", commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 List options = commandLine.getOptions();
 assertEquals(4, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(4, optionTriggers.size());
 assertTrue(optionTriggers.containsAll(Arrays.asList("o", "e", "s", "p")));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = commandLine.getOption("o");
 List defaultValues = new ArrayList<>();
 defaultValues.add("default1");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("value", values.get(0));
 assertEquals(Arrays.asList("default1"), commandLine.getDefaultValues(option));
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = commandLine.getOption("s");
 commandLine.setDefaultSwitch(option, true);
 assertEquals(true, commandLine.getSwitch(option, null));
 commandLine.setDefaultSwitch(option, false);
 assertEquals(false, commandLine.getSwitch(option, null));
 }

 @Test
 public void testGetNormalised() {
 List normalised = commandLine.getNormalised();
 assertEquals(4, normalised.size());
 assertEquals("arg1", normalised.get(0));
 assertEquals("arg2", normalised.get(1));
 assertEquals("arg3", normalised.get(2));
 }
}
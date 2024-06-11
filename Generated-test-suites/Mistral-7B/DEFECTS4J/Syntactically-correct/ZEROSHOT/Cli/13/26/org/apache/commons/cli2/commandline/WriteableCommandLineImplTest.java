package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl writeableCommandLine;
 private Option option;
 private Argument argument;

 @Before
 public void setup() {
 optSetup();
 argSetup();
 writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
 }

 @BeforeClass
 public static void optSetup() {
 option = new Option("o", "output", true, "Output file path.");
 option.setRequired(false);
 option.setLongOpt("outputFile");
 }

 @Before
 public void argSetup() {
 argument = new Argument("a", "arg");
 argument.setMetavar("ARG");
 }

 @Test
 public void testAddOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "test.txt");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchAlreadySet() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 writeableCommandLine.addSwitch(option, false);
 }

 @Test
 public void testHasOption() {
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 writeableCommandLine.addOption(option);
 Option opt = writeableCommandLine.getOption("output");
 assertEquals(option, opt);
 }

 @Test
 public void testGetValues() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "test.txt");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));

 writeableCommandLine.addValue(option, "output2.txt");
 List values2 = writeableCommandLine.getValues(option, null);
 assertEquals(2, values2.size());
 assertEquals("test.txt", values.get(0));
 assertEquals("output2.txt", values2.get(1));
 }

 @Test
 public void testGetValuesWithDefaultValues() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.setDefaultValues(option, new ArrayList());
 writeableCommandLine.addValue(option, "test.txt");
 List values = writeableCommandLine.getValues(option, new ArrayList());
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));
 }

 @Test
 public void testGetValuesWithNoDefaultValues() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "test.txt");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));
 writeableCommandLine.addValue(option, "output2.txt");
 List values2 = writeableCommandLine.getValues(option, null);
 assertEquals(1, values2.size());
 assertEquals("test.txt", values2.get(0));
 }

 @Test
 public void testGetSwitch() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetSwitchWithDefaultSwitch() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.setDefaultSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetSwitchWithNoDefaultSwitch() {
 writeableCommandLine.addOption(option);
 assertFalse(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testAddProperty() {
 writeableCommandLine.addProperty("prop", "value");
 assertEquals("value", writeableCommandLine.getProperty("prop", "default"));
 }

 @Test
 public void testAddPropertyWithDefaultValue() {
 writeableCommandLine.addProperty("prop", "default");
 assertEquals("default", writeableCommandLine.getProperty("prop", ""));
 }

 @Test
 public void testGetProperties() {
 Properties props = new Properties();
 props.setProperty("prop", "value");
 writeableCommandLine.addProperty("prop", "value");
 Set set = writeableCommandLine.getProperties();
 assertEquals(1, set.size());
 assertTrue(set.contains("prop"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(writeableCommandLine.looksLikeOption("o"));
 assertFalse(writeableCommandLine.looksLikeOption("x"));
 }

 @Test
 public void testToString() {
 String expected = "o output arg1 arg2";
 assertEquals(expected, writeableCommandLine.toString());
 }

 @Test
 public void testGetOptions() {
 List options = writeableCommandLine.getOptions();
 assertEquals(1, options.size());
 assertEquals(option, options.get(0));
 }

 @Test
 public void testGetOptionTriggers() {
 Set triggers = writeableCommandLine.getOptionTriggers();
 assertEquals(1, triggers.size());
 assertTrue(triggers.contains("output"));
 }

 @Test
 public void testSetDefaultValues() {
 writeableCommandLine.setDefaultValues(option, new ArrayList());
 writeableCommandLine.addValue(option, "test.txt");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));
 List defaults = writeableCommandLine.getDefaultValues(option);
 assertEquals(0, defaults.size());
 }

 @Test
 public void testSetDefaultSwitch() {
 writeableCommandLine.setDefaultSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetNormalised() {
 List normalised = writeableCommandLine.getNormalised();
 assertEquals(0, normalised.size());
 }
}
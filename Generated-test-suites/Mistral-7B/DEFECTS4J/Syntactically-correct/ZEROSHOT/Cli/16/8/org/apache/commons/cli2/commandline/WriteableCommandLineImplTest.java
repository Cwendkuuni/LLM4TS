import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 option = Mockito.mock(Option.class);
 commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value1");
 List values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 commandLine.addOption(option);
 Option result = commandLine.getOption("trigger");
 assertNull(result);
 Option result1 = commandLine.getOption(option.getPreferredName());
 assertEquals(option, result1);
 }

 @Test
 public void testGetValues() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine.addValue(option, "value1");
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetProperty() {
 Properties properties = new Properties();
 properties.setProperty("property1", "value1");
 commandLine.addProperty(option, new PropertyOption(), "property1", properties);
 assertEquals("value1", commandLine.getProperty(option, "property1", null));
 }

 @Test
 public void testAddProperty() {
 Properties properties = new Properties();
 properties.setProperty("property1", "value1");
 commandLine.addProperty(option, "property1", "value2");
 assertEquals("value2", commandLine.getProperty(option, "property1", null));
 }

 @Test
 public void testGetProperties() {
 Properties properties = new Properties();
 properties.setProperty("property1", "value1");
 commandLine.addProperty(option, new PropertyOption(), properties);
 Set propertiesSet = commandLine.getProperties(option);
 assertEquals(1, propertiesSet.size());
 assertTrue(propertiesSet.contains("property1"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("option"));
 assertFalse(commandLine.looksLikeOption("other"));
 }

 @Test
 public void testToString() {
 List normalised = new ArrayList();
 normalised.add("option");
 normalised.add("arg1");
 normalised.add("arg2");
 commandLine.setNormalised(normalised);
 assertEquals("option arg1 arg2", commandLine.toString());
 }
}
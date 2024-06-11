// WriteableCommandLineImplTest.java
package org.junit.platform.commons.testing.generator.junit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.cli2.commandline.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class WriteableCommandLineImplTest {
  private WriteableCommandLineImpl writeableCommandLine;
  private Option option;

  @Before
  public void setUp() {
    option = new Option("example", true, false, "Example");
    writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
  }

  @Test
  public void testHasOption() {
    assertTrue(writeableCommandLine.hasOption(option));
  }

  // Test with a null Option argument
  @Test(expected = IllegalArgumentException.class)
  public void testHasOptionWithNullOption() {
    writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
    writeableCommandLine.hasOption(option);
  }

  // Test with an empty command line (List arguments)
  @Test
  public void testHasOptionWithEmptyCommandLine() {
    writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
    assertFalse(writeableCommandLine.hasOption(option));
  }

  // Test with an Option that is not present in the command line
  @Test
  public void testHasOptionWithNonPresentOption() {
    Option otherOption = new Option("other", false, false, "Other");
    writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
    assertFalse(writeableCommandLine.hasOption(otherOption));
  }

  // Test addOption method
  @Test
  public void testAddOption() {
    writeableCommandLine.addOption(option);
    assertTrue(writeableCommandLine.hasOption(option));
  }

  // Test addValue method
  @Test
  public void testAddValue() {
    List values = new ArrayList<>();
    values.add("value1");
    writeableCommandLine.addValue(option, values);
    assertEquals(values, writeableCommandLine.getValues(option, null));
  }

  // Test addSwitch method
  @Test
  public void testAddSwitch() {
    writeableCommandLine.addSwitch(option, true);
    assertTrue(writeableCommandLine.getSwitch(option, false));
  }

  // Test getValues method
  @Test
  public void testGetValues() {
    writeableCommandLine.addValue(option, "value1");
    List values = writeableCommandLine.getValues(option, null);
    assertEquals(Arrays.asList("value1"), values);
  }

  // Test getSwitch method
  @Test
  public void testGetSwitch() {
    writeableCommandLine.addSwitch(option, true);
    assertTrue(writeableCommandLine.getSwitch(option, false));
  }

  // Test addProperty method
  @Test
  public void testAddProperty() {
    writeableCommandLine.addProperty("property", "value");
    assertEquals("value", writeableCommandLine.getProperty("property", ""));
  }

  // Test getProperty method
  @Test
  public void testGetProperty() {
    writeableCommandLine.addProperty("property", "value");
    assertEquals("value", writeableCommandLine.getProperty("property", "defaultValue"));
  }

  // Test getProperties method
  @Test
  public void testGetProperties() {
    writeableCommandLine.addProperty("property1", "value1");
    writeableCommandLine.addProperty("property2", "value2");
    Set<String> properties = writeableCommandLine.getProperties();
    assertEquals(2, properties.size());
    assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));
  }

  // Test looksLikeOption method
  @Test
  public void testLooksLikeOption() {
    assertTrue(writeableCommandLine.looksLikeOption("example"));
  }

  // Test toString method
  @Test
  public void testToString() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.addValue(option, "value1");
    writeableCommandLine.addProperty("property", "value");
    String toString = writeableCommandLine.toString();
    String expected = String.format("%s %s=%s %s=%s", option.getLongOpt(), option.getValue(), "value1", option.getLongOpt(), "property");
    assertEquals(expected, toString);
  }

  // Test getOptions method
  @Test
  public void testGetOptions() {
    writeableCommandLine.addOption(option);
    List<Option> options = writeableCommandLine.getOptions();
    assertEquals(1, options.size());
    assertEquals(option, options.get(0));
  }

  // Test getOptionTriggers method
  @Test
  public void testGetOptionTriggers() {
    writeableCommandLine.addOption(option);
    Option otherOption = new Option("other", false, false, "Other");
    writeableCommandLine.addOption(otherOption);
    Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();
    assertEquals(2, optionTriggers.size());
    assertTrue(optionTriggers.containsAll(Arrays.asList(option.getLongOpt(), otherOption.getLongOpt())));
  }

  // Test setDefaultValues method
  @Test
  public void testSetDefaultValues() {
    writeableCommandLine.addOption(option);
    List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
    writeableCommandLine.setDefaultValues(option, defaultValues);
    List<String> values = writeableCommandLine.getValues(option, null);
    assertEquals(2, values.size());
    assertTrue(values.containsAll(defaultValues));
  }

  // Test setDefaultSwitch method
  @Test
  public void testSetDefaultSwitch() {
    writeableCommandLine.addOption(option);
    writeableCommandLine.setDefaultSwitch(option, true);
    assertTrue(writeableCommandLine.getSwitch(option, false));
  }

  // Test getNormalised method
  @Test
  public void testGetNormalised() {
    writeableCommandLine.addOption(option);
    String arg = "argument";
    writeableCommandLine.addValue(option, arg);
    List<String> normalised = writeableCommandLine.getNormalised();
    assertEquals(2, normalised.size());
    assertTrue(normalised.contains(arg));
  }
}
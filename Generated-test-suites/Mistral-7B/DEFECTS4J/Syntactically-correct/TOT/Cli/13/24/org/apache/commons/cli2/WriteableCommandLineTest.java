import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new DefaultWriteableCommandLine();
 }

 @Test
 public void testAddOptionAndValue() {
 // Given
 Option option = Option.builder("option").longOpt("longOption")
 .valueSeparator('=')
 .hasArg(true)
 .build();

 // When
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");

 // Then
 List values = commandLine.getValuesForOption("option");
 assertEquals("testValue", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 // Given
 Option option = Option.builder("option").longOpt("longOption")
 .isRequired(false)
 .build();

 // When
 commandLine.addSwitch(option, true);

 // Then
 boolean value = commandLine.getValueForOption("option");
 assertTrue(value);
 }

 @Test
 public void testLooksLikeOption() {
 // Given

 // When
 String argument = "--option";

 // Then
 boolean result = commandLine.looksLikeOption(argument);
 assertTrue(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOption() {
 // Given
 Option option = null;

 // When & Then
 commandLine.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNullOption() {
 // Given
 Option option = new Option("option", "opt");
 commandLine = new DefaultWriteableCommandLine();

 // When & Then
 commandLine.addValue(null, "value");
 }

 @Test
 public void testAddProperty() {
 // Given

 // When
 commandLine.addProperty("property", "value");

 // Then
 String propertyValue = commandLine.getProperty("property");
 assertEquals("value", propertyValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyWithNullPropertyName() {
 // Given

 // When & Then
 commandLine.addProperty(null, "value");
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 // Given
 Option option = Option.builder("option").build();
 commandLine.parse(new String[]{"--invalidOption"});
 }
}
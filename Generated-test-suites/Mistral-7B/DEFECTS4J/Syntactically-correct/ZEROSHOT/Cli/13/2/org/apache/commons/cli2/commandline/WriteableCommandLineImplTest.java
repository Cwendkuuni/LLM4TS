import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testCreateWriteableCommandLineImpl() {
 // Given
 Option rootOption = new Option("opt", "option", true, "Option description");
 List arguments = new ArrayList();
 arguments.add("--opt");
 arguments.add("value1");
 arguments.add("--opt2");

 // When
 commandLine = new WriteableCommandLineImpl(rootOption, arguments);

 // Then
 assertEquals("option", commandLine.getRootOption().getShortName());
 assertEquals("Option description", commandLine.getRootOption().getLongOpt().getValue());
 assertEquals(2, commandLine.getOptions().size());
 }

 @Test
 public void testAddOption() {
 // Given
 Option option = new Option("t", "test", false, "Test option");
 commandLine = new WriteableCommandLineImpl(null, null);

 // When
 commandLine.addOption(option);

 // Then
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testAddValue() {
 // Given
 Option option = new Option("v", "value", true, "Value option");
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addOption(option);

 // When
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 // Then
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 // Given
 Option option = new Option("s", "switch", false, "Switch option");
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addOption(option);

 // When
 commandLine.addSwitch(option, true);

 // Then
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 // Given
 Option option = new Option("h", "help", false, "Help option");
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addOption(option);

 // When
 // Then
 assertTrue(commandLine.hasOption(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testSwitchAlreadySet() {
 // Given
 Option option = new Option("h", "help", false, "Help option");
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);

 // Then
 // This test should fail with IllegalStateException
 }

 @Test
 public void testgetProperty() {
 // Given
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addProperty("prop", "property value");

 // When
 // Then
 assertEquals("property value", commandLine.getProperty("prop", "default value"));
 }

 @Test
 public void testGetProperties() {
 // Given
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addProperty("prop1", "property value1");
 commandLine.addProperty("prop2", "property value2");

 // When
 // Then
 Set properties = commandLine.getProperties();
 assertEquals(2, properties.size());
 }

 @Test
 public void testToString() {
 // Given
 Option option = new Option("t", "test", false, "Test option");
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 List arguments = new ArrayList();
 arguments.add("arg1");
 arguments.add("arg2");
 commandLine.setNormalised(arguments);

 // When
 // Then
 assertEquals("--test --switch arg1 arg2", commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 // Given
 Option option1 = new Option("o", "option1", true, "Option 1");
 Option option2 = new Option("o", "option2", true, "Option 2");
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addOption(option1);
 commandLine.addOption(option2);

 // When
 // Then
 List options = commandLine.getOptions();
 assertEquals(2, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 }

 @Test
 public void testGetOptionTriggers() {
 // Given
 Option option1 = new Option("o", "option1", true, "Option 1");
 Option option2 = new Option("o", "option2", true, "Option 2");
 commandLine = new WriteableCommandLineImpl(null, null);
 commandLine.addOption(option1);
 commandLine.addOption(option2);

 // When
 // Then
 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(2, optionTriggers.size());
 assertTrue(optionTriggers.contains("o"));
 }
}
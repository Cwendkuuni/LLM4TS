import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option(null, "o", false, "Option with no arg");
 assertEquals("o", option.getOpt());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertEquals(-1, option.getArgs());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertNotNull(option.toString());
 }

 @Test
 public void testOptionCreationWithArg() {
 Option option = new Option(null, "o", true, "Option with arg");
 assertEquals("o", option.getOpt());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.hasArg());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertEquals(1, option.getArgs());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertNotNull(option.toString());
 }

 @Test
 public void testOptionCreationWithLongOpt() {
 Option option = new Option(null, "longOpt", false, "Option with long opt");
 assertEquals(null, option.getOpt());
 assertEquals("longOpt", option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertEquals(-1, option.getArgs());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertNotNull(option.toString());
 }

 @Test
 public void testOptionCreationWithLongOptAndArg() {
 Option option = new Option("o", "longOpt", true, "Option with long opt and arg");
 assertEquals("o", option.getOpt());
 assertEquals("longOpt", option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.hasArg());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertEquals(1, option.getArgs());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertNotNull(option.toString());
 }

 @Test
 public void testOptionGettersAndSetters() {
 Option option = new Option("o", null, false, "Option with getters and setters");
 option.setDescription("Description for option o");
 assertEquals("Description for option o", option.getDescription());
 option.setArgName("argName");
 assertEquals("argName", option.getArgName());
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 option.setRequired(true);
 assertTrue(option.isRequired());
 option.setLongOpt("longOpt");
 assertEquals("longOpt", option.getLongOpt());
 option.setType(String.class);
 assertEquals(String.class, option.getType());
 }

 @Test
 public void testOptionGetId() {
 Option option = new Option("o", null, false, "Option with getId");
 assertEquals('o', option.getId());
 }

 @Test
 public void testOptionKey() {
 Option option = new Option("o", null, false, "Option with key");
 assertEquals("o", option.getKey());

 Option optionWithLongOpt = new Option(null, "longOpt", false, "Option with long opt");
 assertEquals("longOpt", optionWithLongOpt.getKey());
 }

 @Test
 public void testOptionHasArg() {
 Option optionWithNoArg = new Option("o", null, false, "Option with no arg");
 assertFalse(optionWithNoArg.hasArg());

 Option optionWithOneArg = new Option("o", null, true, "Option with arg");
 assertTrue(optionWithOneArg.hasArg());
 }

 @Test
 public void testOptionHasOptionalArg() {
 Option optionWithoutOptionalArg = new Option("o", null, false, "Option without optional arg");
 assertFalse(optionWithoutOptionalArg.hasOptionalArg());

 Option optionWithOptionalArg = new Option("o", null, true, "Option with optional arg");
 assertTrue(optionWithOptionalArg.hasOptionalArg());
 }

 @Test
 public void testOptionHasLongOpt() {
 Option optionWithoutLongOpt = new Option("o", null, false, "Option without long opt");
 assertFalse(optionWithoutLongOpt.hasLongOpt());

 Option optionWithLongOpt = new Option(null, "longOpt", false, "Option with long opt");
 assertTrue(optionWithLongOpt.hasLongOpt());
 }

 @Test
 public void testOptionHasDescription() {
 Option optionWithoutDescription = new Option("o", null, false, "Option without description");
 assertNull(optionWithoutDescription.getDescription());

 Option optionWithDescription = new Option("o", null, false, "Option with description");
 assertEquals("Option with description", optionWithDescription.getDescription());
 }

 @Test
 public void testOptionIsRequired() {
 Option optionNotRequired = new Option("o", null, false, "Option not required");
 assertFalse(optionNotRequired.isRequired());

 Option optionRequired = new Option("o", null, false, "Option required");
 assertTrue(optionRequired.isRequired());
 }

 @Test
 public void testOptionArgName() {
 Option optionWithoutArgName = new Option("o", null, false, "Option without arg name");
 assertNull(optionWithoutArgName.getArgName());

 Option optionWithArgName = new Option("o", null, false, "Option with arg name");
 assertEquals("arg", optionWithArgName.getArgName());
 }

 @Test
 public void testOptionHasArgs() {
 Option optionWithoutArgs = new Option("o", null, false, "Option without args");
 assertFalse(optionWithoutArgs.hasArgs());

 Option optionWithArgs = new Option("o", null, false, "Option with args");
 assertTrue(optionWithArgs.hasArgs());
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueWithNoArgs() {
 Option option = new Option("o", null, false, "Option without args");
 option.addValueForProcessing("value");
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueWithMoreValuesThanArgs() {
 Option option = new Option("o", null, 2, "Option with 2 args");
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 option.addValueForProcessing("value3");
 }

 @Test
 public void testAddValueWithOneValueAndOneArg() {
 Option option = new Option("o", null, true, "Option with 1 arg");
 option.addValueForProcessing("value");
 assertEquals("value", option.getValue());
 }

 @Test
 public void testAddValueWithOneValueAndMoreArgs() {
 Option option = new Option("o", null, 2, "Option with 2 args");
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 String[] expected = new String[]{"value1", "value2"};
 assertArrayEquals(expected, option.getValues());
 }

 @Test
 public void testAddValueWithValueSeparator() {
 Option option = new Option("o", null, 2, "Option with 2 args and value separator");
 option.setValueSeparator(';');
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 String[] expected = new String[]{"value1", "value2"};
 assertArrayEquals(expected, option.getValues());
 }
}
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @BeforeEach
 public void setUp() {
 commandLine = new CmdLineParser().parse("--optionA valueA --optionB valueB");
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("optionC").build();
 commandLine.addOption(option);
 Assertions.assertEquals(3, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("optionA").hasArg(true).build();
 commandLine.addValue(option, "valueC");
 List<String> values = commandLine.getOptionValues("optionA");
 Assertions.assertEquals(2, values.size());
 Assertions.assertEquals("valueC", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("optionB").hasArg(true).build();
 List<String> values = commandLine.getUndefaultedValues(option);
 Assertions.assertEquals(1, values.size());
 Assertions.assertEquals("valueB", values.get(0));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("optionA").hasArg(true).build();
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("defaultValueA1");
 defaultValues.add("defaultValueA2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues("optionA");
 Assertions.assertEquals(3, values.size());
 Assertions.assertEquals("valueA", values.get(0));
 Assertions.assertEquals("valueC", values.get(1));
 Assertions.assertEquals("defaultValueA1", values.get(2));
 Assertions.assertEquals("defaultValueA2", values.get(3));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("optionA").build();
 commandLine.addSwitch(option, true);
 Assertions.assertTrue(option.isSet());
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("optionA").build();
 commandLine.setDefaultSwitch(option, true);
 Assertions.assertTrue(option.isSet());
 Assertions.assertEquals(true, option.getValues().get(0));
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("optionA").build();
 commandLine.addProperty(option, "property", "valueA");
 Assertions.assertEquals("valueA", commandLine.getOptionProperty(option, "property"));
 }

 @Test
 public void testAddPropertyToDefaultSet() {
 commandLine.addProperty("property", "defaultValue");
 Assertions.assertEquals("defaultValue", commandLine.getOptionProperty("optionA", "property"));
 }

 @Test
 public void testLooksLikeOption() {
 Assertions.assertTrue(commandLine.looksLikeOption("--optionA"));
 Assertions.assertFalse(commandLine.looksLikeOption("valueA"));
 }

 @Test
 public void testGetCurrentOption() {
 Option option = Option.builder("optionA").hasArg(true).build();
 commandLine.parseNext();
 commandLine.parseNext();
 commandLine.parseNext();
 commandLine.parseNext();
 commandLine.parseNext();
 Assertions.assertEquals(option, commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 Option option = Option.builder("optionX").build();
 commandLine.setCurrentOption(option);
 Assertions.assertEquals(option, commandLine.getCurrentOption());
 }
}
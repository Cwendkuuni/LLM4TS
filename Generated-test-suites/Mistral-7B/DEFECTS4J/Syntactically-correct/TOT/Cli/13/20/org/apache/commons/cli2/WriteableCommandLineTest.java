import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @BeforeEach
 public void setup() {
 commandLine = new Cli2DefaultParser().parse(new Option[]{}, new String[0]);
 }

 @Test
 void testAddOption() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .numberOfValues(1)
 .required(false)
 .build();
 commandLine.addOption(option);
 Assertions.assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 void testAddValue() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .numberOfValues(1)
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 Assertions.assertEquals("value", commandLine.getOptions().get(0).getValue());
 }

 @Test
 void testGetValues() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .numberOfValues(1)
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("o");
 Assertions.assertEquals(2, values.size());
 Assertions.assertEquals("value1", values.get(0));
 Assertions.assertEquals("value2", values.get(1));
 }

 @Test
 void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option")
 .valueSeparator('=')
 .numberOfValues(1)
 .required(false)
 .build();
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 Assertions.assertEquals(2, commandLine.getOptions().get(0).getDefaultValues().size());
 Assertions.assertEquals("defaultValue1", commandLine.getOptions().get(0).getDefaultValues().get(0));
 Assertions.assertEquals("defaultValue2", commandLine.getOptions().get(0).getDefaultValues().get(1));
 }

 @Test
 void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option")
 .numberOfValues(0)
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 Assertions.assertEquals(true, option.getValue());
 }

 @Test
 void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option")
 .numberOfValues(0)
 .required(false)
 .build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 Assertions.assertEquals(true, option.getDefaultValue());
 }

 @Test
 void testAddProperty() {
 commandLine.addProperty("property", "defaultValue");
 Assertions.assertEquals("defaultValue", commandLine.getProperty("property"));
 }

 @Test
 void testLooksLikeOption() {
 Assertions.assertTrue(commandLine.looksLikeOption("--option"));
 Assertions.assertFalse(commandLine.looksLikeOption("argument"));
 }
}
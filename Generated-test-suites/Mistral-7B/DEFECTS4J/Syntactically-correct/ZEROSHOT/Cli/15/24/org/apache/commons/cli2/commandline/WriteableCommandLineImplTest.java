import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(
 new Option.Builder("testOption").build(), Collections.emptyList());

 // test adding option
 Option option = new Option.Builder("testOption").build();
 commandLine.addOption(option);

 // test getting option
 Option getOption = commandLine.getOption("testOption");
 assertTrue(getOption.equals(option));

 // test hasOption
 assertTrue(commandLine.hasOption(option));

 // test getValues
 List values = commandLine.getValues(option, null);
 assertNull(values);

 // test getUndefaultedValues
 List unfaultedValues = commandLine.getUndefaultedValues(option);
 assertNull(unfaultedValues);

 // test getSwitch
 Boolean switchValue = commandLine.getSwitch(option, false);
 assertFalse(switchValue);

 // test getProperty
 String propertyValue = commandLine.getProperty("testProperty", "defaultValue");
 assertNull(propertyValue);

 // test addProperty
 commandLine.addProperty(option, "testProperty", "testValue");
 Properties properties = commandLine.getProperties(option);
 assertTrue(properties.containsKey("testProperty"));
 assertEquals("testValue", properties.getProperty("testProperty"));

 // test toString
 assertEquals("testOption", commandLine.toString());

 // test getOptions
 List options = commandLine.getOptions();
 assertTrue(options.isEmpty());

 // test getOptionTriggers
 Set optionTriggers = commandLine.getOptionTriggers();
 assertTrue(optionTriggers.isEmpty());

 // test setDefaultValues
 List defaultValues = new ArrayList();
 defaultValues.add("defaultValue1");
 commandLine.setDefaultValues(option, defaultValues);
 List getDefaultValues = commandLine.getValues(option, null);
 assertTrue(getDefaultValues.contains("defaultValue1"));

 // test setDefaultSwitch
 commandLine.setDefaultSwitch(option, true);
 Boolean defaultSwitch = commandLine.getSwitch(option, false);
 assertTrue(defaultSwitch);

 // test getNormalised
 List normalised = commandLine.getNormalised();
 assertTrue(normalised.isEmpty());
 }
}
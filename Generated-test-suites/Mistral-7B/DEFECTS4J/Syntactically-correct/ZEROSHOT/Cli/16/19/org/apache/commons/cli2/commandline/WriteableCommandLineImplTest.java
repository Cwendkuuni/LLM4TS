import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, new ArrayList<>());

 assertNull(cmd.getOption(null));
 assertNull(cmd.getValues(null, null));
 assertNull(cmd.getSwitch(null, false));
 assertNull(cmd.getProperty(null));
 assertNull(cmd.getProperty(null, null));
 assertSame(Collections.EMPTY_LIST, cmd.getProperties());
 assertSame(Collections.EMPTY_SET, cmd.getOptionTriggers());
 assertSame(Collections.EMPTY_LIST, cmd.getOptions());
 assertSame(Collections.EMPTY_LIST, cmd.getNormalised());
 }

 @Test
 public void testAddOption() {
 Option option = new Option("t", "test", false, "Test option");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList<>());

 cmd.addOption(option);

 Option opt = cmd.getOption("t");
 assertTrue(cmd.hasOption(option));
 assertSame(option, opt);
 }

 @Test
 public void testAddValue() {
 Option argOption = new Option("a", "arg", true, "An argument");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(argOption, new ArrayList<>());

 cmd.addOption(argOption);

 cmd.addValue(argOption, "test");

 List values = cmd.getValues(argOption, null);
 assertTrue(values.contains("test"));
 }

 @Test
 public void testAddSwitch() {
 Option switchOption = new Option("s", "switch", false, "A switch");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(switchOption, new ArrayList<>());

 cmd.addOption(switchOption);

 cmd.addSwitch(switchOption, true);

 Boolean switchValue = cmd.getSwitch(switchOption, false);
 assertTrue(switchValue);
 }

 @Test
 public void testHasOption() {
 Option option = new Option("t", "test", false, "Test option");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList<>());

 cmd.addOption(option);

 assertTrue(cmd.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("t", "test", false, "Test option");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList<>());

 cmd.addOption(option);

 Option opt = cmd.getOption("t");
 assertTrue(opt != null);
 assertSame(option, opt);
 }

 @Test
 public void testGetValues() {
 Option argOption = new Option("a", "arg", true, "An argument");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(argOption, new ArrayList<>());

 cmd.addOption(argOption);
 cmd.addValue(argOption, "test1");
 cmd.addValue(argOption, "test2");

 List values = cmd.getValues(argOption, null);
 assertTrue(values.size() == 2);
 assertTrue(values.contains("test1"));
 assertTrue(values.contains("test2"));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option argOption = new Option("a", "arg", true, "An argument");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(argOption, new ArrayList<>());

 cmd.addOption(argOption);
 cmd.addValue(argOption, "test1");
 cmd.addValue(argOption, "test2");

 List values = cmd.getUndefaultedValues(argOption);
 assertTrue(values.isEmpty());
 }

 @Test
 public void testGetSwitch() {
 Option switchOption = new Option("s", "switch", false, "A switch");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(switchOption, new ArrayList<>());

 cmd.addOption(switchOption);
 cmd.addSwitch(switchOption, true);

 Boolean switchValue = cmd.getSwitch(switchOption, false);
 assertTrue(switchValue);
 }

 @Test
 public void testGetProperty() {
 Option propertyOption = new Option("p", "property", true, "Property option");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(propertyOption, new ArrayList<>());

 cmd.addOption(propertyOption);
 cmd.addProperty(propertyOption, "property", "value1");
 cmd.addProperty(propertyOption, "property2", "value2");

 String propertyValue = cmd.getProperty(propertyOption, "property");
 assertTrue(propertyValue.equals("value1"));
 }

 @Test
 public void testGetProperties() {
 Option propertyOption = new Option("p", "property", true, "Property option");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(propertyOption, new ArrayList<>());

 cmd.addOption(propertyOption);
 cmd.addProperty(propertyOption, "property1", "value1");
 cmd.addProperty(propertyOption, "property2", "value2");

 Set properties = cmd.getProperties();
 assertTrue(properties.size() == 2);
 assertTrue(properties.contains("property1"));
 assertTrue(properties.contains("property2"));
 }

 @Test
 public void testToString() {
 Option option = new Option("t", "test", false, "Test option");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList<>());

 cmd.addOption(option);
 cmd.addValue(option, "test1");
 cmd.addValue(option, "test2");

 String result = cmd.toString();
 String expected = "test1 test2";
 assertTrue(result.equals(expected));
 }

 @Test
 public void testGetOptions() {
 Option option1 = new Option("t1", "test1", false, "Test option 1");
 Option option2 = new Option("t2", "test2", false, "Test option 2");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, new ArrayList<>());

 cmd.addOption(option1);
 cmd.addOption(option2);

 List options = cmd.getOptions();
 List expected = new ArrayList();
 expected.add(option1);
 expected.add(option2);

 assertTrue(options.equals(expected));
 }

 @Test
 public void testGetOptionTriggers() {
 Option option1 = new Option("t1", "test1", false, "Test option 1");
 Option option2 = new Option("t2", "test2", false, "Test option 2");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, new ArrayList<>());

 cmd.addOption(option1);
 cmd.addOption(option2);

 Set optionTriggers = cmd.getOptionTriggers();
 Set expected = new HashSet();
 expected.add("t1");
 expected.add("t2");

 assertTrue(optionTriggers.equals(expected));
 }

 @Test
 public void testSetDefaultValues() {
 Option argOption = new Option("a", "arg", true, "An argument");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(argOption, new ArrayList<>());

 cmd.addOption(argOption);

 List defaults = new ArrayList();
 defaults.add("test1");
 defaults.add("test2");

 cmd.setDefaultValues(argOption, defaults);

 List values = cmd.getValues(argOption, null);
 assertTrue(values.containsAll(defaults));
 }

 @Test
 public void testSetDefaultSwitch() {
 Option switchOption = new Option("s", "switch", false, "A switch");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(switchOption, new ArrayList<>());

 cmd.addOption(switchOption);

 cmd.setDefaultSwitch(switchOption, true);

 Boolean switchValue = cmd.getSwitch(switchOption, false);
 assertTrue(switchValue);
 }

 @Test
 public void testGetNormalised() {
 Option option = new Option("t", "test", false, "Test option");
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList<>());

 cmd.addOption(option);
 cmd.addValue(option, "test1");
 cmd.addValue(option, "test2");

 List normalised = cmd.getNormalised();
 List expected = new ArrayList();
 expected.add("test test1");
 expected.add("test test2");

 assertTrue(normalised.equals(expected));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, new ArrayList<>());

 assertFalse(cmd.looksLikeOption("test"));
 }

 @Test
 public void testLooksLikeOptionWithPrefix() {
 WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, new ArrayList<>());

 assertFalse(cmd.looksLikeOption("t-test"));
 assertTrue(cmd.looksLikeOption("--test"));
 }
}
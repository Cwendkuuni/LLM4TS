import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("o", "option", false, "This is an option");
 options.addOption("s", "switch", true, "This is a switch");
 commandLine = new WriteableCommandLineImpl(new Option.Builder("root").withOptions(options).build(), Collections.emptyList());
 }

 @Test
 public void testAddOption() {
 Option option = new Option.Builder("testOption").withLongOpt("longTestOption")
 .build();
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testAddValue() {
 commandLine.addOption(options.findOption("option"));
 commandLine.addValue(options.findOption("option"), "value1");
 commandLine.addValue(options.findOption("option"), "value2");
 List values = commandLine.getValues(options.findOption("option"), null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addOption(options.findOption("switch"));
 commandLine.addSwitch(options.findOption("switch"), true);
 assertTrue(commandLine.getSwitch(options.findOption("switch"), false));
 }

 @Test
 public void testHasOption() {
 Option option = new Option.Builder("testOption").build();
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option option = new Option.Builder("testOption").build();
 commandLine.addOption(option);
 Option retrievedOption = commandLine.getOption("testOption");
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetValues() {
 commandLine.addOption(options.findOption("option"));
 commandLine.addValue(options.findOption("option"), "value1");
 commandLine.addValue(options.findOption("option"), "value2");
 List values = commandLine.getValues(options.findOption("option"), null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine.addOption(new Option.Builder("testOption").withValues(new String[]{"value1", "value2"}).build());
 commandLine.addValue(options.findOption("testOption"), "value3");
 List values = commandLine.getUndefaultedValues(options.findOption("testOption"));
 assertEquals(0, values.size());
 }

 @Test
 public void testGetSwitch() {
 commandLine.addOption(options.findOption("switch"));
 commandLine.addSwitch(options.findOption("switch"), true);
 assertTrue(commandLine.getSwitch(options.findOption("switch"), false));
 }

 @Test
 public void testGetProperty() {
 commandLine.addOption(new PropertyOption.Builder("property", "propValue").build());
 commandLine.addProperty(options.findOption("property"), "testValue");
 assertEquals("testValue", commandLine.getProperty(options.findOption("property"), "defaultValue"));
 }

 @Test
 public void testAddProperty() {
 commandLine.addOption(new PropertyOption.Builder("property", "propValue").build());
 commandLine.addProperty(options.findOption("property"), "testValue1");
 commandLine.addProperty(options.findOption("property"), "testValue2");
 Set properties = commandLine.getProperties(options.findOption("property"));
 assertEquals(2, properties.size());
 assertTrue(properties.contains("testValue1"));
 assertTrue(properties.contains("testValue2"));
 }

 @Test
 public void testGetProperties() {
 commandLine.addOption(new PropertyOption.Builder("property1", "propValue1").build());
 commandLine.addOption(new PropertyOption.Builder("property2", "propValue2").build());
 Set properties = commandLine.getProperties();
 assertEquals(2, properties.size());
 assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));
 }

 @Test(expected = IllegalStateException.class)
 public void testSwitchAlreadySet() {
 commandLine.addOption(options.findOption("switch"));
 commandLine.addSwitch(options.findOption("switch"), true);
 commandLine.addSwitch(options.findOption("switch"), false);
 }

 @Test
 public void testToString() {
 CommandLine cl = new DefaultCommandLine();
 cl.addOption(new Option.Builder("test").build());
 cl.addOption(new Option.Builder("test2").build());
 cl.addOption(new Option.builder("test3").longOpt("longTest3").build());
 List args = Arrays.asList("arg1", "arg2", "arg3");
 commandLine = new WriteableCommandLineImpl(cl, args);
 String result = commandLine.toString();
 String expected = "arg1 arg2 arg3 --test --test2 --longTest3";
 assertEquals(expected, result);
 }

 @Test
 public void testGetOptions() {
 CommandLine cl = new DefaultCommandLine();
 cl.addOption(new Option.Builder("test").build());
 cl.addOption(new Option.Builder("test2").build());
 cl.addOption(new Option.builder("test3").longOpt("longTest3").build());
 commandLine = new WriteableCommandLineImpl(cl, Collections.emptyList());
 List options = commandLine.getOptions();
 assertEquals(3, options.size());
 }

 @Test
 public void testSetDefaultValues() {
 commandLine.addOption(options.findOption("option"));
 List defaults = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(options.findOption("option"), defaults);
 List values = commandLine.getValues(options.findOption("option"), null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.addOption(options.findOption("switch"));
 commandLine.setDefaultSwitch(options.findOption("switch"), true);
 assertTrue(commandLine.getSwitch(options.findOption("switch"), false));
 }

 @Test
 public void testGetNormalised() {
 CommandLine cl = new DefaultCommandLine();
 cl.addOption(new Option.Builder("test").build());
 cl.addOption(new Option.Builder("test2").build());
 cl.addOption(new Option.builder("test3").longOpt("longTest3").build());
 commandLine = new WriteableCommandLineImpl(cl, Arrays.asList("arg1", "arg2", "arg3"));
 List normalised = commandLine.getNormalised();
 assertEquals(3, normalised.size());
 }
}
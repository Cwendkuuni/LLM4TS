import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("b", "boolean", false, "a boolean option"));
 Option optionB = commandLine.getOption("b");
 Assert.assertNotNull(optionB);
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", true, "an argument"));
 commandLine.addValue(commandLine.getOption("a"), "value1");
 commandLine.addValue(commandLine.getOption("a"), "value2");
 List values = commandLine.getValues(commandLine.getOption("a"), null);
 Assert.assertEquals(2, values.size());
 Assert.assertEquals("value1", values.get(0));
 Assert.assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", false, "an argument"));
 commandLine.addSwitch(commandLine.getOption("a"), true);
 Boolean switchValue = commandLine.getSwitch(commandLine.getOption("a"), false);
 Assert.assertTrue(switchValue);
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", true, "an argument"));
 boolean hasOption = commandLine.hasOption(commandLine.getOption("a"));
 Assert.assertTrue(hasOption);
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", true, "an argument"));
 Option optionA = commandLine.getOption("a");
 Assert.assertNotNull(optionA);
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", true, "an argument"));
 commandLine.addValue(commandLine.getOption("a"), "value1");
 commandLine.addValue(commandLine.getOption("a"), "value2");
 List values = commandLine.getValues(commandLine.getOption("a"), null);
 Assert.assertEquals(2, values.size());
 Assert.assertEquals("value1", values.get(0));
 Assert.assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", true, "an argument"));
 commandLine.addValue(commandLine.getOption("a"), "value1");
 List values = commandLine.getUndefaultedValues(commandLine.getOption("a"));
 Assert.assertEquals(1, values.size());
 Assert.assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", false, "an argument"));
 commandLine.addSwitch(commandLine.getOption("a"), true);
 Boolean switchValue = commandLine.getSwitch(commandLine.getOption("a"), false);
 Assert.assertTrue(switchValue);
 }

 @Test
 public void testGetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());commandLine.addOption(new Option("p", "property", true, "a property option"));commandLine.addProperty(commandLine.getOption("p"), "propertyName", "propertyValue");String propertyValue = commandLine.getProperty(commandLine.getOption("p"), "defaultValue");Assert.assertEquals("propertyValue", propertyValue);}

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", false, "an argument"));
 commandLine.addSwitch(commandLine.getOption("a"), true);
 commandLine.addSwitch(commandLine.getOption("a"), false);}

 @Test(expected = IllegalStateException.class)
 public void testAddValueToNonArgumentOption() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", false, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", false, "an argument"));
 commandLine.addValue(commandLine.getOption("a"), "value");}

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", true, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", true, "an argument"));
 commandLine.addValue(commandLine.getOption("a"), "value1");
 commandLine.addValue(commandLine.getOption("a"), "value2");
 String expected = "-a value1 value2";
 String actual = commandLine.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueToOptionWithoutValue() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "argument", false, "an argument"), new ArrayList());
 commandLine.addOption(new Option("a", "argument", false, "an argument"));
 commandLine.addValue(commandLine.getOption("a"), "value");}
}
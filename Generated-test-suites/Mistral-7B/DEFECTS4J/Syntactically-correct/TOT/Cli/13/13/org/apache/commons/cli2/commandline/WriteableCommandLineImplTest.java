import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.ParseResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl cmdLine;
 private Option option;

 @Before
 public void setup() {
 cmdLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 option = new Option("a", "append", true, "Append to file");
 cmdLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 List values = cmdLine.getValues(option, Collections.emptyList());
 Assert.assertEquals(0, values.size());

 cmdLine.addValue(option, "test.txt");
 values = cmdLine.getValues(option, Collections.emptyList());
 Assert.assertEquals("test.txt", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 Assert.assertFalse(cmdLine.getSwitch(option, false));

 cmdLine.addSwitch(option, true);
 Assert.assertTrue(cmdLine.getSwitch(option, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 cmdLine.addSwitch(option, true);
 cmdLine.addSwitch(option, true);
 }

 @Test
 public void testAddProperty() {
 cmdLine.addProperty("test", "value");
 Assert.assertEquals("value", cmdLine.getProperty("test", "default"));
 }

 @Test
 public void testParse() {
 ParseResult result = new ParseResult();
 cmdLine.parse(new String[]{"-a", "--append", "--append=test.txt"}, result);

 List values = cmdLine.getValues(option, Collections.emptyList());
 Assert.assertEquals("test.txt", values.get(0));
 Assert.assertTrue(cmdLine.getSwitch(option, false));
 }

 @Test
 public void testParseWithInvalidOption() {
 ParseResult result = new ParseResult();
 cmdLine.parse(new String[]{"--invalid"}, result);
 Assert.assertFalse(cmdLine.hasOption(option));
 }

 @Test
 public void testParseWithInvalidValue() {
 ParseResult result = new ParseResult();
 cmdLine.parse(new String[]{"-a", "test.txt test.txt"}, result);
 Assert.assertEquals(2, cmdLine.getValues(option, Collections.emptyList()).size());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("-a --append test.txt", cmdLine.toString());
 }
}
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("o", "option", true, "Option value");
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] args = new String[]{"-o", "value"};
 parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(args);
 assertEquals("value", cmd.getOptionValue("option"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 String[] args = new String[]{"-a"};
 parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(args);
 fail();
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Missing required option"));
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 String[] args = new String[]{"-x", "value"};
 parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(args);
 fail();
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Unrecognized option"));
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingValueForOption() throws ParseException {
 String[] args = new String[]{"-o"};
 parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(args);
 fail();
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Missing argument for option"));
 }
 }

 @Test
 public void testParseWithValuesForOption() throws ParseException {
 String[] args = new String[]{"-o", "value1", "-o", "value2"};
 parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(args);
 Assert.assertEquals(2, cmd.getNumOptions());
 assertEquals("value1", cmd.getOptionValue("option", 0));
 assertEquals("value2", cmd.getOptionValue("option", 1));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("option", "value3");
 String[] args = new String[]{"--property"};
 parser = new DefaultParser();
 parser.setOptions(options);
 parser.parse(args, properties);
 CommandLine cmd = parser.parse(new String[0]);
 assertEquals("value3", cmd.getOptionValue("option"));
 }
}
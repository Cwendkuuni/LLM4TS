import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParseBasic() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"-a"};
 CommandLine cmd = parser.parse(options, arguments);
 Assert.assertTrue(cmd.hasOption("a"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"--b"};
 parser.parse(options, arguments);
 }

 @Test(expected = ParseException.class)
 public void testParseMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", true, "option B");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"-a"};
 parser.parse(options, arguments);
 Assert.assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[0]));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", true, "option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[0];
 Assert.assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"-x"};
 parser.parse(options, arguments);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 Properties properties = new Properties();
 properties.setProperty("optionA", "valueA");
 CommandLine cmd = parser.parse(options, new String[0], properties);
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertEquals("valueA", cmd.getOptionValue("a", null));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidPropertiesValue() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 Properties properties = new Properties();
 properties.setProperty("optionA", "1,2,3");
 parser.parse(options, new String[0], properties);
 }

 @Test
 public void testParseWithMultipleArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "option A");
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] arguments = new String[]{"-a", "arg1", "arg2"};
 CommandLine cmd = parser.parse(options, arguments);
 Assert.assertEquals(2, cmd.getArgs().size());
 Assert.assertEquals("arg1", cmd.getArgs().get(0));
 Assert.assertEquals("arg2", cmd.getArgs().get(1));
 }
}
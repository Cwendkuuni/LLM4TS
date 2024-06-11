import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParseWithEmptyArguments() throws ParseException {
 Options options = new Options();
 CommandLine cmd = new Parser().parse(options, new String[0]);
 assertTrue(cmd.getArgs().isEmpty());
 assertFalse(cmd.getOptions().isEmpty());
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", false, "This is an option");
 CommandLine cmd = new Parser().parse(options, new String[]{"o"});
 assertTrue(cmd.getArgs().isEmpty());
 assertTrue(cmd.getOptions().size() == 1);
 assertTrue(cmd.getOptionValue("option"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", false, "This is an option");
 CommandLine cmd = new Parser().parse(options, new String[]{"x"});
 assertFalse(cmd.getOptions().isEmpty());
 assertNull(cmd.getOptionValue("option"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnknownOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", false, "This is an option");
 CommandLine cmd = new Parser().parse(options, new String[]{"z"});
 assertFalse(cmd.getOptions().isEmpty());
 assertNull(cmd.getOptionValue("option"));
 }

 @Test
 public void testParseWithOptionalArg() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "This is an option with an optional arg");
 CommandLine cmd = new Parser().parse(options, new String[]{"o", "value"});
 assertEquals("value", cmd.getOptionValue("option"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingArg() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "This is an option with a required arg");
 CommandLine cmd = new Parser().parse(options, new String[]{"o"});
 assertFalse(cmd.getOptions().isEmpty());
 assertNull(cmd.getOptionValue("option"));
 }

 @Test
 public void testParseWithMultipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o1", "option1", false, "Option 1");
 options.addOption("o2", "option2", false, "Option 2");
 CommandLine cmd = new Parser().parse(options, new String[]{"o1", "o2"});
 assertTrue(cmd.getArgs().isEmpty());
 assertTrue(cmd.getOptions().size() == 2);
 assertEquals(2, cmd.getNumOptions());
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", false, "This is an option");
 CommandLine cmd = new Parser().parse(options, null, new Properties() {{
 put("o", "value");
 }});
 assertTrue(cmd.getArgs().isEmpty());
 assertTrue(cmd.getOptions().size() == 1);
 assertTrue(cmd.getOptionValue("option").equals("value"));
 }
}
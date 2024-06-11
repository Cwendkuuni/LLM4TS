import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParser() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--output", "test.txt"};
 CommandLine cmd = parser.parse(options, args);

 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("nonExistentOption"));

 options.addRequiredOption("r", "required", true, "Required option");
 args = new String[]{"--required"};
 cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("required"));
 assertFalse(cmd.hasOption("nonExistentOption"));

 args = new String[]{"--required", "--output", "test.txt"};
 cmd = parser.parse(options, args);
 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertTrue(cmd.hasOption("required"));

 args = new String[]{"--output", "test.txt", "--required"};
 cmd = parser.parse(options, args);
 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertTrue(cmd.hasOption("required"));

 args = new String[]{"--output", "test.txt", "--output2", "test2.txt"};
 cmd = parser.parse(options, args);
 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("output2"));

 args = new String[]{"--", "arg1", "--option", "value"};
 cmd = parser.parse(options, args);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 assertEquals("value", cmd.getArgs().get(1));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("r", "required", true, "Required option");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--invalid"};
 parser.parse(options, args);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--unrecognized"};
 parser.parse(options, args);
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", "arg", true, "Argument option");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--arg"};
 parser.parse(options, args);
 }
}
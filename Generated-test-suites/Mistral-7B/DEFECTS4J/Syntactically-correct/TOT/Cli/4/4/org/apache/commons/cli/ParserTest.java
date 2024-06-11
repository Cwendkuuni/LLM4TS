import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS_1 = {"-a", "1", "--b", "2", "--c", "3"};
 private static final String[] ARGS_2 = {"--", "-a", "1", "--b", "2", "--c", "3"};
 private static final String[] ARGS_3 = {"--help"};
 private static final String[] ARGS_4 = {"--a", "--b"};
 private static final String[] ARGS_5 = {"--foo"};
 private static final String[] ARGS_6 = {"--invalid"};

 @Test
 public void testParse_Simple() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "An option with a value");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_1, false);

 assertEquals(1, cmd.getArgs().size());
 assertEquals("1", cmd.getArgs().get(0));

 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.getOptionValue("a"));
 assertFalse(cmd.hasOption("b"));
 }

 @Test
 public void testParse_DashDash() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "An option with a value");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_2, false);

 assertEquals(5, cmd.getArgs().size());
 assertEquals("1", cmd.getArgs().get(0));
 assertEquals("--", cmd.getArgs().get(1));

 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.getOptionValue("a"));
 assertFalse(cmd.hasOption("b"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParse_UnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "An option with a value");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_5, false);
 }

 @Test(expected = MissingArgumentException.class)
 public void testParse_MissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "An option with a value");
 options.addOption("b", true, "An option with an optional argument");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_4, false);
 }

 @Test(expected = MissingOptionException.class)
 public void testParse_MissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "An option with a value");
 options.addOption("b", true, "An option with a value");
 options.addRequiredOption("c", true, "A required option");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_1, false);
 }

 @Test(expected = MissingOptionException.class)
 public void testParse_MissingAllRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "An option with a value");
 options.addOption("b", true, "An option with a value");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_3, false);
 }

 @Test(expected = ParseException.class)
 public void testParse_Invalid() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "An option with a value");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_6, false);
 }

 @Test
 public void testParse_Help() throws ParseException {
 Options options = new Options();
 options.addOption("help", false, "Prints this help message.");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_3, true);

 assertTrue(cmd.hasOption("help"));
 }
}
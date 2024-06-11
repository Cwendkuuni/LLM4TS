import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 2, "Option C with an integer value");
 options.addOption("d", null, "Option D with a string value");
 options.addOption("e", null, "Option E with a string value");
 options.addRequiredOption("r", true, "Required Option R");

 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = new String[]{"-a", "--b", "3", "-c", "5", "--d", "test", "--e", "value", "-r"};
 CommandLine cmd = parser.parse(args);

 assertEquals(1, cmd.getArgs().size());
 assertEquals("test", cmd.getOptionValue("d"));
 assertEquals("value", cmd.getOptionValue("e"));
 assertEquals(3, cmd.getOptionValue("b", Integer.class));
 assertEquals(5, cmd.getOptionValue("c", Integer.class));
 assertEquals(true, cmd.hasOption("a"));
 assertEquals(true, cmd.hasOption("r"));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addRequiredOption("r", true, "Required Option R");

 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = new String[]{"-a"};
 CommandLine cmd = parser.parse(args);
 }

 @Test(expected = ParseException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = new String[]{"-z"};
 }

 @Test(expected = ParseException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");

 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = new String[]{"-a", "--b"};
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", null, "Option C with a string value");

 Properties props = new Properties();
 props.setProperty("b", "4");
 props.setProperty("c", "test");

 Parser parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = new String[]{"--property-file", "test.properties"};
 CommandLine cmd = parser.parse(args, props);

 assertEquals(1, cmd.getArgs().size());
 assertEquals(4, cmd.getOptionValue("b", Integer.class));
 assertEquals("test", cmd.getOptionValue("c"));
 }
}
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{

 private Parser parser = new DefaultParser();

 @Test
 public void testParseBasic() throws ParseException
 {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 CommandLine cmd = parser.parse(options, new String[]{"--output", "test.txt"});
 assertEquals("test.txt", cmd.getOptionValue("output"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOption() throws ParseException
 {
 Options options = new Options();
 CommandLine cmd = parser.parse(options, new String[]{"--invalid"});
 fail("Expected ParseException but no exception was thrown");
 }

 @Test
 public void testParseMultipleOptions() throws ParseException
 {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 CommandLine cmd = parser.parse(options, new String[]{"--output", "test1.txt", "--output", "test2.txt"});
 assertEquals(2, cmd.getOptionValues("output").length);
 assertEquals("test1.txt", cmd.getOptionValues("output")[0]);
 assertEquals("test2.txt", cmd.getOptionValues("output")[1]);
 }

 @Test
 public void testParseOptionalArgument() throws ParseException
 {
 Options options = new Options();
 options.addOption("n", "number", true, "number");
 CommandLine cmd = parser.parse(options, new String[]{"--number", "10"});
 assertEquals("10", cmd.getOptionValue("number"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOptionalArgument() throws ParseException
 {
 Options options = new Options();
 options.addOption("n", "number", true, "number");
 CommandLine cmd = parser.parse(options, new String[]{"--number"});
 fail("Expected ParseException but no exception was thrown");
 }

 @Test
 public void testParseNoOptions() throws ParseException
 {
 Options options = new Options();
 CommandLine cmd = parser.parse(options, new String[]{"--"});
 assertTrue(cmd.getArgs().length == 0);
 }

 @Test
 public void testParseWithProperties() throws ParseException
 {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 CommandLine cmd = parser.parse(options, new String[]{"--output", "test.txt"}, new Properties(){
 {
 put("output", "output2.txt");
 }
 });
 assertEquals("output2.txt", cmd.getOptionValue("output"));
 }

}
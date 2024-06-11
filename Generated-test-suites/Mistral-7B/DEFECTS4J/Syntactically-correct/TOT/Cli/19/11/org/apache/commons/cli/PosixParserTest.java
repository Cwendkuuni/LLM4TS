import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testSimpleOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 String[] args = {"-a"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Option A should be set when using a single '-a'", new String[]{"-a"}, parsedArgs);
 }

 @Test(expected = ParseException.class)
 public void testUnknownOption() {
 Options options = new Options();
 String[] args = {"--unknown"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 fail("Unknown option should cause a ParseException");
 }

 @Test
 public void testOptionWithValue() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", 1, "Option C");

 String[] args = {"-a", "--b=value", "-c=123"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Options A and C should be set, Option B should have its value set", new String[]{"-a", "--b=value", "-c=123"}, parsedArgs);
 }

 @Test
 public void testTwoOptionsWithValues() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", 1, "Option C");

 String[] args = {"-a", "--b=value1", "--b=value2", "-c=123"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Options A and C should be set, Option B should have both values set", new String[]{"-a", "--b=value1", "--b=value2", "-c=123"}, parsedArgs);
 }

 @Test
 public void testLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");

 String[] args = {"--longOption"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Long Option should be set when using a long option", new String[]{"--longOption"}, parsedArgs);
 }

 @Test(expected = ParseException.class)
 public void testInvalidLongOption() {
 Options options = new Options();
 String[] args = {"--invalidOption"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 fail("Invalid long option should cause a ParseException");
 }

 @Test
 public void testOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"-a", "--b=value"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Option A should be set and Option B should have its value set", new String[]{"-a", "--b=value"}, parsedArgs);
 }

 @Test
 public void testMultipleOptionsWithArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"-a", "--b=value1", "--b=value2"};
 PosixParser parser = new PosixParser();String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Option A should be set and Option B should have both values set", new String[]{"-a", "--b=value1", "--b=value2"}, parsedArgs);
 }

 @Test
 public void testStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");

 String[] args = {"--optionA", "nonOption"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, true);
 assertEquals("Non option should result in the remaining arguments being added to the parsed arguments without further processing", new String[]{"--optionA", "nonOption"}, parsedArgs);
 }

 @Test
 public void testBurstOption() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", true, "Option CD");

 String[] args = {"-abcd"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Burst option should be split into its constituent parts", new String[]{"-ab", "-cd"}, parsedArgs);
 }

 @Test
 public void testBurstOptionWithValue() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", true, "Option CD");

 String[] args = {"-abcd=value"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Burst option with value should be split into its constituent parts and have its value set", new String[]{"-ab", "--cd=value"}, parsedArgs);
 }

 @Test
 public void testBurstOptionWithMultipleValues() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", true, "Option CD");

 String[] args = {"-abcd=value1", "-abcd=value2"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertEquals("Burst option with multiple values should be split into its constituent parts and have both values set", new String[]{"-ab", "--cd=value1", "--cd=value2"}, parsedArgs);
 }

 @Test
 public void testBurstOptionStopAtNonOption() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", true, "Option CD");

 String[] args = {"-abcd=value", "nonOption"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, true);
 assertEquals("Burst option with value followed by a non option should result in the burst option and the non option being added to the parsed arguments without further processing", new String[]{"-abcd=value", "nonOption"}, parsedArgs);
 }
}
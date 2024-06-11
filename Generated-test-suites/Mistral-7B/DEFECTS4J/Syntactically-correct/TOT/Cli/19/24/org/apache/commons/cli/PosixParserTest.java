import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

public class PosixParserTest {

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 String[] args = {"--foo", "-o", "bar", "arg1", "arg2", "-a", "-b", "--"};
 PosixParser parser = new PosixParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 List<String> expected = Arrays.asList("--foo", "-o", "bar", "arg1", "arg2", "-a", "-b", "--");
 assertEquals(expected.size(), flattenedArgs.length);
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), flattenedArgs[i]);
 }
 }

 @Test
 public void testFlattenNoArguments() {
 Options options = new Options();
 String[] args = {"--foo", "-o", "--"};
 PosixParser parser = new PosixParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 List<String> expected = Arrays.asList("--foo", "--");
 assertEquals(expected.size(), flattenedArgs.length);
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), flattenedArgs[i]);
 }
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 String[] args = {"--foo", "-o", "output.txt", "--", "-input", "input.txt"};
 PosixParser parser = new PosixParser();
 String[] flattenedArgs = parser.flatten(options, args, true);
 List<String> expected = Arrays.asList("--foo", "-o", "output.txt", "--", "-input", "input.txt");
 assertEquals(expected.size(), flattenedArgs.length);
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), flattenedArgs[i]);
 }
 }

 @Test
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] args = {"--invalid", "--foo", "-o", "output.txt", "--"};
 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, args, false);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testInit() {
 PosixParser parser = new PosixParser();
 parser.init();
 assertNull(parser.getCurrentOption());
 assertEquals(0, parser.getTokens().size());
 assertFalse(parser.isEatTheRest());
 }

 @Test
 public void testProcess() {
 PosixParser parser = new PosixParser();
 String[] args = {"--foo", "arg1", "--arg", "arg2", "--"};
 parser.init();
 parser.process("--foo");
 List<String> expected = Arrays.asList("--foo");
 assertEquals(expected.size(), parser.getTokens().size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), parser.getTokens().get(i));
 }
 }

 @Test
 public void testProcessOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 PosixParser parser = new PosixParser();
 String[] args = {"-o", "output.txt"};
 parser.init();
 parser.processOptionToken("-o", false);
 List<String> expected = Arrays.asList("-o");
 assertEquals(expected.size(), parser.getTokens().size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), parser.getTokens().get(i));
 }
 assertEquals("output", parser.getCurrentOption().getLongOpt());
 assertEquals("output", parser.getCurrentOption().getOpt());
 assertEquals("output file path", parser.getCurrentOption().getDescription());
 assertFalse(parser.isEatTheRest());
 }

 @Test
 public void testProcessUnknown() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = {"--unknown"};
 parser.init();
 parser.process("--unknown");
 List<String> expected = Arrays.asList("--unknown");
 assertEquals(expected.size(), parser.getTokens().size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), parser.getTokens().get(i));
 }
 assertNull(parser.getCurrentOption());
 }

 @Test
 public void testBurstSimple() {
 Options options = new Options();
 String[] args = {"-ao", "arg1", "arg2", "--"};
 PosixParser parser = new PosixParser();
 parser.init();
 parser.burstToken("-ao", false);
 List<String> expected = Arrays.asList("-a", "--o");
 assertEquals(expected.size(), parser.getTokens().size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), parser.getTokens().get(i));
 }
 }

 @Test
 public void testBurstWithArgument() {
 Options options = new Options();
 options.addOption("a", "arg", true, "an argument");
 String[] args = {"-aarg1", "arg2"};
 PosixParser parser = new PosixParser();
 parser.init();
 parser.burstToken("-aarg1", false);
 List<String> expected = Arrays.asList("-a", "--arg", "arg1");
 assertEquals(expected.size(), parser.getTokens().size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), parser.getTokens().get(i));
 }
 }

 @Test
 public void testBurstWithMultipleArguments() {
 Options options = new Options();
 options.addOption("a", "arg", true, "an argument");
 String[] args = {"-aa", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 parser.init();
 parser.burstToken("-aa", false);
 List<String> expected = Arrays.asList("-a", "--arg", "arg1", "-a", "--arg", "arg2");
 assertEquals(expected.size(), parser.getTokens().size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), parser.getTokens().get(i));
 }
 }

 @Test
 public void testBurstStopAtNonOption() {
 Options options = new Options();
 String[] args = {"-aarg1", "--", "arg3"};
 PosixParser parser = new PosixParser();
 parser.init();
 parser.burstToken("-aarg1", true);
 List<String> expected = Arrays.asList("-a", "--arg", "arg1", "--", "arg3");
 assertEquals(expected.size(), parser.getTokens().size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), parser.getTokens().get(i));
 }
 }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private List<String> tokens;
 private PosixParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("input", true, "i")
 .longOpt("inputFile")
 .requiredArg()
 .desc("Input file");
 options.addOption("output", false, "o")
 .longOpt("outputFile")
 .hasArg()
 .desc("Output file");
 }

 @Test
 public void testFlattenWithSimpleArguments() throws ParseException {
 String[] arguments = new String[]{"java", "-i", "input.txt", "-o", "output.txt"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "-i", "input.txt", "-o", "output.txt"), tokens);
 }

 @Test
 public void testFlattenWithLongArguments() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=input.txt", "--outputFile=output.txt"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "input.txt", "--outputFile", "output.txt"), tokens);
 }

 @Test
 public void testFlattenWithNoOptions() throws ParseException {
 String[] arguments = new String[]{"java", "arg1", "arg2"};
 tokens = parser.flatten(options, arguments, false);
 assertTrue(tokens.contains("--"));
 assertEquals(Arrays.asList("arg1", "arg2"), tokens.subList(tokens.indexOf("--") + 1, tokens.size()));
 }

 @Test
 public void testFlattenWithStopAtNonOption() throws ParseException {
 String[] arguments = new String[]{"java", "--", "-i", "input.txt", "--outputFile", "output.txt"};
 tokens = parser.flatten(options, arguments, true);
 assertEquals(Arrays.asList("--i", "input.txt", "--outputFile", "output.txt"), tokens);
 }

 @Test
 public void testFlattenWithInvalidOption() throws ParseException {
 String[] arguments = new String[]{"java", "--invalidOption"};
 parser.flatten(options, arguments, false);
 assertFalse(parser.eatTheRest);
 assertNull(parser.currentOption);
 tokens = parser.tokens;
 assertTrue(tokens.contains("--invalidOption"));
 }

 @Test
 public void testFlattenWithOptionArg() throws ParseException {
 String[] arguments = new String[]{"java", "--outputFile=output.txt"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--outputFile", "output.txt"), tokens);
 }

 @Test
 public void testFlattenWithOptionArgAndFollowingTokens() throws ParseException {
 String[] arguments = new String[]{"java", "--outputFile=output.txt", "arg1", "arg2"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--outputFile", "output.txt", "arg1", "arg2"), tokens);
 }

 @Test
 public void testFlattenWithOptionArgAndNextOption() throws ParseException {
 String[] arguments = new String[]{"java", "--outputFile=output.txt", "--inputFile=input.txt"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--outputFile", "output.txt", "--inputFile", "input.txt"), tokens);
 }

 @Test (expected = ParseException.class)
 public void testFlattenWithInvalidOptionArg() throws ParseException {
 String[] arguments = new String[]{"java", "--outputFile"};
 parser.flatten(options, arguments, false);
 assertFalse(parser.eatTheRest);
 assertNull(parser.currentOption);
 tokens = parser.tokens;
 assertTrue(tokens.contains("--outputFile"));
 // The test should fail with a ParseException due to missing argument
 }

 @Test
 public void testFlattenWithMultipleOptions() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=input.txt", "--outputFile=output.txt"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "input.txt", "--outputFile", "output.txt"), tokens);
 }

 @Test
 public void testFlattenWithMultipleOptionsAndFollowingTokens() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=input.txt", "--outputFile=output.txt", "arg1", "arg2"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "input.txt", "--outputFile", "output.txt", "arg1", "arg2"), tokens);
 }

 @Test
 public void testFlattenWithMultipleOptionsAndMixedArguments() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=input.txt", "--outputFile=output.txt", "-arg1", "-arg2"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "input.txt", "--outputFile", "output.txt", "-arg1", "--", "arg2"), tokens);
 }

 @Test
 public void testFlattenWithMultipleOptionsAndNextOption() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=input.txt", "--outputFile=output.txt", "--inputFile2=input2.txt"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "input.txt", "--outputFile", "output.txt", "--inputFile2", "input2.txt"), tokens);
 }

 @Test
 public void testFlattenWithMultipleOptionsAndMissingArgument() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=input.txt", "--outputFile"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "input.txt", "--outputFile"), tokens);
 }

 @Test
 public void testFlattenWithLongOptionArg() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=longinputarg.txt"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "longinputarg.txt"), tokens);
 }

 @Test
 public void testFlattenWithLongOptionArgAndFollowingTokens() throws ParseException {
 String[] arguments = new String[]{"java", "--inputFile=longinputarg.txt", "arg1", "arg2"};
 tokens = parser.flatten(options, arguments, false);
 assertEquals(Arrays.asList("java", "--inputFile", "longinputarg.txt", "arg1", "arg2"), tokens);
 }
}
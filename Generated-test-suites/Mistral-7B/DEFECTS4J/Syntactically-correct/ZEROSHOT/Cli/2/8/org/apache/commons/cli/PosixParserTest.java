import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private String[] expectedArgs;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 3, "Option C with three arguments");
 }

 @After
 public void tearDown() {
 options = null;
 }

 @Test
 public void testEmptyArguments() throws ParseException {
 String[] args = {};
 String[] expectedArgs = {};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testSingleArgument() throws ParseException {
 String[] args = {"arg1"};
 String[] expectedArgs = {"arg1"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testDoubleHyphen() throws ParseException {
 String[] args = {"--"};
 String[] expectedArgs = {"--"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testSingleOption() throws ParseException {
 String[] args = {"-a"};
 String[] expectedArgs = {"-a"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testOptionWithArgument() throws ParseException {
 String[] args = {"-a", "arg1"};
 String[] expectedArgs = {"-a", "arg1"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testOptionWithMultipleArguments() throws ParseException {
 String[] args = {"-c", "arg1", "arg2", "arg3"};
 String[] expectedArgs = {"-c", "arg1", "arg2", "arg3"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testOptionWithLongId() throws ParseException {
 String[] args = {"--option-with-long-id"};
 String[] expectedArgs = {"--option-with-long-id"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test(expected = ParseException.class)
 public void testUnknownOption() throws ParseException {
 String[] args = {"-x"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 String[] args = {"arg1", "-a", "arg2"};
 String[] expectedArgs = {"arg1", "-a"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, true);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testBursting() throws ParseException {
 String[] args = {"-abc"};
 String[] expectedArgs = {"-a", "-b", "-c"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testBurstingWithArgument() throws ParseException {
 String[] args = {"-ab", "arg"};
 String[] expectedArgs = {"-a", "-b", "arg"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testBurstingWithStopAtNonOption() throws ParseException {
 String[] args = {"arg1", "-ab", "arg2"};
 String[] expectedArgs = {"arg1", "-ab"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, true);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testBurstingWithMultipleOptions() throws ParseException {
 String[] args = {"-acd"};
 String[] expectedArgs = {"-a", "-c", "-d"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testBurstingWithOptionWithArgument() throws ParseException {
 String[] args = {"-bc", "arg"};
 String[] expectedArgs = {"-b", "-c", "arg"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, args, false);
 assertArrayEquals(expectedArgs, parsedArgs);
 }
}
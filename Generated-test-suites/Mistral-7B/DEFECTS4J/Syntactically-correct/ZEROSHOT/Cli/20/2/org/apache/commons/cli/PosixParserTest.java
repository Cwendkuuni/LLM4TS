import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 3, "Option C with an integer argument");
 }

 @After
 public void tearDown() {
 parser = null;
 options = null;
 }

 @Test
 public void testFlattenSimple() throws ParseException {
 String[] args = new String[]{"--foo", "-a", "-b", "--bar"};
 String[] expected = new String[]{"--foo", "-a", "--", "--bar"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithArg() throws ParseException {
 String[] args = new String[]{"--foo", "-a", "arg1", "-b", "arg2"};
 String[] expected = new String[]{"--foo", "-a", "arg1", "--", "--bar", "-b", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() throws ParseException {
 String[] args = new String[]{"--foo", "-z", "-b", "--bar"};
 parser = new PosixParser();
 try {
 parser.flatten(options, args, false);
 } catch (ParseException e) {
 assertEquals("Unrecognized option: z", e.getMessage());
 }
 }

 @Test
 public void testFlattenStopAtNonOption() throws ParseException {
 String[] args = new String[]{"--foo", "-a", "-b", "nonOption", "--bar"};
 String[] expected = new String[]{"--foo", "-a", "--", "nonOption", "--bar"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOption() throws ParseException {
 String[] args = new String[]{"--foo", "-abc", "arg1", "--bar"};
 String[] expected = new String[]{"--foo", "-a", "--", "-b", "-c", "arg1", "--bar"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithArg() throws ParseException {
 String[] args = new String[]{"--foo", "-abc", "arg1", "--bar", "arg2"};
 String[] expected = new String[]{"--foo", "-a", "--", "-b", "-c", "arg1", "--", "--bar", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithIntegerArg() throws ParseException {
 String[] args = new String[]{"--foo", "-abc", "arg1", "--bar", "3"};
 String[] expected = new String[]{"--foo", "-a", "--", "-b", "-c", "arg1", "--", "--bar", "3"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }
}
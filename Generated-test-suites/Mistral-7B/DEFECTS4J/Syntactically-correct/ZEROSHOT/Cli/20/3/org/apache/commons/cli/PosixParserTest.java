import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 1, "Option C with an integer value");
 options.addOption("d", 2, "Option D with two integers");
 }

 @After
 public void tearDown() {
 parser = null;
 options = null;
 }

 @Test
 public void testFlatten_Simple() throws ParseException {
 String[] args = {"--optionA", "--optionB", "1", "--optionD", "1", "2"};
 String[] expected = {"--optionA", "--optionB", "1", "--optionD", "1", "2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected.length, result.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], result[i]);
 }
 }

 @Test
 public void testFlatten_StopAtNonOption() throws ParseException {
 String[] args = {"--optionA", "--optionB", "1", "--optionC", "-- unused", "1", "2"};
 String[] expected = {"--optionA", "--optionB", "1", "--optionC"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected.length, result.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], result[i]);
 }
 }

 @Test
 public void testFlatten_Empty() throws ParseException {
 String[] args = {};
 String[] expected = {};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected.length, result.length);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidOption() throws ParseException {
 String[] args = {"--invalidOption"};
 parser = new PosixParser(); // need to instantiate without options
 parser.flatten(null, args, false);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_MissingOption() throws ParseException {
 String[] args = {"--optionA", "--optionX", "1"};
 parser = new PosixParser(); // need to instantiate without options
 parser.flatten(options, args, false);
 }

 @Test
 public void testBurstToken_Simple() {
 String token = "--optionA";
 String[] expected = {"--optionA"};
 parser = new PosixParser();
 List<String> result = parser.burstToken(token, false);
 assertEquals(expected.length, result.size());
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], result.get(i));
 }
 }

 @Test
 public void testBurstToken_NoOption() {
 String token = "--optionA--optionB";
 String[] expected = {"--optionA", "--optionB"};
 parser = new PosixParser();
 List<String> result = parser.burstToken(token, true);
 assertEquals(expected.length, result.size());
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], result.get(i));
 }
 }

 @Test
 public void testBurstToken_OptionWithArg() {
 String token = "--optionB=1";
 String[] expected = {"--optionB", "1"};
 parser = new PosixParser();
 List<String> result = parser.burstToken(token, false);
 assertEquals(expected.length, result.size());
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], result.get(i));
 }
 }

 @Test
 public void testBurstToken_OptionWithMultipleArgs() {
 String token = "--optionD=1 2";
 String[] expected = {"--optionD", "1", "2"};
 parser = new PosixParser();
 List<String> result = parser.burstToken(token, false);
 assertEquals(expected.length, result.size());
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], result.get(i));
 }
 }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class PosixParserTest {

 private List tokens;
 private PosixParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B");
 options.addOption("c", 3, "Option C with argument");
 }

 @Test
 public void testFlattenEmpty() {
 tokens = parser.flatten(options, new String[0], false);
 Assert.assertEquals(0, tokens.size());
 }

 @Test
 public void testFlattenSingleOption() {
 tokens = parser.flatten(options, new String[]{"-a"}, false);
 Assert.assertEquals(1, tokens.size());
 Assert.assertEquals("-a", tokens.get(0));
 }

 @Test
 public void testFlattenMultipleOptions() {
 tokens = parser.flatten(options, new String[]{"-a", "-b", "-c", "--value=3"}, false);
 Assert.assertEquals(5, tokens.size());
 Assert.assertEquals("-a", tokens.get(0));
 Assert.assertEquals("-b", tokens.get(1));
 Assert.assertEquals("-c", tokens.get(2));
 Assert.assertEquals("--value=3", tokens.get(3));
 }

 @Test
 public void testFlattenLongOption() {
 tokens = parser.flatten(options, new String[]{"--value=3"}, false);
 Assert.assertEquals(1, tokens.size());
 Assert.assertEquals("--value=3", tokens.get(0));
 }

 @Test
 public void testFlattenLongOptionWithArgument() {
 tokens = parser.flatten(options, new String[]{"--value=3", "arg"}, false);
 Assert.assertEquals(3, tokens.size());
 Assert.assertEquals("--value", tokens.get(0));
 Assert.assertEquals("arg", tokens.get(1));
 Assert.assertEquals("3", tokens.get(2));
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 parser = new PosixParser();
 List tokens = parser.flatten(options, new String[]{"--invalid"}, false);
 fail("ParseException expected");
 }

 @Test
 public void testFlattenStopAtNonOption() {
 tokens = parser.flatten(options, new String[]{"-a", "-b", "--value=3"}, true);
 Assert.assertEquals(3, tokens.size());
 Assert.assertEquals("-a", tokens.get(0));
 Assert.assertEquals("-b", tokens.get(1));
 }

 @Test
 public void testFlattenBurstToken() {
 tokens = parser.flatten(options, new String[]{"-abc"}, false);
 Assert.assertEquals(4, tokens.size());
 Assert.assertEquals("-a", tokens.get(0));
 Assert.assertEquals("-b", tokens.get(1));
 Assert.assertEquals("-c", tokens.get(2));
 Assert.assertEquals("", tokens.get(3));
 }

 @Test
 public void testBurstTokenOptionWithValue() {
 tokens = parser.flatten(options, new String[]{"-ac=3"}, false);
 Assert.assertEquals(4, tokens.size());
 Assert.assertEquals("-a", tokens.get(0));
 Assert.assertEquals("-c", tokens.get(1));
 Assert.assertEquals("3", tokens.get(2));
 Assert.assertEquals("", tokens.get(3));
 }

 @Test
 public void testBurstTokenLongOption() {
 tokens = parser.flatten(options, new String[]{"--value=3a"}, false);
 Assert.assertEquals(3, tokens.size());
 Assert.assertEquals("--value", tokens.get(0));
 Assert.assertEquals("3a", tokens.get(1));
 }

 @Test
 public void testBurstTokenLongOptionWithValue() {
 tokens = parser.flatten(options, new String[]{"--value=3a=2"}, false);
 Assert.assertEquals(4, tokens.size());
 Assert.assertEquals("--value", tokens.get(0));
 Assert.assertEquals("3", tokens.get(1));
 Assert.assertEquals("a=2", tokens.get(2));
 }
}
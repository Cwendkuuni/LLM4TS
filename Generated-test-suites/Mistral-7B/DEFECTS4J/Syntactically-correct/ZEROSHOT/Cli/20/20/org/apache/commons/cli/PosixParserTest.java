import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testFlatten() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 2, "Option C with a short value");
 options.addOption("d", "Value", "Option D with a string value");
 options.addOption("e", false, "Option E");
 options.addOption("f", "defaultValue", "Option F with a default value");

 Parser parser = new PosixParser();
 String[] arguments = {"--a", "--c", "-d", "Value", "--e", "--f", "arg1", "arg2", "arg3"};
 String[] expected = {"--a", "-c", "--d", "Value", "--e", "--f", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 2, "Option C with a short value");
 options.addOption("d", "Value", "Option D with a string value");
 options.addOption("e", false, "Option E");
 options.addOption("f", "defaultValue", "Option F with a default value");

 Parser parser = new PosixParser();
 String[] arguments = {"--a", "--c", "-d", "Value", "--e", "--f", "arg1", "arg2", "--"};
 String[] expected = {"--a", "--c", "--", "arg1", "arg2"};
 String[] result = parser.flatten(options, arguments, true);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testProcessOptionToken() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 2, "Option C with a short value");
 options.addOption("d", "Value", "Option D with a string value");
 options.addOption("e", false, "Option E");
 options.addOption("f", "defaultValue", "Option F with a default value");

 Parser parser = new PosixParser();
 String[] arguments = {"--a", "--c", "-d", "Value", "--e", "--f", "arg1", "arg2", "arg3", "-g"};

 parser.processOptionToken("-a", false);
 Assert.assertNull(parser.currentOption);
 Assert.assertEquals(5, parser.tokens.size());
 Assert.assertEquals("-a", parser.tokens.get(0));
 Assert.assertEquals("-c", parser.tokens.get(1));
 Assert.assertEquals("--d", parser.tokens.get(2));
 Assert.assertEquals("Value", parser.tokens.get(3));
 Assert.assertEquals("--e", parser.tokens.get(4));
 Assert.assertNull(parser.currentOption);
 Assert.assertEquals("--f", parser.tokens.get(5));

 parser.init();
 parser.processOptionToken("-g", false);
 Assert.assertNull(parser.currentOption);
 Assert.assertEquals(7, parser.tokens.size());
 Assert.assertEquals("-a", parser.tokens.get(0));
 Assert.assertEquals("-c", parser.tokens.get(1));
 Assert.assertEquals("--d", parser.tokens.get(2));
 Assert.assertEquals("Value", parser.tokens.get(3));
 Assert.assertEquals("--e", parser.tokens.get(4));
 Assert.assertEquals("--f", parser.tokens.get(5));
 Assert.assertEquals("-g", parser.tokens.get(6));
 }

 @Test
 public void testBurstToken() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", "Value", "Option CD with a string value");
 options.addOption("ef", 1, "Option EF with an integer value");

 Parser parser = new PosixParser();
 String[] arguments = {"--abcd", "--ef", "--ab cd", "--ef"};
 String[] expected = {"--ab", "--cd", "--ef", "--ef"};
 String[] result = parser.flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstTokenInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 2, "Option C with a short value");
 options.addOption("d", "Value", "Option D with a string value");
 options.addOption("e", false, "Option E");
 options.addOption("f", "defaultValue", "Option F with a default value");

 Parser parser = new PosixParser();
 String[] arguments = {"--xyz"};

 parser.burstToken("xyz", false);
 }
}
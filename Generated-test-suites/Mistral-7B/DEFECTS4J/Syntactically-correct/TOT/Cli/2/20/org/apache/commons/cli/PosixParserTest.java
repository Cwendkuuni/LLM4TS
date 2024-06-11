import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test for PosixParser class
 */
public class PosixParserTest {

 @Test
 public void testFlattenWithEmptyArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {};
 String[] expected = {};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenWithSimpleArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--foo", "--bar=value", "-b", "-a", "--"};
 String[] expected = {"--foo", "--bar=value", "-b", "-a"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenWithComplexArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", 1, "b");
 options.addOption("baz", 3, "z");
 String[] arguments = {"--foo", "--bar=value1", "--baz=value2--value3", "-b", "-a", "--"};
 String[] expected = {"--foo", "--bar=value1", "--baz=value2", "--baz=value3", "-b", "-a"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--invalidOption", "--foo"};
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] arguments = {"--foo", "--bar", "-b", "-a", "--"};
 String[] expected = {"--foo", "--bar", "-b", "-a"};
 String[] actual = parser.flatten(options, arguments, true);
 assertEquals(expected, actual);
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
 public void testProcessSingleHyphen() {
 PosixParser parser = new PosixParser();
 parser.processSingleHyphen("-");
 assertEquals(1, parser.getTokens().size());
 assertEquals("-", parser.getTokens().get(0));
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 parser.processOptionToken("--foo", false);
 assertEquals(1, parser.getTokens().size());
 assertEquals("--foo", parser.getTokens().get(0));
 parser.processOptionToken("--foo", true);
 assertEquals(2, parser.getTokens().size());
 assertEquals("--foo", parser.getTokens().get(0));
 assertEquals("", parser.getTokens().get(1));
 }

 @Test
 public void testBurstToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", 1, "b");
 String[] expected = {"--foo", "--bar=", "-"};
 String[] actual = parser.burstToken("-f--bar=value--", false);
 assertEquals(expected, actual);
 String[] expected2 = {"--foo", "--bar=value"};
 String[] actual2 = parser.burstToken("-f--bar=value--", true);
 assertEquals(expected2, actual2);
 }

 @Test
 public void testCurrentOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 parser.processOptionToken("--foo", false);
 assertSame(options.getOption("foo"), parser.getCurrentOption());
 }

 private ArrayList getTokens(PosixParser parser) {
 return (ArrayList) parser.getTokens();
 }
}
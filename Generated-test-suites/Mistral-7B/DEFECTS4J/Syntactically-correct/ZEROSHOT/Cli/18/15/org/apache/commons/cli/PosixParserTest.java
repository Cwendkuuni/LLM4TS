import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private static final String[] TEST_ARGUMENTS = {"--foo", "-o", "-a", "--bar", "-x", "value", "--baz=qux"};

 @Test
 public void testFlattenWithStopAtNonOptionFalse() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] tokens = parser.flatten(options, TEST_ARGUMENTS, false);
 assertArrayEquals(new String[]{"--foo", "-o", "--a", "--bar", "-x", "value", "--baz=qux"}, tokens);
 }

 @Test
 public void testFlattenWithStopAtNonOptionTrue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] tokens = parser.flatten(options, TEST_ARGUMENTS, true);
 assertArrayEquals(new String[]{"--foo", "--", "-o", "--a", "--bar", "-x", "value", "--baz=qux"}, tokens);
 }

 @Test
 public void testProcessSingleHyphen() {
 PosixParser parser = new PosixParser();
 String[] tokens = new String[0];
 parser.processSingleHyphen("-");
 tokens = parser.getTokens();
 assertEquals("-", tokens[0]);
 }

 @Test
 public void testInit() {
 PosixParser parser = new PosixParser();
 parser.init();
 assertNull(parser.getTokens());
 assertFalse(parser.isEatTheRest());
 assertNull(parser.getCurrentOption());
 }

 @Test
 public void testProcess() {
 PosixParser parser = new PosixParser();
 String[] tokens = new String[0];
 parser.process("--foo");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertNull(parser.getCurrentOption());

 parser.process("--bar");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertEquals("--bar", tokens[1]);
 assertNull(parser.getCurrentOption());

 parser.process("-o");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertEquals("--bar", tokens[1]);
 assertEquals("-o", tokens[2]);
 assertNull(parser.getCurrentOption());

 parser.process("-a");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertEquals("--bar", tokens[1]);
 assertEquals("-o", tokens[2]);
 assertEquals("-a", tokens[3]);
 assertNull(parser.getCurrentOption());

 parser.process("--bar");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertEquals("--bar", tokens[1]);
 assertEquals("-o", tokens[2]);
 assertEquals("-a", tokens[3]);
 assertEquals("--bar", tokens[4]);
 assertNull(parser.getCurrentOption());

 parser.process("-x");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertEquals("--bar", tokens[1]);
 assertEquals("-o", tokens[2]);
 assertEquals("-a", tokens[3]);
 assertEquals("--bar", tokens[4]);
 assertEquals("-x", tokens[5]);
 assertNull(parser.getCurrentOption());

 parser.process("value");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertEquals("--bar", tokens[1]);
 assertEquals("-o", tokens[2]);
 assertEquals("-a", tokens[3]);
 assertEquals("--bar", tokens[4]);
 assertEquals("-x", tokens[5]);
 assertEquals("value", tokens[6]);
 assertNull(parser.getCurrentOption());

 parser.process("--baz=qux");
 tokens = parser.getTokens();
 assertEquals("--foo", tokens[0]);
 assertEquals("--bar", tokens[1]);
 assertEquals("-o", tokens[2]);
 assertEquals("-a", tokens[3]);
 assertEquals("--bar", tokens[4]);
 assertEquals("-x", tokens[5]);
 assertEquals("value", tokens[6]);
 assertEquals("--baz", tokens[7]);
 assertEquals("qux", tokens[8]);
 assertNull(parser.getCurrentOption());
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("o", false, null, "Option O");
 options.addOption("a", true, "arg", "Option A");
 options.addOption("b", true, null, "Option B");
 String[] tokens = new String[0];
 parser.processOptionToken("-o", false);
 tokens = parser.getTokens();
 assertEquals("-o", tokens[0]);
 assertNull(parser.getCurrentOption());

 parser.processOptionToken("--foo", false);
 tokens = parser.getTokens();
 assertEquals("-o", tokens[0]);
 assertEquals("--foo", tokens[1]);
 assertNull(parser.getCurrentOption());

 parser.processOptionToken("-a", false);
 tokens = parser.getTokens();
 assertEquals("-o", tokens[0]);
 assertEquals("--foo", tokens[1]);
 assertEquals("-a", tokens[2]);
 assertNull(parser.getCurrentOption());

 parser.processOptionToken("-a", true);
 tokens = parser.getTokens();
 assertEquals("-o", tokens[0]);
 assertEquals("--foo", tokens[1]);
 assertEquals("-a", tokens[2]);
 assertEquals("arg", tokens[3]);
 assertNull(parser.getCurrentOption());

 parser.processOptionToken("--bar", false);
 tokens = parser.getTokens();
 assertEquals("-o", tokens[0]);
 assertEquals("--foo", tokens[1]);
 assertEquals("-a", tokens[2]);
 assertEquals("arg", tokens[3]);
 assertEquals("--bar", tokens[4]);
 assertNull(parser.getCurrentOption());

 parser.processOptionToken("--baz=qux", false);
 tokens = parser.getTokens();
 assertEquals("-o", tokens[0]);
 assertEquals("--foo", tokens[1]);
 assertEquals("-a", tokens[2]);
 assertEquals("arg", tokens[3]);
 assertEquals("--bar", tokens[4]);
 assertEquals("--baz", tokens[5]);
 assertEquals("qux", tokens[6]);
 assertNull(parser.getCurrentOption());
 }
}
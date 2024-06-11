import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");
 options.addOption("c", 3, "Option C with three arguments");
 }

 @Test
 public void testEmptyArgs() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[0], false);
 assertEquals(new String[0], args);
 }

 @Test
 public void testSingleArg() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"arg1"}, false);
 assertEquals(new String[]{"arg1"}, args);
 }

 @Test
 public void testLongOption() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"--option-a"}, false);
 assertEquals(new String[]{"--option-a"}, args);
 }

 @Test
 public void testShortOption() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"-a"}, false);
 assertEquals(new String[]{"-a"}, args);
 }

 @Test
 public void testShortOptionWithArg() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"-a", "arg"}, false);
 assertEquals(new String[]{"-a", "arg"}, args);
 }

 @Test(expected = ParseException.class)
 public void testUnknownOption() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"-x"}, false);
 fail("Unexpectedly did not throw ParseException");
 }

 @Test
 public void testShortOptionWithMultipleArgs() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"-c", "arg1", "arg2", "arg3"}, false);
 assertEquals(new String[]{"-c", "arg1", "arg2", "arg3"}, args);
 }

 @Test(expected = ParseException.class)
 public void testShortOptionDuplicate() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"-a", "-a"}, false);
 fail("Unexpectedly did not throw ParseException");
 }

 @Test
 public void testLongOptionWithArg() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"--option-b", "arg"}, false);
 assertEquals(new String[]{"--option-b", "arg"}, args);
 }

 @Test
 public void testLongOptionWithMultipleArgs() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"--option-c", "arg1", "arg2", "arg3"}, false);
 assertEquals(new String[]{"--option-c", "arg1", "arg2", "arg3"}, args);
 }

 @Test
 public void testLongOptionDuplicate() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"--option-b", "--option-b"}, false);
 fail("Unexpectedly did not throw ParseException");
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"arg1", "--option-a", "arg2"}, true);
 assertEquals(new String[]{"arg1", "--option-a"}, args);
 }

 @Test
 public void testStopAtNonOptionWithMultipleOptions() throws ParseException {
 parser = new PosixParser();args = parser.flatten(options, new String[]{"arg1", "--option-a", "--option-b", "arg2"}, true); assertEquals(new String[]{"arg1", "--option-a"}, args); }

 @Test
 public void testDashDash() throws ParseException {
 parser = new PosixParser();
 args = parser.flatten(options, new String[]{"--", "arg"}, false);
 assertEquals(new String[]{"--", "arg"}, args);
 }
}
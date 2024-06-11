import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 private static final String ARG_A = "a";
 private static final String ARG_B = "b";
 private static final String ARG_C = "c";
 private static final String ARG_D = "d";

 @Test
 public void testFlattenSimpleArguments() {
 Options options = new Options();
 String[] arguments = new String[]{"arg1", "arg2", "arg3"};

 String[] result = new GnuParser().flatten(options, arguments, false);

 assertEquals(3, result.length);
 assertEquals("arg1", result[0]);
 assertEquals("arg2", result[1]);
 assertEquals("arg3", result[2]);
 }

 @Test
 public void testFlattenOptionWithValue() {
 Options options = new Options().addOption("d", true, "displayHelpOnFail");
 String[] arguments = new String[]{"-d", "value"};

 String[] result = new GnuParser().flatten(options, arguments, false);

 assertEquals(3, result.length);
 assertEquals("-d", result[0]);
 assertEquals("value", result[1]);
 assertEquals("", result[2]);
 }

 @Test
 public void testFlattenDashDash() {
 Options options = new Options();
 String[] arguments = new String[]{"--", "option1=value1", "option2=value2"};

 String[] result = new GnuParser().flatten(options, arguments, false);

 assertEquals(5, result.length);
 assertEquals("--", result[0]);
 assertEquals("option1", result[1]);
 assertEquals("value1", result[2]);
 assertEquals("option2", result[3]);
 assertEquals("value2", result[4]);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 String[] arguments = new String[]{"-a", "arg1", "-b", "arg2", "arg3"};

 String[] result = new GnuParser().flatten(options, arguments, true);

 assertEquals(4, result.length);
 assertEquals("-a", result[0]);
 assertEquals("arg1", result[1]);
 assertEquals("-b", result[2]);
 assertEquals("arg3", result[3]);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidArgument() {
 Options options = new Options();
 String[] arguments = new String[]{"--invalidArgument", "arg1", "-b", "arg2"};

 try {
 new GnuParser().flatten(options, arguments, false);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOptionWithValue() {
 Options options = new Options();
 String[] arguments = new String[]{"-x", "value"};

 try {
 new GnuParser().flatten(options, arguments, false);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }
}
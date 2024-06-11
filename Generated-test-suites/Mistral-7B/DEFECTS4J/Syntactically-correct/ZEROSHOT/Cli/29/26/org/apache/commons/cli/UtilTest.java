import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String inputStr = "--test";
 String expectedOutput = "test";
 String outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "---test";
 expectedOutput = "test";
 outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "test";
 expectedOutput = "test";
 outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String inputStr = "\"test1 test2\"";
 String expectedOutput = "test1 test2";
 String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "\"test1\" test2";
 expectedOutput = "test1";
 outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "test1 \"test2\"";
 expectedOutput = "test1 test2";
 outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);
 }
}
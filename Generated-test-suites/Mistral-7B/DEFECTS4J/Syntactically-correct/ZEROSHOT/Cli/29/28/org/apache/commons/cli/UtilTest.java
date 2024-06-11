import org.junit.Test;
import org.junit.Assert;

public class UtilTest {

 /**
 * Test for Util.stripLeadingHyphens method with no leading hyphens.
 */
 @Test
 public void testStripLeadingHyphensNoLeadingHyphens() {
 String input = "testString";
 String expected = "testString";
 String actual = Util.stripLeadingHyphens(input);
 Assert.assertEquals(expected, actual);
 }

 /**
 * Test for Util.stripLeadingHyphens method with one leading hyphen.
 */
 @Test
 public void testStripLeadingHyphensOneLeadingHyphen() {
 String input = "--testString";
 String expected = "testString";
 String actual = Util.stripLeadingHyphens(input);
 Assert.assertEquals(expected, actual);
 }

 /**
 * Test for Util.stripLeadingHyphens method with multiple leading hyphens.
 */
 @Test
 public void testStripLeadingHyphensMultipleLeadingHyphens() {
 String input = "---testString";
 String expected = "testString";
 String actual = Util.stripLeadingHyphens(input);
 Assert.assertEquals(expected, actual);
 }

 /**
 * Test for Util.stripLeadingHyphens method with null input.
 */
 @Test
 public void testStripLeadingHyphensNullInput() {
 String input = null;
 String expected = null;
 String actual = Util.stripLeadingHyphens(input);
 Assert.assertEquals(expected, actual);
 }

 /**
 * Test for Util.stripLeadingAndTrailingQuotes method with no quotes.
 */
 @Test
 public void testStripLeadingAndTrailingQuotesNoQuotes() {
 String input = "testString";
 String expected = "testString";
 String actual = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals(expected, actual);
 }

 /**
 * Test for Util.stripLeadingAndTrailingQuotes method with leading quotes.
 */
 @Test
 public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
 String input = "'testString'";
 String expected = "testString";
 String actual = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals(expected, actual);
 }

 /**
 * Test for Util.stripLeadingAndTrailingQuotes method with trailing quotes.
 */
 @Test
 public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
 String input = "testString'";
 String expected = "testString";
 String actual = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals(expected, actual);
 }

 /**
 * Test for Util.stripLeadingAndTrailingQuotes method with leading and trailing quotes.
 */
 @Test
 public void testStripLeadingAndTrailingQuotesLeadingAndTrailingQuotes() {
 String input = "''testString''";
 String expected = "testString";
 String actual = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals(expected, actual);
 }
}
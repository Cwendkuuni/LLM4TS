import org.apache.commons.cli.Util;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UtilTest {

 @Test
 void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--Test";
 expected = "Test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--";
 expected = "";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "test";
 assertEquals("test", Util.stripLeadingHyphens(str));
 }

 @Test
 void testStripLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"test \"";
 expected = "test ";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\" test\"";
 expected = " test";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "test";
 assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
 }

 @Test
 void testEmptyString() {
 String str = "";
 String expected = "";
 assertEquals(expected, Util.stripLeadingHyphens(str));
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
 }

 @Test
 void testNullString() {
 assertThrows(NullPointerException.class, () -> Util.stripLeadingHyphens(null));
 assertThrows(NullPointerException.class, () -> Util.stripLeadingAndTrailingQuotes(null));
 }
}
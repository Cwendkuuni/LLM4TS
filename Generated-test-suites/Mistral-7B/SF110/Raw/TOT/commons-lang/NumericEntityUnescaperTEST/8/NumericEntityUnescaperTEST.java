import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Before
 public void setUp() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 }

 @Test
 public void testTranslateNumericEntityWithSemicolon() {
 String input = "&amp;#x1F600;";
 char[] expectedOutput = {'😃'};
 try (Writer output = new StringWriter()) {
 unescaper.translate(new StringReader(input), 0, output);
 assertEquals(new String(expectedOutput), output.toString());
 }
 }

 @Test
 public void testTranslateNumericEntityWithoutSemicolon() {
 String input = "&amp;#x1F600";
 char[] expectedOutput = {'😃'};
 try (Writer output = new StringWriter()) {
 unescaper.translate(new StringReader(input), 0, output);
 assertEquals(new String(expectedOutput), output.toString());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNumericEntityWithoutSemiColonAndErrorIfNoSemiColon() {
 String input = "&amp;#x1F600";
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 unescaper.translate(new StringReader(input), 0, new StringWriter());
 }

 @Test
 public void testTranslateNumericEntityWithSemicolonButWithoutOPTION_semiColonRequired() {
 String input = "&amp;#x1F600;";
 unescaper = new NumericEntityUnescaper();
 try (Writer output = new StringWriter()) {
 unescaper.translate(new StringReader(input), 0, output);
 assertNull(output.toString());
 }
 }

 @Test
 public void testTranslateNumericEntityWithNonNumericCharacter() {
 String input = "&amp;#abc";
 try (Writer output = new StringWriter()) {
 unescaper.translate(new StringReader(input), 0, output);
 assertEquals("", output.toString());
 }
 }

 @Test
 public void testTranslateNumericEntityWithSpecialCharacterAfterNumericEntity() {
 String input = "&amp;#x1F600&apos;";
 char[] expectedOutput = {'😃', '\''};
 try (Writer output = new StringWriter()) {
 unescaper.translate(new StringReader(input), 0, output);
 assertEquals(new String(expectedOutput), output.toString());
 }
 }

 @Test
 public void testTranslateNumericEntityWithHexEntity() {
 String input = "&amp;#x10FFFF";
 char[] expectedOutput = {'🛍️'};
 try (Writer output = new StringWriter()) {
 unescaper.translate(new StringReader(input), 0, output);
 assertEquals(new String(expectedOutput), output.toString());
 }
 }

 @Test
 public void testTranslateNumericEntityWithDecimalEntity() {
 String input = "&amp;#128448";
 char[] expectedOutput = {'🏴󠁧󠁢󠁳󠁣󠁴󠁿'};
 try (Writer output = new StringWriter()) {
 unescaper.translate(new StringReader(input), 0, output);
 assertEquals(new String(expectedOutput), output.toString());
 }
 }
}
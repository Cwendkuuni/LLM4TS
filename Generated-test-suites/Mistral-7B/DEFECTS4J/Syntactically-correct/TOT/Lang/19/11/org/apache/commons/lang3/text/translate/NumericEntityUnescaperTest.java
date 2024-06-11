import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_ValidNumericEntity_ASCII() throws IOException {
 char[] expected = new char[]{'&', '#', 'A', ';'};
 CharSequence input = "&amp;#65;";
 int length = unescaper.translate(input, 0, new StringBuilder()).toString().toCharArray().length;
 char[] result = new char[length];
 unescaper.translate(input, 0, new StringBuilder(result, length).writer());
 assertEquals(expected, result);
 }

 @Test
 public void testTranslate_ValidNumericEntity_Hex() throws IOException {
 char[] expected = new char[]{'&', '#', 'x', '3', 'A', ';'};
 CharSequence input = "&amp;#x41;";
 int length = unescaper.translate(input, 0, new StringBuilder()).toString().toCharArray().length;
 char[] result = new char[length];
 unescaper.translate(input, 0, new StringBuilder(result, length).writer());
 assertEquals(expected, result);
 }

 @Test
 public void testTranslate_ValidNumericEntity_LargeCharacter() throws IOException {
 char[] expected = new char[]{'&', '#', 'x', '4', 'D', '8', '3', 'D', '7', ';'};
 CharSequence input = "&amp;#x4DC3D7;";
 int length = unescaper.translate(input, 0, new StringBuilder()).toString().toCharArray().length;
 char[] result = new char[length];
 unescaper.translate(input, 0, new StringBuilder(result, length).writer());
 assertEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testTranslate_MalformedNumericEntity() throws IOException {
 CharSequence input = "&amp;#xABC;"; // Invalid Hex constant
 unescaper.translate(input, 0, new StringBuilder());
 }

 @Test(expected = IOException.class)
 public void testTranslate_MissingSemicolon() throws IOException {
 CharSequence input = "&amp;#123;"; // Missing semicolon
 unescaper.translate(input, 0, new StringBuilder());
 }

 @Test(expected = IOException.class)
 public void testTranslate_MalformedInput() throws IOException {
 CharSequence input = "&amp;#1234;abcd"; // Malformed input
 unescaper.translate(input, 0, new StringBuilder());
 }

 @Test
 public void testTranslate_EmptyInput() throws IOException {
 assertEquals("", unescaper.translate("", 0, new StringBuilder()).toString());
 }
}
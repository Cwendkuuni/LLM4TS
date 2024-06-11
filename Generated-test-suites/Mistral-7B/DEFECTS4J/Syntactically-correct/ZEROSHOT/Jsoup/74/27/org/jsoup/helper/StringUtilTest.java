import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("apple", "banana", "cherry");
 String expected = "apple banana cherry";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> strings = Arrays.asList("apple", "banana", "cherry").iterator();
 String expected = "apple banana cherry";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"apple", "banana", "cherry"};
 String expected = "apple banana cherry";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testPadding() {
 for (int i = 0; i <= 20; i++) {
 String expected = StringUtil.padding(i);
 String result = StringUtil.padding(i);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testIsBlank() {
 String empty = "";
 String nullString = null;
 String whitespace = " ";
 String nonBlank = "not blank";

 assertEquals(true, StringUtil.isBlank(empty));
 assertEquals(true, StringUtil.isBlank(nullString));
 assertEquals(true, StringUtil.isBlank(whitespace));
 assertEquals(false, StringUtil.isBlank(nonBlank));
 }

 @Test
 public void testIsNumeric() {
 String number = "12345";
 String notNumber = "abcde";
 String empty = "";
 String nullString = null;

 assertEquals(true, StringUtil.isNumeric(number));
 assertEquals(false, StringUtil.isNumeric(notNumber));
 assertEquals(false, StringUtil.isNumeric(empty));
 assertEquals(false, StringUtil.isNumeric(nullString));
 }

 @Test
 public void testIsWhitespace() {
 char space = ' ';
 char tab = '\t';
 char newline = '\n';
 char formFeed = '\f';
 char carriageReturn = '\r';

 assertEquals(true, StringUtil.isWhitespace(space));
 assertEquals(true, StringUtil.isWhitespace(tab));
 assertEquals(true, StringUtil.isWhitespace(newline));
 assertEquals(true, StringUtil.isWhitespace(formFeed));
 assertEquals(true, StringUtil.isWhitespace(carriageReturn));
 assertEquals(false, StringUtil.isWhitespace('a'));
 }

 @Test
 public void testIsActuallyWhitespace() {
 char space = ' ';
 char tab = '\t';
 char newline = '\n';
 char formFeed = '\f';
 char carriageReturn = '\r';
 char nonBreakingSpace = '\u00A0'; // non-breaking space

 assertEquals(true, StringUtil.isActuallyWhitespace(space));
 assertEquals(true, StringUtil.isActuallyWhitespace(tab));
 assertEquals(true, StringUtil.isActuallyWhitespace(newline));
 assertEquals(true, StringUtil.isActuallyWhitespace(formFeed));
 assertEquals(true, StringUtil.isActuallyWhitespace(carriageReturn));
 assertEquals(true, StringUtil.isActuallyWhitespace(nonBreakingSpace));
 }

 @Test
 public void testNormaliseWhitespace() {
 String multiSpaces = "hello     world   ";
 String expected = "hello world";
 String result = StringUtil.normaliseWhitespace(multiSpaces);
 assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String needle = "apple";
 String[] haystack = {"apple", "banana", "cherry"};
 assertTrue(StringUtil.in(needle, haystack));
 }

 @Test
 public void testInSorted() {
 String needle = "apple";
 String[] haystack = {"banana", "cherry", "apple"};
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String base = "https://example.com/";
 String relUrl = "/path/to/file.html";
 String expected = "https://example.com/path/to/file.html";
 URL result = StringUtil.resolve(new URL(base), relUrl);
 assertEquals(expected, result.toExternalForm());
 }

}
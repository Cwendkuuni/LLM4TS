import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 // Test for isNamedEntity method
 @Test
 public void testIsNamedEntity() {
 Map<Character, String> fullMap = Entities.full;
 char lt = Entities.getCharacterByName("lt");

 // Basic test case
 Assert.assertTrue("lt should be a named entity", Entities.isNamedEntity("lt"));

 // Edge cases
 // Check for empty string
 Assert.assertFalse("Empty string should not be a named entity", Entities.isNamedEntity(""));
 // Check for null name
 Assert.assertFalse("Null name should not be a named entity", Entities.isNamedEntity(null));
 // Check for invalid named entity
 Assert.assertFalse("Invalid named entity should not be a named entity", Entities.isNamedEntity("invalid"));

 // Exception handling
 // Check for IllegalArgumentException when name is not a String
 Assert.assertThrows("IllegalArgumentException when name is not a String", IllegalArgumentException.class, () -> Entities.isNamedEntity(123));
 }

 // Test for isBaseNamedEntity method
 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> baseMap = Entities.base;
 char amp = Entities.getCharacterByName("amp");

 // Basic test case
 Assert.assertTrue("amp should be a base named entity", Entities.isBaseNamedEntity("amp"));

 // Edge cases
 // Check for empty string
 Assert.assertFalse("Empty string should not be a base named entity", Entities.isBaseNamedEntity(""));
 // Check for null name
 Assert.assertFalse("Null name should not be a base named entity", Entities.isBaseNamedEntity(null));
 // Check for invalid named entity
 Assert.assertFalse("Invalid named entity should not be a base named entity", Entities.isBaseNamedEntity("invalid"));

 // Exception handling
 // Check for IllegalArgumentException when name is not a String
 Assert.assertThrows("IllegalArgumentException when name is not a String", IllegalArgumentException.class, () -> Entities.isBaseNamedEntity(123));
 }

 // Test for getCharacterByName method
 @Test
 public void testGetCharacterByName() {
 Map<Character, String> fullMap = Entities.full;
 Map<Character, String> baseMap = Entities.base;
 Map<Character, String> xhtmlByValMap = Entities.xhtmlByVal;

 // Basic test case
 Character lt = Entities.getCharacterByName("lt");
 Assert.assertEquals("lt should return <", lt.charValue(), '<');

 // Edge cases
 // Check for null name
 Assert.assertNull("Null name should return null", Entities.getCharacterByName(null));
 // Check for empty string
 Assert.assertNull("Empty string should return null", Entities.getCharacterByName(""));
 // Check for invalid named entity
 Assert.assertNull("Invalid named entity should return null", Entities.getCharacterByName("invalid"));

 // Test for xhtmlByVal
 Assert.assertEquals("quot should return '", Entities.getCharacterByName("quot"), '\u00022');
 Assert.assertEquals("amp should return '&'", Entities.getCharacterByName("amp"), '\u00026');
 Assert.assertEquals("lt should return '<'", Entities.getCharacterByName("lt"), '\u0003C');
 Assert.assertEquals("gt should return '>',", Entities.getCharacterByName("gt"), '\u0003E');

 // Exception handling
 // Check for IllegalArgumentException when name is not a String
 Assert.assertThrows("IllegalArgumentException when name is not a String", IllegalArgumentException.class, () -> Entities.getCharacterByName(123));
 }

 // Test for escape method
 @Test
 public void testEscape() {
 StringBuilder accum = new StringBuilder();
 String string = "Hello <world>! & it's 安慰 & good!";
 Document.OutputSettings out = new Document.OutputSettings();
 out.setEscapeMode(Entities.EscapeMode.xhtml);

 // Basic test case
 Entities.escape(accum, string, out, false, false, false);
 String expected = "Hello &lt;world&gt;! &amp; it's &#x7F83;&#x5B50; &amp;good!";
 Assert.assertEquals("escaped string should match expected", expected, accum.toString());

 // Edge cases
 // Check for empty string
 accum.setLength(0);
 Entities.escape(accum, "", out, false, false, false);
 Assert.assertEquals("escaped empty string should be empty", "", accum.toString());
 // Check for null string
 Entities.escape(accum, null, out, false, false, false);
 Assert.assertEquals("escaped null string should be empty", "", accum.toString());
 // Check for invalid Unicode character (surrogate pair)
 String invalidUnicode = "\uD800\uDC00";
 accum.setLength(0);
 Entities.escape(accum, invalidUnicode, out, false, false, false);
 Assert.assertEquals("escaped invalid Unicode character should be replaced with \uD800\uDC00", "\uD800\uDC00", accum.toString());

 // Exception handling
 // Check for IllegalArgumentException when string is null
 Assert.assertThrows("IllegalArgumentException when string is null", IllegalArgumentException.class, () -> Entities.escape(accum, null, out, false, false, false));
 }

 // Test for unescape method
 @Test
 public void testUnescape() {
 StringBuilder accum = new StringBuilder();
 String string = "&lt;Hello &amp; World&gt;!";

 // Basic test case
 Entities.unescape(accum, string);
 String expected = "<Hello World!>";
 Assert.assertEquals("unescaped string should match expected", expected, accum.toString());

 // Edge cases
 // Check for empty string
 accum.setLength(0);
 Entities.unescape(accum, "");
 Assert.assertEquals("unescaped empty string should be empty", "", accum.toString());
 // Check for null string
 Entities.unescape(accum, null);
 Assert.assertEquals("unescaped null string should be empty", "", accum.toString());

 // Test for strict mode
 String stringWithTrailingSemicolon = "&lt;Hello &amp; World&gt;!;";
 accum.setLength(0);
 Entities.unescape(accum, stringWithTrailingSemicolon, true);
 String expectedWithSemicolon = "<Hello World!>";
 Assert.assertEquals("unescaped string with trailing semicolon should match expected", expectedWithSemicolon, accum.toString());

 // Test for invalid entities
 accum.setLength(0);
 String invalidEntityString = "&x &y &z";
 Entities.unescape(accum, invalidEntityString);
 Assert.assertEquals("unescaped invalid entities should be unchanged", invalidEntityString, accum.toString());
 }

 // Test for canEncode method
 @Test
 public void testCanEncode() {
 CoreCharset utf = CoreCharset.utf;
 CharsetEncoder fallback = StandardCharsets.UTF_8.newEncoder();

 // Basic test case
 Assert.assertTrue("UTF-8 can encode ASCII character 'A'", Entities.canEncode(utf, 'A', fallback));

 // Edge cases
 // Check for non-ASCII character
 Assert.assertFalse("UTF-8 cannot encode non-ASCII character 'é'", Entities.canEncode(utf, (char) 0xE9, fallback));
 // Check for surrogate pair
 Assert.assertFalse("UTF-8 cannot encode surrogate pair \uD800\uDC00", Entities.canEncode(utf, '\uD800', fallback));
 // Check for character outside BMP
 Assert.assertFalse("UTF-8 cannot encode character \u1F123456", Entities.canEncode(utf, '\u1F123456', fallback));

 // Exception handling
 // Check for IllegalArgumentException when char is not a character
 Assert.assertThrows("IllegalArgumentException when char is not a character", IllegalArgumentException.class, () -> Entities.canEncode(utf, 12345, fallback));
 }
}
import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Testing for known named entities in the full entity set
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("gt"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("quot"));

 // Testing for unknown named entities
 assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Testing for known named entities in the base entity set
 assertTrue(Entities.isBaseNamedEntity("lt"));
 assertTrue(Entities.isBaseNamedEntity("amp"));

 // Testing for unknown named entities
 assertFalse(Entities.isBaseNamedEntity("quot"));
 }

 @Test
 public void testGetCharacterByName() {
 // Testing for known named entities in the full entity set
 assertEquals(Integer.toHexString(0x0003C).charAt(0), Entities.getCharacterByName("lt"));
 assertEquals(Integer.toHexString(0x0003E).charAt(0), Entities.getCharacterByName("gt"));
 assertEquals(Integer.toHexString(0x00026).charAt(0), Entities.getCharacterByName("amp"));
 assertEquals(Integer.toHexString(0x00022).charAt(0), Entities.getCharacterByName("quot"));

 // Testing for unknown named entities
 assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 // Testing escaping a simple string
 StringBuilder expected = new StringBuilder();
 Entities.escape(expected, "&amp;lt;abc&gt;quot;", null);
 String actual = expected.toString();
 assertEquals("&amp;lt;abc&gt;quot;", actual);

 // Testing escaping a whitespace only string
 StringBuilder expectedWhitespace = new StringBuilder();
 Entities.escape(expectedWhitespace, "   ", null);
 String actualWhitespace = expectedWhitespace.toString();
 assertEquals(" &nbsp; &nbsp; &nbsp; ", actualWhitespace);

 // Testing escaping a string with special characters
 StringBuilder expectedSpecial = new StringBuilder();
 Entities.escape(expectedSpecial, "&amp;lt;<&gt;>\"&amp;quot;", null);
 String actualSpecial = expectedSpecial.toString();
 assertEquals("&amp;lt;&amp;lt;&gt;&gt;&gt;&quot;&amp;quot;", actualSpecial);
 }

 @Test
 public void testUnescape() {
 // Testing unescaping a simple string
 String input = "&amp;lt;abc&gt;quot;";
 String expected = "<abc\"";
 String actual = Entities.unescape(input);
 assertEquals(expected, actual);

 // Testing unescaping a string with special characters
 String inputSpecial = "&amp;lt;&amp;lt;&gt;&gt;&gt;&quot;&amp;quot;";
 String expectedSpecial = "<<>>\"\"";
 String actualSpecial = Entities.unescape(inputSpecial);
 assertEquals(expectedSpecial, actualSpecial);
 }
}
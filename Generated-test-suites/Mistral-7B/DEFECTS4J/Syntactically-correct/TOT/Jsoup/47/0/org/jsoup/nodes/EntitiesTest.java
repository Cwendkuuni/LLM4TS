import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Testing for known named entities in the full entity set
 assertEquals(true, Entities.isNamedEntity("lt"));
 assertEquals(true, Entities.isNamedEntity("amp"));
 assertEquals(true, Entities.isNamedEntity("quot"));

 // Testing for unknown named entities
 assertEquals(false, Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Testing for known named entities in the base entity set
 assertEquals(true, Entities.isBaseNamedEntity("amp"));
 assertEquals(true, Entities.isBaseNamedEntity("lt"));
 assertEquals(true, Entities.isBaseNamedEntity("quot"));

 // Testing for unknown named entities
 assertEquals(false, Entities.isBaseNamedEntity("gt"));
 }

 @Test
 public void testGetCharacterByName() {
 // Testing for known named entities in the full entity set
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Testing for unknown named entities
 assertNull(Entities.getCharacterByName("gt"));
 }

 @Test
 public void testEscape() {
 // Testing basic cases
 String input = "&amp;<lt>Hello &quot;world&quot; ><gt>!";
 String expectedOutput = "&amp;amp;<amp>&lt;Hello &amp;quot;world&amp;quot; &gt;<amp>&gt;!";
 StringBuilder actualOutput = new StringBuilder();
 Entities.escape(actualOutput, input, null, false, false, false);
 assertEquals(expectedOutput, actualOutput.toString());

 // Testing escaping within an attribute value
 String inputWithAttribute = "tag attr=\"&amp;<lt>Hello &quot;world&quot; ><gt>!\"";
 String expectedOutputWithAttribute = "tag attr=\"&amp;amp;<amp>&lt;Hello &amp;quot;world&amp;quot; &gt;<amp>&gt;!\"";
 StringBuilder actualOutputWithAttribute = new StringBuilder();
 Entities.escape(actualOutputWithAttribute, inputWithAttribute, null, true, false, false);
 assertEquals(expectedOutputWithAttribute, actualOutputWithAttribute.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testEscapeWithNullString() {
 StringBuilder accum = new StringBuilder();
 Entities.escape(accum, null, null, false, false, false);
 }

 @Test
 public void testUnescape() {
 // Testing basic cases
 String input = "&amp;amp;<amp>&lt;Hello &amp;quot;world&amp;quot; &gt;<amp>&gt;!";
 String expectedOutput = "&amp;<lt>Hello &quot;world&quot; ><gt>!";
 String actualOutput = Entities.unescape(input, false);
 assertEquals(expectedOutput, actualOutput);

 // Testing strict unescaping
 String inputStrict = "&amp;amp;<amp>&lt;Hello &amp;quot;world&amp;quot; &gt;<amp>&gt;!&";
 String expectedOutputStrict = "&lt;Hello &quot;world&quot; >!";
 String actualOutputStrict = Entities.unescape(inputStrict, true);
 assertEquals(expectedOutputStrict, actualOutputStrict);
 }

 @Test(expected = NullPointerException.class)
 public void testUnescapeWithNullString() {
 String input = null;
 String output = Entities.unescape(input, false);
 }
}
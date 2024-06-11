import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities in the full entity set
 assertEquals('<', Entities.getCharacterByName("lt").charValue());
 assertEquals('&', Entities.getCharacterByName("amp").charValue());

 // Test for unknown named entities
 assertFalse(Entities.isNamedEntity("random"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test for known named entities in the base entity set
 assertTrue(Entities.isBaseNamedEntity("lt"));
 assertTrue(Entities.isBaseNamedEntity("amp"));

 // Test for unknown named entities
 assertFalse(Entities.isBaseNamedEntity("random"));
 }

 @Test
 public void testEscape() {
 // Test escaping a simple string
 String input = "Hello & World! <greeting xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
 StringBuilder expected = new StringBuilder();
 Entities.escape(expected, input, Document.OutputSettings.base(), false, false, false);
 String output = expected.toString();
 assertEquals("Hello &amp; World! &lt;greeting xmlns:xsi=\"&amp;http://www.w3.org/2001/XMLSchema-instance\"&gt;", output);

 // Test escaping a string with surrogate pairs
 String inputWithSurrogate = "\uD835\uDC00\uD835\uDC01";
 StringBuilder expectedWithSurrogate = new StringBuilder();
 Entities.escape(expectedWithSurrogate, inputWithSurrogate, Document.OutputSettings.base(), false, false, false);
 String outputWithSurrogate = expectedWithSurrogate.toString();
 assertEquals("&#x10000;&#x10001;", outputWithSurrogate);
 }

 @Test
 public void testUnescape() {
 // Test unescaping a simple string
 String input = "Hello &amp; World! &lt;greeting xmlns:xsi=\"&amp;http://www.w3.org/2001/XMLSchema-instance\"&gt;";
 String output = Entities.unescape(input, false);
 assertEquals("Hello & World! <greeting xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">", output);

 // Test unescaping a string with surrogate pairs
 String inputWithSurrogate = "&#x10000;&#x10001;";
 String outputWithSurrogate = Entities.unescape(inputWithSurrogate, false);
 assertEquals("\uD835\uDC00\uD835\uDC01", outputWithSurrogate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithInvalidOutputSettings() {
 StringBuilder accum = new StringBuilder();
 Entities.escape(accum, "test", new Document.OutputSettings("invalidOutputSetting"), false, false, false);
 }
}
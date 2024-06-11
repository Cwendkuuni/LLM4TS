import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character entity = entry.getKey();
            String name = entry.getValue();
            assertTrue("Named Entity '" + name + "' not recognized as named entity.", Entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character entity = entry.getKey();
            String name = entry.getValue();
            assertTrue("Named Entity '" + name + "' not recognized as base named entity.", Entities.isBaseNamedEntity(name));
        }

        // Test for entities not in the base set
        assertFalse("Named Entity 'lt' recognized as base named entity when it is not.", Entities.isBaseNamedEntity("lt"));
        assertFalse("Named Entity 'amp' recognized as base named entity when it is not.", Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character entity = entry.getKey();
            String name = entry.getValue();
            Character charValue = Entities.getCharacterByName(name);
            assertEquals("Named Entity '" + name + "' does not return the expected character.", entity, charValue);
        }

        // Test for unknown entities
        assertNull("getCharacterByName() should return null for unknown entities.", Entities.getCharacterByName("unkownEntity"));
    }

    @Test
    public void testEscape() {
        String testString = "<amp>&lt; &gt; \" &quot; &amp; &amp; &amp;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String escapedString = Entities.escape(testString, outputSettings);

        String expectedEscapedString = "&amp;lt; &amp;amp; &amp;gt; &amp;quot; &amp;amp; &amp;amp; &amp;amp;";
        assertEquals("Escape method not working as expected.", expectedEscapedString, escapedString);

        // Test for edge cases: escaping surrogate pairs and special characters
        CharsetEncoder encoder = Character.encodingForName("UTF-8");
        String testStringWithSurrogatePair = "👋";
        String escapedStringWithSurrogatePair = Entities.escape(testStringWithSurrogatePair, outputSettings);
        assertTrue("Escape method should be able to escape surrogate pairs.", escapedStringWithSurrogatePair.contains("&#x"));

        String testStringWithSpecialCharacters = "!\"#$%&'()*+,;<=>?@[\\]^_`{|}~";
        String escapedStringWithSpecialCharacters = Entities.escape(testStringWithSpecialCharacters, outputSettings);
        assertTrue("Escape method should be able to escape special characters.", escapedStringWithSpecialCharacters.contains("&#x"));
    }

    @Test
    public void testUnescape() {
        String testString = "&amp;lt; &amp;amp; &amp;gt; &amp;quot; &amp;amp; &amp;amp; &amp;amp;";
        String unescapedString = Entities.unescape(testString, false);

        String expectedUnescapedString = "<amp>&amp; &gt; \" &amp; &amp; &amp;";
        assertEquals("Unescape method not working as expected.", expectedUnescapedString, unescapedString);

        // Test for edge cases: unescaping special characters
        String testStringWithSpecialCharacters = "&amp;#x20AC; &amp;#x00A0; &amp;#x003C; &amp;#x003E; &amp;#x221D; &amp;#x221A; &amp;#xA0;";
        String unescapedStringWithSpecialCharacters = Entities.unescape(testStringWithSpecialCharacters, false);

        String expectedUnescapedStringWithSpecialCharacters = "€   < > “ “";
        assertEquals("Unescape method not working as expected.", expectedUnescapedStringWithSpecialCharacters, unescapedStringWithSpecialCharacters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithStrictFalseAndInvalidEncoding() {
        String testString = "&amp;lt; &amp;amp; &amp;gt; &amp;quot; &amp;amp; &amp;amp; &amp;amp; &amp;#xABCD";
        Entities.unescape(testString, false);
    }

    @Test
    public void testUnescapeWithStrictTrue() {
        String testString = "&amp;lt; &amp;amp; &amp;gt; &amp;quot; &amp;amp; &amp;amp; &amp;amp; &amp;#xABCD";
        String unescapedString = Entities.unescape(testString, true);

        String expectedUnescapedString = "<amp>&amp; &gt; \" &amp; &amp; &amp; &#xABCD;";
        assertEquals("Unescape method not working as expected.", expectedUnescapedString, unescapedString);
    }
}
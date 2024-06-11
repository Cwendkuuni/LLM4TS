import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("unknown"));

        // Test for invalid inputs
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known named entities in base set
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("gt")); // not in base set
        assertFalse(Entities.isBaseNamedEntity("unknown"));

        // Test for invalid inputs
        assertFalse(Entities.isBaseNamedEntity(null));
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test for invalid inputs
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "& < > \" &amp; &lt; &gt; &quot;";
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("&amp; &amp;lt; &amp;gt; &amp;quot; &amp;amp; &amp;lt; &amp;gt; &amp;quot;");

        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, null, false, false, false);

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullInput() {
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, null, null, false, false, false);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt; &amp;gt; &amp;quot; &amp;amp;";
        String expectedOutput = "< > \" &";

        String actualOutput = Entities.unescape(input, false);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCanEncode() {
        // Test for ASCII characters
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'a', null));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '0', null));

        // Test for non-ASCII characters
        assertTrue(Entities.canEncode(CoreCharset.utf, 'é', null));
        assertTrue(Entities.canEncode(CoreCharset.utf, '😃', null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithNullCharset() {
        assertFalse(Entities.canEncode(null, 'A', null));
    }

    @Test
    public void testCanEncodeWithInvalidCharacter() {
        assertFalse(Entities.canEncode(CoreCharset.ascii, 0xfffd, null));
    }
}
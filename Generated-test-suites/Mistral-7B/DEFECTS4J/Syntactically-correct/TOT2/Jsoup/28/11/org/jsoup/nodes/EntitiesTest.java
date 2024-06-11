import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test with known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));

        // Test with invalid names
        assertFalse(Entities.isNamedEntity("invalidName"));
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        Character expected = Entities.getCharacterByName("lt");
        assertEquals('<', expected);

        expected = Entities.getCharacterByName("amp");
        assertEquals('&', expected);

        // Test with invalid names
        assertNull(Entities.getCharacterByName("invalidName"));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = mock(Document.OutputSettings.class);
        String input = "&amp; <>& '\"";
        String expected = "&amp; &amp;lt; &amp;gt; &amp;amp; &amp;apos; &amp;quot;";

        when(outputSettings.encoder()).thenReturn(mock(CharsetEncoder.class));
        when(outputSettings.escapeMode()).thenReturn(Entities.EscapeMode.extended);

        String output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        verify(outputSettings).encoder();
        verify(outputSettings).escapeMode();
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;Hello&amp;quot; World&amp;gt;";
        String expected = "<Hello\" World>";

        String output = Entities.unescape(input, false);
        assertEquals(expected, output);

        String outputStrict = Entities.unescape(input, true);
        assertEquals("<lt>Hello<quot> World<gt>", outputStrict);
    }
}
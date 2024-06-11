import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> fullEntities = entities.getFullMap();

        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            assertTrue(entities.isNamedEntity(name));
        }

        // Test with non-existent entity
        assertFalse(entities.isNamedEntity("nonExistentEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> baseEntities = entities.getBaseMap();

        for (Object[] entity : Entities.xhtmlArray) {
            Character key = Character.valueOf((char) ((Integer) entity[1]).intValue());
            String name = (String) entity[0];
            assertTrue(entities.isBaseNamedEntity(name));
        }

        // Test with non-existent entity in base set
        assertFalse(entities.isBaseNamedEntity("nonExistentEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<Character, String> fullEntities = entities.getFullMap();

        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            Character result = entities.getCharacterByName(name);
            assertEquals(key, result);
        }

        // Test with non-existent entity
        assertNull(entities.getCharacterByName("nonExistentEntity"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        String input = "&lt;<>&quot;\"&gt;>";
        entities.escape(accum, input, null, false, false, false);
        String expectedOutput = "&amp;lt;&lt;amp;&amp;gt;&gt;amp;quot;&quot;";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testEscapeWithAttribute() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        String input = "<script>alert('Hello World')</script>";
        entities.escape(accum, input, null, true, false, false);
        String expectedOutput = "&lt;script&gt;alert('Hello World')&lt;/script&gt;";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testEscapeWithNormalizeWhite() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        String input = "\t <script>alert('Hello World')</script>\n";
        entities.escape(accum, input, null, false, true, false);
        String expectedOutput = "\t &nbsp;<script>&gt;alert('Hello World')&lt;/script>&#xA;";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testEscapeWithStripLeadingWhite() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        String input = "\t <script>alert('Hello World')</script>\n";
        entities.escape(accum, input, null, false, true, true);
        assertEquals("", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullInput() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        entities.escape(accum, null, null, false, false, false);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();

        String input = "&amp;lt;&lt;amp;&amp;gt;&gt;amp;quot;&quot;";
        String expectedOutput = "&lt;<>&gt;";
        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset charset = CoreCharset.utf;

        // Test ASCII characters
        for (char c = 0x20; c <= 0x7E; c++) {
            assertTrue(entities.canEncode(charset, c, null));
        }

        // Test non-ASCII characters
        for (char c = 0xA0; c <= 0x10FFFF; c++) {
            if (c > 0x10000) {
                assertTrue(entities.canEncode(charset, c, null));
            } else {
                String hexString = Integer.toHexString(c);
                if (hexString.length() % 2 == 1) {
                    hexString = "0" + hexString;
                }
                String testString = new String(Character.toChars(c));
                boolean canEncode = entities.canEncode(charset, c, null);
                if (canEncode) {
                    assertTrue(charset.canEncode(testString));
                } else {
                    assertFalse(charset.canEncode(testString));
                }
            }
        }
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();
        Map<String, Character> result = entities.loadEntities("entities-full.properties");

        Map<String, Character> expectedMap = entities.getFullMap();

        assertEquals(expectedMap.size(), result.size());
        Arrays.stream(expectedMap.entrySet().toArray()).forEach(entry -> assertEquals(entry.getValue(), result.get(entry.getKey())));
    }
}
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        // Test positive cases
        Assert.assertTrue(entities.isNamedEntity("lt"));
        Assert.assertTrue(entities.isNamedEntity("amp"));
        Assert.assertTrue(entities.isNamedEntity("quot"));
        Assert.assertTrue(entities.isNamedEntity("nbsp"));
        // Test negative cases
        Assert.assertFalse(entities.isNamedEntity(""));
        Assert.assertFalse(entities.isNamedEntity(null));
        Assert.assertFalse(entities.isNamedEntity("InvalidEntity"));
        Assert.assertFalse(entities.isNamedEntity("&")); // entity reference, not a named entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        // Test positive cases
        Assert.assertTrue(entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(entities.isBaseNamedEntity("amp"));
        Assert.assertTrue(entities.isBaseNamedEntity("quot"));
        Assert.assertFalse(entities.isBaseNamedEntity("nbsp")); // not present in base set
        // Test negative cases
        Assert.assertFalse(entities.isBaseNamedEntity(""));
        Assert.assertFalse(entities.isBaseNamedEntity(null));
        Assert.assertFalse(entities.isBaseNamedEntity("InvalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        // Test valid named entities
        Assert.assertEquals('<', entities.getCharacterByName("lt"));
        Assert.assertEquals('&', entities.getCharacterByName("amp"));
        Assert.assertEquals('"', entities.getCharacterByName("quot"));
        Assert.assertEquals('\u00A0', entities.getCharacterByName("nbsp"));
        // Test invalid named entity
        Assert.assertNull(entities.getCharacterByName("InvalidEntity"));
        // Test empty and null inputs
        Assert.assertNull(entities.getCharacterByName(""));
        Assert.assertNull(entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeMethod() {
        Entities entities = new Entities();
        String input = "Hello & World! <greet xmlns:my=\"http://example.com/greetings\">Hello <user name=\"John\"/></greet>";
        StringBuilder expectedOutput = new StringBuilder();
        entities.escape(expectedOutput, input, null, false, false, false);
        String output = expectedOutput.toString();
        String expectedResult = "Hello &amp; World! &lt;greet xmlns:my=&quot;http://example.com/greetings&quot;&gt;Hello &lt;user name=&quot;John&quot;&gt;</greet>";
        Assert.assertEquals(expectedResult, output);

        // Test edge cases
        String inputWithSurrogateCharacters = "👋 Hello & World! <greet xmlns:my=\"http://example.com/greetings\">Hello <user name=\"John\"/></greet>";
        StringBuilder expectedOutputWithSurrogates = new StringBuilder();
        entities.escape(expectedOutputWithSurrogates, inputWithSurrogateCharacters, null, false, false, false);
        String outputWithSurrogates = expectedOutputWithSurrogates.toString();
        Assert.assertEquals(expectedResult, outputWithSurrogates);

        // Test error cases: unsupported encoding and invalid input
        Assert.assertThrows(IllegalArgumentException.class, () -> entities.escape(null, "Invalid Input", null, false, false, false));
        Assert.assertThrows(IllegalArgumentException.class, () -> entities.escape(new StringBuilder(), null, null, false, false, null));
        Assert.assertThrows(IllegalArgumentException.class, () -> entities.escape(new StringBuilder(), null, null, false, null, null));
    }

    @Test
    public void testUnescapeMethod() {
        Entities entities = new Entities();
        String input = "Hello &amp; World! &lt;greet xmlns:my=&quot;http://example.com/greetings&quot;&gt;Hello &lt;user name=&quot;John&quot;&gt;</greet>";
        String output = entities.unescape(input);
        String expectedResult = "Hello & World! <greet xmlns:my=\"http://example.com/greetings\">Hello <user name=\"John\">";
        Assert.assertEquals(expectedResult, output);

        // Test edge cases
        String inputWithSurrogateCharacters = "👋 Hello &amp; World! &lt;greet xmlns:my=&quot;http://example.com/greetings&quot;&gt;Hello &lt;user name=&quot;John&quot;&gt;</greet>";
        String outputWithSurrogates = entities.unescape(inputWithSurrogateCharacters, true);
        Assert.assertEquals("👋 Hello World! <greet xmlns:my=\"http://example.com/greetings\">Hello <user name=\"John\">", outputWithSurrogates);

        // Test error cases: unsupported encoding and invalid input
        Assert.assertThrows(IllegalArgumentException.class, () -> entities.unescape(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> entities.unescape("Invalid Input"));
        Assert.assertThrows(IllegalArgumentException.class, () -> entities.unescape("", true));
    }
}
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("apos"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        Character ltEntity = Entities.getCharacterByName("lt");
        Character ampEntity = Entities.getCharacterByName("amp");
        Character aposEntity = Entities.getCharacterByName("apos");
        Character quotEntity = Entities.getCharacterByName("quot");

        Assert.assertEquals('<', ltEntity);
        Assert.assertEquals('&', ampEntity);
        Assert.assertEquals('\'', aposEntity);
        Assert.assertEquals('"', quotEntity);
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String inputString = "Hello & World! < Greetings >";
        String expectedEscaped = "Hello &amp; World! &lt; Greetings &gt;";

        String result = Entities.escape(inputString, outputSettings);
        Assert.assertEquals(expectedEscaped, result);
    }

    @Test
    public void testUnescape() {
        String inputString = "Hello &amp; World! &lt; Greetings &gt;";
        String expectedUnescaped = "Hello & World! < Greetings >";

        String result = Entities.unescape(inputString);
        Assert.assertEquals(expectedUnescaped, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullInput() {
        String inputString = null;
        Entities.unescape(inputString);
    }

    @Test
    public void testIsNamedEntityWithEmptyString() {
        Assert.assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByNameWithUnknownEntity() {
        Assert.assertNull(Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testLoadEntitiesFromPropertiesFile() {
        Map<String, Character> entities = Entities.loadEntities("entities-test.properties");
        Character entity = entities.get("testEntity");

        Assert.assertNotNull(entity);
        Assert.assertEquals('testEntityCharacter', entity);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesFromNonExistentPropertiesFile() {
        Entities.loadEntities("nonexistent.properties");
    }
}
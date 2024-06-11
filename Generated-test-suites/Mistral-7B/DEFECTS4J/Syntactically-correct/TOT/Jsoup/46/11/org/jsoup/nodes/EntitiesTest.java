import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.fullByVal;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            boolean result = Entities.isNamedEntity(entityName);
            Assert.assertTrue("Named entity '" + entityName + "' should be known", result);
            Assert.assertEquals("Named entity '" + entityName + "' should map to character " + entity,
                    entity, Entities.getCharacterByName(entityName));
        }

        Map<String, Character> baseEntities = Entities.baseByVal;
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            boolean result = Entities.isBaseNamedEntity(entityName);
            Assert.assertTrue("Base named entity '" + entityName + "' should be known", result);
            Assert.assertEquals("Base named entity '" + entityName + "' should map to character " + entity,
                    entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsBaseNamedEntity_UnknownEntity() {
        String unknownEntity = "unkownEntity";
        boolean result = Entities.isBaseNamedEntity(unknownEntity);
        Assert.assertFalse("Unknown named entity '" + unknownEntity + "' should not be known", result);
    }

    @Test
    public void testGetCharacterByName_UnknownEntity() {
        String unknownEntity = "unkownEntity";
        Character result = Entities.getCharacterByName(unknownEntity);
        Assert.assertNull("Unknown named entity '" + unknownEntity + "' should return null", result);
    }

    @Test
    public void testEscape() {
        String input = "&lt;foo &amp; bar &quot;quoted&quot; &gt;baz &apos;apostrophe&apos; &nbsp; tab &a0;";
        StringBuilder expectedOutput = new StringBuilder();
        Entities.escape(expectedOutput, input, Document.OutputSettings.base(), false, false, false);
        String output = Entities.escape(input, Document.OutputSettings.base());
        Assert.assertEquals("Escaped string should match expected output", expectedOutput.toString(), output);

        StringBuilder expectedOutputXhtml = new StringBuilder();
        Entities.escape(expectedOutputXhtml, input, Document.OutputSettings.xhtml(), false, false, false);
        String outputXhtml = Entities.escape(input, Document.OutputSettings.xhtml());
        Assert.assertEquals("Escaped XHTML string should match expected output", expectedOutputXhtml.toString(), outputXhtml);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;foo&gt; &amp; bar &quot;quoted&quot; &gt;baz&apos; &nbsp; tab &a0;";
        String expectedOutput = "&lt;foo&gt; &amp; bar &quot;quoted&quot; &gt;baz&apos; &nbsp; tab &a0;";
        String output = Entities.unescape(input, false);
        Assert.assertEquals("Unescaped string should match input", expectedOutput, output);

        String outputStrict = Entities.unescape(input, true);
        Assert.assertEquals("Unescaped string (strict) should match input", expectedOutput, outputStrict);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntities_MissingFile() {
        Entities.loadEntities("missingEntities.properties");
    }
}
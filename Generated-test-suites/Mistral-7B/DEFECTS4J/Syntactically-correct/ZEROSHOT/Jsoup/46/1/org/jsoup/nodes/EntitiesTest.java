package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> full = entities.getFullMap();

        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            boolean result = entities.isNamedEntity(name);
            boolean expected = true;
            Assert.assertEquals("Unexpected result for named entity: " + name, expected, result);
        }

        String invalidName = "invalidEntity";
        boolean result = entities.isNamedEntity(invalidName);
        boolean expected = false;
        Assert.assertEquals("Unexpected result for invalid named entity: " + invalidName, expected, result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> base = entities.getBaseMap();

        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            boolean result = entities.isBaseNamedEntity(name);
            boolean expected = true;
            Assert.assertEquals("Unexpected result for base named entity: " + name, expected, result);

            result = entities.isBaseNamedEntity(character.toString());
            expected = true;
            Assert.assertEquals("Unexpected result for base named entity: " + character, expected, result);
        }

        String invalidName = "invalidEntity";
        boolean result = entities.isBaseNamedEntity(invalidName);
        boolean expected = false;
        Assert.assertEquals("Unexpected result for invalid base named entity: " + invalidName, expected, result);
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<String, Character> full = entities.getFullMap();

        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Character result = entities.getCharacterByName(name);
            Assert.assertEquals("Unexpected result for named entity: " + name, character, result);
        }

        String invalidName = "invalidEntity";
        Character result = entities.getCharacterByName(invalidName);
        Assert.assertNull("Unexpected result for invalid named entity: " + invalidName, result);
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();

        String input = "&amp;<lt>>&quot;";
        String expectedOutput = "&amp;amp;(&lt;)&quot;(&gt;)";

        StringBuilder actualOutput = new StringBuilder();
        entities.escape(actualOutput, input, null, false, false, false);

        String result = actualOutput.toString();
        Assert.assertEquals("Unexpected output for escape method", expectedOutput, result);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();

        String input = "&amp;amp;(&lt;)&quot;(&gt;)";
        String expectedOutput = "&amp;<lt>&quot;>";

        StringBuilder actualOutput = new StringBuilder();
        entities.escape(actualOutput, input, null, false, false, false);
        String result = entities.unescape(actualOutput.toString(), false);

        Assert.assertEquals("Unexpected output for unescape method", expectedOutput, result);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();

        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = null; // not used in this test

        for (char c = 0x00; c <= 0x7F; c++) {
            boolean result = entities.canEncode(coreCharset, c, encoder);
            boolean expected = true;
            Assert.assertEquals("Unexpected result for canEncode method for ASCII character: " + (int) c, expected, result);
        }

        char surrogateStart = '\uD800';
        char surrogateEnd = '\uDBFF';
        char supplementary = '\uDFFF';

        boolean result = entities.canEncode(coreCharset, surrogateStart, encoder);
        boolean expected = false;
        Assert.assertEquals("Unexpected result for canEncode method for surrogate start: " + surrogateStart, expected, result);

        result = entities.canEncode(coreCharset, surrogateEnd, encoder);
        expected = false;
        Assert.assertEquals("Unexpected result for canEncode method for surrogate end: " + surrogateEnd, expected, result);

        result = entities.canEncode(coreCharset, supplementary, encoder);
        expected = false;
        Assert.assertEquals("Unexpected result for canEncode method for supplementary character: " + supplementary, expected, result);
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();

        Map<String, Character> result = entities.loadEntities("entities-base.properties");

        Character entity = Character.valueOf('\u00A0');
        String name = "nbsp";
        Map.Entry<String, Character> entry = Map.entry(name, entity);
        Map<String, Character> expectedMap = new HashMap<>();
        expectedMap.put(name, entity);

        Assert.assertEquals("Unexpected result for loadEntities method", expectedMap, result);
    }
}
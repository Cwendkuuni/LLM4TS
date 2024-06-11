package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> fullEntities = entities.getFull();

        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            boolean result = entities.isNamedEntity(name);
            boolean expected = true;
            Assert.assertEquals("Named entity " + name + " should be recognized", expected, result);
        }

        String nonEntity = "not_a_named_entity";
        boolean result = entities.isNamedEntity(nonEntity);
        boolean expected = false;
        Assert.assertEquals("Non-entity " + nonEntity + " should not be recognized", expected, result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> baseEntities = entities.getBase().getMap();

        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            boolean result = entities.isBaseNamedEntity(name);
            boolean expected = true;
            Assert.assertEquals("Base entity " + name + " should be recognized", expected, result);

            result = entities.isBaseNamedEntity(String.valueOf(character));
            expected = true;
            Assert.assertEquals("Base entity " + character + " should be recognized", expected, result);
        }

        Character nonEntity = '~';
        boolean result = entities.isBaseNamedEntity(String.valueOf(nonEntity));
        boolean expected = false;
        Assert.assertEquals("Non-entity " + nonEntity + " should not be recognized", expected, result);
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<String, Character> fullEntities = entities.getFull();

        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Character result = entities.getCharacterByName(name);
            Assert.assertEquals("Named entity " + name + " should return the expected character", character, result);
        }

        String nonEntityName = "not_a_named_entity";
        Character result = entities.getCharacterByName(nonEntityName);
        Assert.assertNull("Non-entity " + nonEntityName + " should return null", result);
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        StringBuilder expectedOutput = new StringBuilder();
        String input = "&amp;<lt>Hello &lt;world&gt;< quot >&apos; <'&gt;";

        entities.escape(expectedOutput, input, null, false, false, false);

        String result = expectedOutput.toString();
        String expected = "&amp;amp;<amp;&lt;>Hello &amp;lt;<amp;&quot; >&amp;&apos; <amp;&apos;&gt;<amp;&gt;";
        Assert.assertEquals("Escaped string should match the expected output", expected, result);
    }

    @Test
    public void testEscapeInAttribute() {
        Entities entities = new Entities();

        StringBuilder expectedOutput = new StringBuilder();
        String input = "<tag attr='&amp;<lt>Hello &lt;world&gt;< quot >&apos; <'&gt;'>";

        boolean inAttribute = true;
        entities.escape(expectedOutput, input, null, inAttribute, false, false);

        String result = expectedOutput.toString();
        String expected = "<tag attr='&amp;amp;<amp;&lt;>Hello &amp;lt;<amp;&quot; >&amp;&apos; <amp;&apos;&gt;<amp;&gt;'>";
        Assert.assertEquals("Escaped string in attribute should match the expected output", expected, result);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;amp;<amp;&lt;>Hello &amp;lt;<amp;&quot; >&amp;&apos; <amp;&apos;&gt;<amp;&gt;";
        String expectedOutput = "&lt;Hello &lt;world&gt;&quot; '&gt;";

        String result = entities.unescape(input, false);
        Assert.assertEquals("Unescaped string should match the expected output", expectedOutput, result);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CharsetEncoder encoder = null; // not using the encoder for this test case

        // test ASCII characters
        for (char c = 0; c < 128; c++) {
            boolean result = entities.canEncode(entities.CoreCharset.ascii, c, encoder);
            boolean expected = true;
            Assert.assertEquals("ASCII character " + c + " should be encodable", expected, result);
        }

        // test non-ASCII characters using a fallback encoder
        String nonAsciiCharacter = "ñ";
        CharsetEncoder fallbackEncoder = Charset.forName("UTF-8").newEncoder();
        boolean result = entities.canEncode(entities.CoreCharset.utf, nonAsciiCharacter.charAt(0), fallbackEncoder);
        boolean expected = true;
        Assert.assertEquals("Non-ASCII character " + nonAsciiCharacter + " should be encodable", expected, result);

        // test characters that cannot be encoded
        char unencodableCharacter = '\uD800';
        boolean result = entities.canEncode(entities.CoreCharset.ascii, unencodableCharacter, encoder);
        boolean expected = false;
        Assert.assertEquals("Unencodable character " + unencodableCharacter + " should not be encodable", expected, result);
    }
}
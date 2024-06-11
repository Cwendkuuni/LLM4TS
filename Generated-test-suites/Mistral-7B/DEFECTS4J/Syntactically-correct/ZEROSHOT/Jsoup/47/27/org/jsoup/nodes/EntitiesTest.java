package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for isNamedEntity method with valid named entities
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Assert.assertTrue("isNamedEntity should return true for valid named entity: " + entry.getKey(), Entities.isNamedEntity(entry.getValue()));
        }

        // Test for isNamedEntity method with invalid named entities
        Assert.assertFalse("isNamedEntity should return false for invalid named entity", Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for isBaseNamedEntity method with valid named entities
        for (Map.Entry<Character, String> entry : Entities.base.entrySet()) {
            Assert.assertTrue("isBaseNamedEntity should return true for valid named entity: " + entry.getKey(), Entities.isBaseNamedEntity(entry.getValue()));
        }

        // Test for isBaseNamedEntity method with invalid named entities
        Assert.assertFalse("isBaseNamedEntity should return false for invalid named entity", Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for getCharacterByName method with valid named entities
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Assert.assertEquals("getCharacterByName should return correct character for valid named entity: " + entry.getValue(), entry.getKey(), Entities.getCharacterByName(entry.getValue()));
        }

        // Test for getCharacterByName method with invalid named entities
        Assert.assertNull("getCharacterByName should return null for invalid named entity", Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscapeMethod() {
        // Test for escape method with various input strings, Document.OutputSettings, inAttribute, normaliseWhite, and stripLeadingWhite
        String inputString = "&<>'\"test <> &amp; &quot; test &nbsp; Test &lt;=&gt; Test &#123; Test &x27; Test &#x20AC; Test &#x1F345; Test &#xA0; Test &#x41F; Test \u00A9 Test \uD83C\uDFFB";
        Document.OutputSettings outputSettings = Document.OutputSettings.xml(); // or any other OutputSettings

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("&amp;amp;#x26;#x3C;#x26;#x27;#x26;quot;#x26;quot;#x2002;#x26;#x3C;#x26;#x3E;#x26;#xA0;#x2325;#x2019;#x1F345;#x2320;#x200A;#x00A9;#x1F602;#x270F;");

        String output = Entities.escape(inputString, outputSettings);
        Assert.assertEquals("escape method should return expected output for given input string and OutputSettings", expectedOutput.toString(), output);
    }

    @Test
    public void testUnescapeMethod() {
        // Test for unescape method with various input strings and strict flag
        String inputString = "&amp;amp;#x26;#x3C;#x26;#x27;#x26;quot;#x26;quot;#x2002;#x26;#x3C;#x26;#x3E;#x26;#xA0;#x2325;#x2019;#x1F345;#x2320;#x200A;#x00A9;#x1F602;#x270F;";

        String output = Entities.unescape(inputString);
        Assert.assertEquals("unescape method should return original input string for given input string", "&<>'\" test <> &amp; &quot; test &nbsp; Test <=>&gt; Test &#123; Test &#x27; Test &#x20AC; Test &#x1F345; Test &#xA0; Test &#x41F; Test © Test 💔;", output);

        String strictOutput = Entities.unescape(inputString, true);
        Assert.assertEquals("unescape method should return corrected input string for given input string with strict flag", "&amp;amp;#x26;#x3C;#x26;#x27;#x26;quot;#x26;quot; #x2002;#x26;#x3C;#x26;#x3E;#x26;#xA0;#x2325;#x2019;#x1F345;#x2320; #x200A;#x00A9;#x1F602;#x270F;", strictOutput);
    }
}
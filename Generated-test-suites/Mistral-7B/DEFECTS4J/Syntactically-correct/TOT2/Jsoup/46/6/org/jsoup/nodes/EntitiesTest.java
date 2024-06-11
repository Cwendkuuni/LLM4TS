import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Named entity " + name + " should be known", Entities.isNamedEntity(name));
            Assert.assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.baseByVal;
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Base named entity " + name + " should be known", Entities.isBaseNamedEntity(name));
            Assert.assertEquals(character, Entities.getCharacterByName(name));
        }

        // test unknown named entities
        Assert.assertFalse("Unknown named entity should not be known", Entities.isNamedEntity("nonexistent"));
        Assert.assertFalse("Unknown named entity should not be known", Entities.isBaseNamedEntity("nonexistent"));
    }

    @Test
    public void testEscapeMethodNormalFunctionality() {
        String input = "This is a test & äöü string with < > and \" characters.";
        String expectedOutput = "This is a test &amp; &#228; &#229; &#231; &lt; &gt; &quot;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodEdgeCases() {
        // test empty string
        String input = "";
        String output = Entities.escape(input, null);
        Assert.assertEquals("", output);

        // test whitespace
        String input = "   ";
        String expectedOutput = "&nbsp; &nbsp; &nbsp;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);

        // test surrogate pairs
        String input = "\uD800\uDC00";
        String expectedOutput = "&#x10000;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);

        // test invalid characters
        String input = "☹";
        String expectedOutput = "&#x2639;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);

        // test invalid unicode characters
        String input = "🤪";
        String expectedOutput = "&#x1F926;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodExceptionHandling() {
        String input = null;
        Entities.escape(input, null);
    }

    @Test
    public void testUnescapeMethodNormalFunctionality() {
        String input = "This is a test &amp; &#228; &#229; &#231; &lt; &gt; &quot;";
        String expectedOutput = "This is a test & äöü string with < > and \" characters.";
        String output = Entities.unescape(input, false);
        Assert.assertEquals(expectedOutput, output);

        // test strict unescape
        String strictInput = "This is a test &amp; &#x228; &#x229; &#x231; &lt; &gt; &quot;";
        String strictOutput = "This is a test &amp; &amp;#x228; &amp;#x229; &amp;#x231; &lt; &gt; &quot;";
        String output = Entities.unescape(strictInput, true);
        Assert.assertEquals(strictOutput, output);
    }

    @Test
    public void testCanEncodeMethodNormalFunctionality() {
        Entities.CoreCharset coreCharset = Entities.CoreCharset.utf;
        char character = 'A';
        boolean expectedOutput = true;
        boolean output = Entities.canEncode(coreCharset, character, null);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncodeMethodEdgeCases() {
        Entities.CoreCharset coreCharset = Entities.CoreCharset.ascii;
        char character = '€';
        boolean expectedOutput = false;
        boolean output = Entities.canEncode(coreCharset, character, null);
        Assert.assertEquals(expectedOutput, output);

        // test invalid characters
        Entities.CoreCharset coreCharset = Entities.CoreCharset.utf;
        char character = '\uD800';
        boolean expectedOutput = false;
        boolean output = Entities.canEncode(coreCharset, character, null);
        Assert.assertEquals(expectedOutput, output);
    }
}
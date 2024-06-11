import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("unknownEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Character expected = '<';
        Character actual = Entities.getCharacterByName("lt");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEscapeNormalMode() {
        String input = "&amp;<lt>Hello & World&gt;";
        String expectedOutput = "&amp;amp;&lt; & &gt;Hello & World&gt;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeAttributeMode() {
        String input = "<script>alert('XSS')</script>";
        String expectedOutput = "&lt;script&gt;alert('XSS')&lt;/script&gt;";
        String output = Entities.escape(input, null, true, true, true, true);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testIsNamedEntityCaseSensitive() {
        Assert.assertFalse(Entities.isNamedEntity("LT"));
        Assert.assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIsNamedEntity() {
        Entities.isNamedEntity(null);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;&lt; & &gt;Hello & World&gt;";
        String expectedOutput = "&amp;<lt> Hello & World&gt;";
        String output = Entities.unescape(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Entities.unescape(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        char c = '☺'; // happy face
        boolean result = Entities.canEncode(charset, c, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testCanEncodeInvalidCharset() {
        CoreCharset charset = new CoreCharset() {
            @Override
            public String name() {
                throw new UnsupportedOperationException();
            }
        };
        char c = '☺'; // happy face
        boolean result = Entities.canEncode(charset, c, null);
        Assert.assertFalse(result);
    }
}
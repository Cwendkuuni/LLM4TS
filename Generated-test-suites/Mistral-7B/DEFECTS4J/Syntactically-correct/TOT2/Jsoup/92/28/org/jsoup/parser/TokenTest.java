import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attributes;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");

        doctype.reset();

        String name = doctype.getName();
        assertNull(name);
    }

    @Test
    public void testDoctypeNameAfterReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");

        doctype.reset();
        doctype.getName().equals("html");
    }

    @Test(expected = NullPointerException.class)
    public void testDoctypeResetNull() {
        Doctype doctype = null;
        doctype.reset();
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag().name("html");
        assertEquals("html", tag.name());
    }

    @Test
    public void testTagNormalName() {
        Tag tag = new StartTag().name("html");
        assertEquals("html", tag.normalName());
    }

    @Test
    public void testTagReset() {
        StartTag tag = new StartTag();
        tag.name("html");

        tag.reset();

        String name = tag.name();
        assertNull(name);
    }

    @Test
    public void testTagNameAfterReset() {
        StartTag tag = new StartTag();
        tag.name("html");

        tag.reset();
        tag.name("html");
    }

    @Test(expected = NullPointerException.class)
    public void testTagResetNull() {
        StartTag tag = null;
        tag.reset();
    }

    @Test
    public void testAttributesNull() {
        Attributes attributes = null;
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAttributesSize() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAttributesPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testAttributesGetNull() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttributesPutNull() {
        Attributes attributes = null;
        attributes.put("key", "value");
    }

    @Test
    public void testAttributesPutEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        assertEquals("", attributes.get("key"));
        assertFalse(attributes.containsValue(""));
    }

    @Test
    public void testAttributesPutEmptyValueWithTrue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        attributes.put("key", true);
        assertTrue(attributes.containsValue(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributesPutTwice() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "value2");
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterResetNull() {
        Character character = null;
        character.reset();
    }

    @Test
    public void testCharacterData() {
        Character character = new Character();
        character.data("Hello World!");

        assertEquals("Hello World!", character.getData());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("Comment");

        comment.reset();

        String data = comment.getData();
        assertNull(data);
    }

    @Test
    public void testCommentData() {
        Comment comment = new Comment();
        comment.getData().equals("Comment");

        comment.reset();
        comment.getData().equals("Comment");
    }

    @Test(expected = NullPointerException.class)
    public void testCommentResetNull() {
        Comment comment = null;
        comment.reset();
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();

        eof.reset();

        assertEquals(TokenType.EOF, eof.type);
    }

}
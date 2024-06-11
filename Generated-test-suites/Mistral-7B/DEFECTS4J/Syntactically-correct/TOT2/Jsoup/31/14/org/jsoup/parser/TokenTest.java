import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeMethods() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.getClass().getSimpleName());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testEmptyTagName() {
        Token tag = new Token.StartTag();
        tag.name(" ");
        assertEquals("", tag.name());
    }

    @Test
    public void testNullTagName() {
        Token tag = new Token.StartTag();
        tag.name(null);
        assertNull(tag.name());
    }

    @Test
    public void testLongTagName() {
        Token tag = new Token.StartTag();
        String longTagName = new String(new char[1024]);
        tag.name(longTagName);
        assertEquals(longTagName, tag.name());
    }

    // Add more test cases for other methods and edge cases

    @Test
    public void testAttributes() {
        Token tag = new Token.StartTag("tag");
        tag.newAttribute();
        tag.appendAttributeName("attr");
        tag.appendAttributeValue("value");
        assertEquals("value", tag.getAttributes().get("attr"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAttributeNameExists() {
        Token tag = new Token.StartTag("tag");
        tag.newAttribute();
        tag.newAttribute();
        tag.appendAttributeName("attr");
        // This should throw an IllegalStateException
    }

    // Add more test cases for other methods and edge cases

    @Test
    public void testAppendingCharacters() {
        Token tag = new Token.StartTag("tag");
        tag.appendTagName("a");
        tag.appendTagName("b");
        tag.appendAttributeName("attr");
        tag.appendAttributeValue("value");
        assertEquals("tagabattrvalue", tag.toString());
    }

    @Test
    public void testSelfClosingTag() {
        Token tag = new Token.StartTag("img", new Attributes().put("src", "image.png"));
        tag.selfClosing = true;
        assertEquals("<img src=\"image.png\" />", tag.toString());
    }

    // Add more test cases for other methods and edge cases

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAttributeValue() {
        Token tag = new Token.StartTag("input");
        tag.newAttribute();
        tag.appendAttributeName("type");
        tag.appendAttributeValue("\u0000"); // Null character
        // This should throw an IllegalArgumentException
    }

    // Add more test cases for other methods and edge cases

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
        assertEquals("EOF", eof.getClass().getSimpleName());
    }

    @Test
    public void testIsDoctype() {
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token startTag = new Token.StartTag("tag");
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token endTag = new Token.EndTag("tag");
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token character = new Token.Character("char");
        assertTrue(character.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    // Add more test cases for other methods and edge cases

}
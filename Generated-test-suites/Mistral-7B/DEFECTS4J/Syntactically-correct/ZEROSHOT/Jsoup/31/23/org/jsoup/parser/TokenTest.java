import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeCreation() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.tokenType());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getSystemIdentifier().append("about:blank");
        doctype.setForceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.asDoctype().getName());
        assertEquals("about:blank", doctype.asDoctype().getSystemIdentifier());
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctypeGettersAfterMutation() {
        Token doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getSystemIdentifier().append("about:blank");
        doctype.setForceQuirks(true);
        doctype.getName().append("body");

        // This should throw an exception as the Doctype object is immutable after construction
    }

    @Test
    public void testStartTagCreation() {
        Token startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.tokenType());
    }

    @Test
    public void testStartTagGetters() {
        Token startTag = new Token.StartTag();
        startTag.name("html");

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("html", startTag.asStartTag().name());
        assertFalse(startTag.asStartTag().isSelfClosing());
        assertNull(startTag.asStartTag().getAttributes());
    }

    @Test
    public void testStartTagSetters() {
        Token startTag = new Token.StartTag();
        startTag.name("html").attributes(new Token.Attributes().put("id", "root"));

        assertEquals("html", startTag.asStartTag().name());
        assertTrue(startTag.asStartTag().isSelfClosing());
        assertNotNull(startTag.asStartTag().getAttributes());
        assertEquals("root", startTag.asStartTag().getAttributes().getValue("id"));
    }

    @Test
    public void testEndTagCreation() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.tokenType());
    }

    @Test
    public void testEndTagGetters() {
        Token endTag = new Token.EndTag();
        endTag.name("html");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("html", endTag.asEndTag().name());
        assertFalse(endTag.asEndTag().isSelfClosing());
        assertNull(endTag.asEndTag().getAttributes());
    }

    @Test
    public void testCommentCreation() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.tokenType());
    }

    @Test
    public void testCommentGetter() {
        Token comment = new Token.Comment();
        comment.getData().append("This is a comment");

        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment", comment.asComment().getData());
    }

    @Test
    public void testCharacterCreation() {
        Token character = new Token.Character("A");
        assertEquals(TokenType.Character, character.tokenType());
    }

    @Test
    public void testCharacterGetter() {
        Token character = new Token.Character("A");

        assertEquals("A", character.asCharacter().getData());
    }

    @Test
    public void testEOFCreation() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.tokenType());
    }

    @Test
    public void testTokenTypeIdentification() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character("A");
        Token eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(eof.isEOF());

        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());

        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());

        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isEOF());

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertTrue(eof.isEOF());
    }
}
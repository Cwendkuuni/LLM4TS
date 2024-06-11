import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        Token doctypeTokenWithName = new Token.Doctype();
        doctypeTokenWithName.asDoctype().getName().append("html");
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
        assertFalse(doctypeTokenWithName.asDoctype().isForceQuirks());

        Token doctypeTokenWithNameAndForceQuirks = new Token.Doctype();
        doctypeTokenWithNameAndForceQuirks.asDoctype().getName().append("html");
        doctypeTokenWithNameAndForceQuirks.asDoctype().setForceQuirks(true);
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
        assertTrue(doctypeTokenWithNameAndForceQuirks.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTagToken.tokenType());
        assertNull(startTagToken.asStartTag().tagName);
        assertFalse(startTagToken.asStartTag().isSelfClosing());
        assertNull(startTagToken.asStartTag().getAttributes());

        Token startTagTokenWithName = new Token.StartTag();
        startTagTokenWithName.asStartTag().tagName = "p";
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("p", startTagTokenWithName.asStartTag().tagName);
        assertFalse(startTagTokenWithName.asStartTag().isSelfClosing());
        assertNull(startTagTokenWithName.asStartTag().getAttributes());

        Token startTagTokenWithNameAndAttributes = new Token.StartTag();
        startTagTokenWithNameAndAttributes.asStartTag().tagName = "p";
        Attribute attribute1 = new Attribute("id", "test1");
        Attribute attribute2 = new Attribute("class", "test2");
        startTagTokenWithNameAndAttributes.asStartTag().attributes.put(attribute1);
        startTagTokenWithNameAndAttributes.asStartTag().attributes.put(attribute2);
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("p", startTagTokenWithNameAndAttributes.asStartTag().tagName);
        assertFalse(startTagTokenWithNameAndAttributes.asStartTag().isSelfClosing());
        assertNotNull(startTagTokenWithNameAndAttributes.asStartTag().getAttributes());
        assertEquals(2, startTagTokenWithNameAndAttributes.asStartTag().getAttributes().size());

        Token startTagTokenWithSelfClosing = new Token.StartTag();
        startTagTokenWithSelfClosing.asStartTag().tagName = "img";
        startTagTokenWithSelfClosing.asStartTag().setSelfClosing(true);
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("img", startTagTokenWithSelfClosing.asStartTag().tagName);
        assertTrue(startTagTokenWithSelfClosing.asStartTag().isSelfClosing());
        assertNull(startTagTokenWithSelfClosing.asStartTag().getAttributes());

        Token startTagTokenWithNameAndSelfClosing = new Token.StartTag();
        startTagTokenWithNameAndSelfClosing.asStartTag().tagName = "input";
        startTagTokenWithNameAndSelfClosing.asStartTag().setSelfClosing(true);
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("input", startTagTokenWithNameAndSelfClosing.asStartTag().tagName);
        assertTrue(startTagTokenWithNameAndSelfClosing.asStartTag().isSelfClosing());
        assertNull(startTagTokenWithNameAndSelfClosing.asStartTag().getAttributes());
    }

    @org.junit.Test
    public void testEndTagToken() { 
        Token endTagToken = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTagToken.tokenType());
        assertNull(endTagToken.asEndTag().tagName);
        assertEquals("", endTagToken.asEndTag().toString());

        Token endTagTokenWithName = new Token.EndTag();
        endTagTokenWithName.asEndTag().tagName = "p";
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("p", endTagTokenWithName.asEndTag().tagName);
        assertEquals("</p>", endTagTokenWithName.toString());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals(TokenType.Comment, commentToken.tokenType());
        assertNull(commentToken.asComment().getData());
        assertEquals("", commentToken.asComment().toString());

        Token commentTokenWithData = new Token.Comment();
        commentTokenWithData.asComment().getData().append("This is a comment.");
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("<!--This is a comment-->", commentTokenWithData.toString());
        assertEquals("This is a comment.", commentTokenWithData.asComment().getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character('A');
        assertEquals(TokenType.Character, characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());
        assertEquals("A", characterToken.toString());

        Token characterTokenWithData = new Token.Character('&');
        characterTokenWithData.asCharacter().appendAttributeValue("amp;");
        assertEquals("Character", characterToken.tokenType());
        assertEquals("&amp;", characterTokenWithData.asCharacter().getData());
        assertEquals("&amp;", characterTokenWithData.toString());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals(TokenType.EOF, eofToken.tokenType());
        assertTrue(eofToken.isEOF());
        assertEquals("", eofToken.toString());
    }
}
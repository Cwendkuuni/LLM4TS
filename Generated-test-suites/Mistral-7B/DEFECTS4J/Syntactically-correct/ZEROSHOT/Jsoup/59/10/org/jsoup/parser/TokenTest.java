import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());
        doctypeToken.reset();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());

        Token.Doctype doctypeResetToken = (Token.Doctype) doctypeToken;
        doctypeResetToken.name("html");
        assertEquals("html", doctypeResetToken.getName());
        doctypeResetToken.pubSysKey("DTD XHTML 1.0 Strict");
        assertEquals("DTD XHTML 1.0 Strict", doctypeResetToken.getPubSysKey());
        doctypeResetToken.publicIdentifier("Public ietf-charset-0-1;charset=UTF-8");
        assertEquals("Public ietf-charset-0-1;charset=UTF-8", doctypeResetToken.getPublicIdentifier());
        doctypeResetToken.systemIdentifier("/myhtmlfile.html");
        assertEquals("/myhtmlfile.html", doctypeResetToken.getSystemIdentifier());
        doctypeResetToken.forceQuirks(true);
        assertTrue(doctypeResetToken.isForceQuirks());
        doctypeResetToken.reset();
        assertFalse(doctypeResetToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTagToken.tokenType());
        startTagToken.name("div");
        assertEquals("div", startTagToken.name());
        startTagToken.normalName("div");
        assertEquals("div", startTagToken.normalName());
        startTagToken.selfClosing(true);
        assertTrue(startTagToken.isSelfClosing());
        startTagToken.attributes = new Attributes();
        startTagToken.attributes.put("id", "myDiv");
        assertEquals("myDiv", startTagToken.attributes.get("id").getValue());
        startTagToken.newAttribute();
        startTagToken.attributes.put("class", "myClass");
        assertEquals("myClass", startTagToken.attributes.get("class").getValue());
        startTagToken.finaliseTag();
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        startTagToken.reset();
        assertEquals(TokenType.StartTag, startTagToken.tokenType());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTagToken.tokenType());
        endTagToken.name("div");
        assertEquals("div", endTagToken.name());
        endTagToken.reset();
        assertEquals(TokenType.EndTag, endTagToken.tokenType());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals(TokenType.Comment, commentToken.tokenType());
        commentToken.data("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
        commentToken.bogus(true);
        assertTrue(commentToken.isBogus());
        commentToken.reset();
        assertFalse(commentToken.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals(TokenType.Character, characterToken.tokenType());
        characterToken.data("!");
        assertEquals("!", characterToken.getData());
        characterToken.reset();
        assertEquals(TokenType.Character, characterToken.tokenType());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals(TokenType.EOF, eofToken.tokenType());
        eofToken.reset();
        assertEquals(TokenType.EOF, eofToken.tokenType());
    }
}
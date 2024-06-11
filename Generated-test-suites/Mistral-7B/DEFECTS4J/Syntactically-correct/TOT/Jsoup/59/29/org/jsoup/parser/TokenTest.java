import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        // Test reset method
        doctypeToken.reset();
        assertEquals(TokenType.Doctype, doctypeToken.type);
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        // Test setting values
        doctypeToken.setName("html");
        doctypeToken.setPubSysKey("PUBLIC");
        doctypeToken.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctypeToken.setSystemIdentifier("/system/xhtml1_strict.dtd");
        doctypeToken.setForceQuirks(true);
        
        // Test getting values
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.getName());
        assertEquals("PUBLIC", doctypeToken.getPubSysKey());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeToken.getPublicIdentifier());
        assertEquals("/system/xhtml1_strict.dtd", doctypeToken.getSystemIdentifier());
        assertTrue(doctypeToken.isForceQuirks());
        
        // Test reset method after setting values
        doctypeToken.reset();
        assertEquals(TokenType.Doctype, doctypeToken.type);
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        // Test reset method
        startTagToken.reset();
        assertEquals(TokenType.StartTag, startTagToken.type);
        assertNull(startTagToken.getName());
        assertNull(startTagToken.attributes);
        assertFalse(startTagToken.isSelfClosing());

        // Test setting values
        startTagToken.name("div");
        startTagToken.attributes(new Attributes().put("id", "container").put("class", "box"));
        startTagToken.selfClosing(true);

        // Test getting values
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("div", startTagToken.getName());
        assertEquals("div", startTagToken.normalName());
        assertSame(new Attributes().put("id", "container").put("class", "box"), startTagToken.getAttributes());
        assertTrue(startTagToken.isSelfClosing());

        // Test reset method after setting values
        startTagToken.reset();
        assertEquals(TokenType.StartTag, startTagToken.type);
        assertNull(startTagToken.getName());
        assertNull(startTagToken.attributes);
        assertFalse(startTagToken.isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        // Test reset method
        endTagToken.reset();
        assertEquals(TokenType.EndTag, endTagToken.type);
        assertNull(endTagToken.getName());

        // Test setting value
        endTagToken.name("div");

        // Test getting value
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("div", endTagToken.getName());
        assertEquals("div", endTagToken.normalName());

        // Test reset method after setting value
        endTagToken.reset();
        assertEquals(TokenType.EndTag, endTagToken.type);
        assertNull(endTagToken.getName());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        // Test reset method
        commentToken.reset();
        assertEquals(TokenType.Comment, commentToken.type);
        assertNull(commentToken.getData());
        assertFalse(commentToken.isBogus());

        // Test setting value
        commentToken.setData("This is a comment");

        // Test getting value
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment", commentToken.getData());

        // Test reset method after setting value
        commentToken.reset();
        assertEquals(TokenType.Comment, commentToken.type);
        assertNull(commentToken.getData());
        assertFalse(commentToken.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        // Test reset method
        characterToken.reset();
        assertEquals(TokenType.Character, characterToken.type);
        assertNull(characterToken.getData());

        // Test setting value
        characterToken.setData("&amp;");

        // Test getting value
        assertEquals("Character", characterToken.tokenType());
        assertEquals("&amp;", characterToken.getData());

        // Test reset method after setting value
        characterToken.reset();
        assertEquals(TokenType.Character, characterToken.type);
        assertNull(characterToken.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        // Test reset method
        eofToken.reset();
        assertEquals(TokenType.EOF, eofToken.type);

        // Test getting value
        assertEquals("EOF", eofToken.tokenType());
    }
}
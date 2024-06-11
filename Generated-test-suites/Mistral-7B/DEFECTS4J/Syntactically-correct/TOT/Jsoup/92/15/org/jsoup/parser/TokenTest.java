import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();

        assertEquals(TokenType.Doctype, resetDoctypeToken.tokenType());
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.getName("html");
        resetDoctypeToken = doctypeToken.reset();

        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertEquals("html", resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPubSysKey());
        assertNull(resetDoctypeToken.getPublicIdentifier());
        assertNull(resetDoctypeToken.getSystemIdentifier());
        assertFalse(resetDoctypeToken.isForceQuirks());

        doctypeToken.setPubSysKey("_pubsys_");
        resetDoctypeToken = doctypeToken.reset();

        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertEquals("html", resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPubSysKey());
        assertNull(resetDoctypeToken.getPublicIdentifier());
        assertNull(resetDoctypeToken.getSystemIdentifier());
        assertFalse(resetDoctypeToken.isForceQuirks());

        doctypeToken.setPublicIdentifier("_public_");
        resetDoctypeToken = doctypeToken.reset();

        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertEquals("html", resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPubSysKey());
        assertEquals("_public_", resetDoctypeToken.getPublicIdentifier());
        assertNull(resetDoctypeToken.getSystemIdentifier());
        assertFalse(resetDoctypeToken.isForceQuirks());

        doctypeToken.setSystemIdentifier("_system_");
        resetDoctypeToken = doctypeToken.reset();

        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertEquals("html", resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPubSysKey());
        assertNull(resetDoctypeToken.getPublicIdentifier());
        assertEquals("_system_", resetDoctypeToken.getSystemIdentifier());
        assertFalse(resetDoctypeToken.isForceQuirks());

        doctypeToken.setForceQuirks(true);
        resetDoctypeToken = doctypeToken.reset();

        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertEquals("html", resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPubSysKey());
        assertNull(resetDoctypeToken.getPublicIdentifier());
        assertNull(resetDoctypeToken.getSystemIdentifier());
        assertTrue(resetDoctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.Tag();
        Token resetTagToken = tagToken.reset();

        assertEquals(TokenType.Tag, resetTagToken.tokenType());
        assertNull(resetTagToken.tagName);
        assertNull(resetTagToken.normalName);
        assertNull(resetTagToken.pendingAttributeName);
        assertNull(resetTagToken.pendingAttributeValue);
        assertNull(resetTagToken.pendingAttributeValueS);
        assertFalse(resetTagToken.hasEmptyAttributeValue);
        assertFalse(resetTagToken.hasPendingAttributeValue);
        assertFalse(resetTagToken.selfClosing);
        assertNull(resetTagToken.attributes);

        tagToken.tagName("html");
        resetTagToken = tagToken.reset();

        assertEquals("Tag", resetTagToken.tokenType());
        assertEquals("html", resetTagToken.tagName);
        assertNull(resetTagToken.normalName);
        assertNull(resetTagToken.pendingAttributeName);
        assertNull(resetTagToken.pendingAttributeValue);
        assertNull(resetTagToken.pendingAttributeValueS);
        assertFalse(resetTagToken.hasEmptyAttributeValue);
        assertFalse(resetTagToken.hasPendingAttributeValue);
        assertFalse(resetTagToken.selfClosing);
        assertNull(resetTagToken.attributes);

        tagToken.name("html");
        resetTagToken = tagToken.reset();

        assertEquals("Tag", resetTagToken.tokenType());
        assertEquals("html", resetTagToken.tagName);
        assertEquals("html", resetTagToken.normalName);
        assertNull(resetTagToken.pendingAttributeName);
        assertNull(resetTagToken.pendingAttributeValue);
        assertNull(resetTagToken.pendingAttributeValueS);
        assertFalse(resetTagToken.hasEmptyAttributeValue);
        assertFalse(resetTagToken.hasPendingAttributeValue);
        assertFalse(resetTagToken.selfClosing);
        assertNull(resetTagToken.attributes);

        tagToken.name("html").selfClosing(true);
        resetTagToken = tagToken.reset();

        assertEquals("Tag", resetTagToken.tokenType());
        assertEquals("html", resetTagToken.tagName);
        assertEquals("html", resetTagToken.normalName);
        assertNull(resetTagToken.pendingAttributeName);
        assertNull(resetTagToken.pendingAttributeValue);
        assertNull(resetTagToken.pendingAttributeValueS);
        assertFalse(resetTagToken.hasEmptyAttributeValue);
        assertFalse(resetTagToken.hasPendingAttributeValue);
        assertTrue(resetTagToken.selfClosing);
        assertNull(resetTagToken.attributes);

        tagToken.newAttribute();
        resetTagToken = tagToken.reset();

        assertEquals("Tag", resetTagToken.tokenType());
        assertEquals("html", resetTagToken.tagName);
        assertEquals("html", resetTagToken.normalName);
        assertNull(resetTagToken.pendingAttributeName);
        assertNull(resetTagToken.pendingAttributeValue);
        assertNull(resetTagToken.pendingAttributeValueS);
        assertFalse(resetTagToken.hasEmptyAttributeValue);
        assertFalse(resetTagToken.hasPendingAttributeValue);
        assertFalse(resetTagToken.selfClosing);
        assertNull(resetTagToken.attributes);

        tagToken.newAttribute().name("class").value("test");
        resetTagToken = tagToken.reset();

        assertEquals("Tag", resetTagToken.tokenType());
        assertEquals("html", resetTagToken.tagName);
        assertEquals("html", resetTagToken.normalName);
        assertNull(resetTagToken.pendingAttributeName);
        assertNull(resetTagToken.pendingAttributeValue);
        assertNull(resetTagToken.pendingAttributeValueS);
        assertFalse(resetTagToken.hasEmptyAttributeValue);
        assertTrue(resetTagToken.hasPendingAttributeValue);
        assertFalse(resetTagToken.selfClosing);
        assertNotNull(resetTagToken.attributes);
        assertEquals("class", resetTagToken.attributes.getKey(0));
        assertEquals("test", resetTagToken.attributes.getValue(0));

        tagToken.finaliseTag();
        resetTagToken = tagToken.reset();

        assertEquals("Tag", resetTagToken.tokenType());
        assertEquals("html", resetTagToken.tagName);
        assertEquals("html", resetTagToken.normalName);
        assertNull(resetTagToken.pendingAttributeName);
        assertNull(resetTagToken.pendingAttributeValue);
        assertNull(resetTagToken.pendingAttributeValueS);
        assertFalse(resetTagToken.hasEmptyAttributeValue);
        assertFalse(resetTagToken.hasPendingAttributeValue);
        assertFalse(resetTagToken.selfClosing);
        assertNotNull(resetTagToken.attributes);
        assertEquals("class", resetTagToken.attributes.getKey(0));
        assertEquals("test", resetTagToken.attributes.getValue(0));
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();

        assertEquals(TokenType.Comment, resetCommentToken.tokenType());
        assertNull(resetCommentToken.getData());
        assertFalse(resetCommentToken.isForceQuirks());

        commentToken.getData("This is a comment");
        resetCommentToken = commentToken.reset();

        assertEquals(TokenType.Comment, resetCommentToken.tokenType());
        assertEquals("This is a comment", resetCommentToken.getData());
        assertFalse(resetCommentToken.isForceQuirks());

        commentToken.bogus(true);
        resetCommentToken = commentToken.reset();

        assertEquals(TokenType.Comment, resetCommentToken.tokenType());
        assertNull(resetCommentToken.getData());
        assertTrue(resetCommentToken.isForceQuirks());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertNull(resetCharacterToken.getData());

        characterToken.data("A");
        resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertEquals("A", resetCharacterToken.getData());

        characterToken.data("&amp;");
        resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertEquals("&amp;", resetCharacterToken.getData());

        characterToken.data("&lt;");
        resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertEquals("&lt;", resetCharacterToken.getData());

        characterToken.data("&gt;");
        resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertEquals("&gt;", resetCharacterToken.getData());

        characterToken.data("&apos;");
        resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertEquals("&apos;", resetCharacterToken.getData());

        characterToken.data("&quot;");
        resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertEquals("&quot;", resetCharacterToken.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();

        assertEquals(TokenType.EOF, resetEofToken.tokenType());
    }
}
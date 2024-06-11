import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        assertEquals(TokenType.Doctype, doctypeToken.tokenType());

        doctypeToken.reset();

        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.getName("html");

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.getName());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.setForceQuirks(true);

        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        assertEquals(TokenType.StartTag, startTagToken.tokenType());

        startTagToken.reset();

        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.selfClosing);
        assertNull(startTagToken.attributes);

        startTagToken.tagName("p");

        assertEquals(TokenType.StartTag, startTagToken.tokenType());
        assertEquals("p", startTagToken.tagName);
        assertEquals("p", startTagToken.name());
        assertFalse(startTagToken.isSelfClosing());
        assertNull(startTagToken.attributes);

        startTagToken.newAttribute();

        assertFalse(startTagToken.hasPendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertNull(startTagToken.getAttributes());

        startTagToken.appendAttributeName("class");

        assertTrue(startTagToken.hasPendingAttributeValue);
        assertEquals("class", startTagToken.pendingAttributeName);

        startTagToken.appendAttributeValue("red");

        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertEquals("red", startTagToken.pendingAttributeValue.toString());
        assertNull(startTagToken.pendingAttributeValueS);
        assertNotNull(startTagToken.getAttributes());
        assertEquals("class", startTagToken.getAttributes().getKey(0).getName());
        assertEquals("red", startTagToken.getAttributes().getValue(0).toString());

        startTagToken.finaliseTag();

        assertFalse(startTagToken.hasPendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertNotNull(startTagToken.getAttributes());

        startTagToken.setEmptyAttributeValue();

        assertTrue(startTagToken.hasEmptyAttributeValue);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertNotNull(startTagToken.getAttributes());
        assertEquals(0, startTagToken.getAttributes().size());

        startTagToken.appendAttributeValue("style");
        startTagToken.newAttribute();

        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertNotNull(startTagToken.getAttributes());
        assertEquals("style", startTagToken.getAttributes().getKey(0).getName());
        assertEquals("style", startTagToken.getAttributes().getValue(0).toString());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        assertEquals(TokenType.EndTag, endTagToken.tokenType());

        endTagToken.reset();

        assertNull(endTagToken.tagName);
        assertNull(endTagToken.normalName);
        assertNull(endTagToken.attributes);

        endTagToken.tagName("p");

        assertEquals(TokenType.EndTag, endTagToken.tokenType());
        assertEquals("p", endTagToken.tagName);
        assertEquals("p", endTagToken.name());
        assertNull(endTagToken.attributes);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        assertEquals(TokenType.Comment, commentToken.tokenType());

        commentToken.reset();

        assertFalse(commentToken.bogus);

        commentToken.data("--This is a comment--");

        assertTrue(commentToken.bogus);
        assertEquals("--This is a comment--", commentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        assertEquals(TokenType.Character, characterToken.tokenType());

        characterToken.reset();

        assertNull(characterToken.data);

        characterToken.data("A");

        assertEquals('A', characterToken.getData().charAt(0));
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals(TokenType.EOF, eofToken.tokenType());

        eofToken.reset();

        assertTrue(eofToken.isEOF());
    }
}
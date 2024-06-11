import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.reset();

        doctypeToken.getName().equals("doctype");
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.setName("html");
        doctypeToken.setPubSysKey("public id");
        doctypeToken.setPublicIdentifier("system id");
        doctypeToken.setSystemIdentifier("local system id");
        doctypeToken.setForceQuirks(true);

        assertEquals("doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.getName());
        assertEquals("public id", doctypeToken.getPubSysKey());
        assertEquals("system id", doctypeToken.getPublicIdentifier());
        assertEquals("local system id", doctypeToken.getSystemIdentifier());
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.reset();

        assertNull(startTagToken.getAttributes());

        startTagToken.name("html");
        startTagToken.attributes(new Attributes().put("class", "page"));

        assertEquals("startTag", startTagToken.tokenType());
        assertEquals("html", startTagToken.name());
        assertEquals("html", startTagToken.normalName());
        assertFalse(startTagToken.isSelfClosing());
        assertNotNull(startTagToken.getAttributes());
        assertEquals("class", startTagToken.getAttributes().keySet().iterator().next());
        assertEquals("page", startTagToken.getAttributes().get("class"));

        startTagToken.newAttribute();
        startTagToken.appendAttributeName("id");
        startTagToken.appendAttributeValue("main");
        startTagToken.newAttribute();

        assertEquals(2, startTagToken.getAttributes().size());
        assertEquals("id", startTagToken.getAttributes().keySet().iterator().next());
        assertEquals("main", startTagToken.getAttributes().get("id"));
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.reset();

        assertNull(endTagToken.getAttributes());

        endTagToken.name("html");

        assertEquals("endTag", endTagToken.tokenType());
        assertEquals("html", endTagToken.name());
        assertEquals("html", endTagToken.normalName());
        assertTrue(endTagToken.isSelfClosing());
        assertNull(endTagToken.getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.reset();

        commentToken.getData().isEmpty();
        commentToken.bogus = true;

        assertEquals("comment", commentToken.tokenType());
        assertTrue(commentToken.isComment());
        assertNull(commentToken.getData());
        assertTrue(commentToken.bogus);

        commentToken.appendAttributeName("type");
        commentToken.appendAttributeValue("cdata");

        assertFalse(commentToken.isComment());
        assertEquals("comment", commentToken.tokenType());
        assertEquals("type", commentToken.getAttributes().keySet().iterator().next());
        assertEquals("cdata", commentToken.getAttributes().get("type"));

        commentToken.getData().append("Test comment");
        assertFalse(commentToken.isComment());
        assertFalse(commentToken.bogus);
        assertEquals("Test comment", commentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.reset();

        assertNull(characterToken.getData());

        characterToken.data("Test character");

        assertEquals("character", characterToken.tokenType());
        assertEquals("Test character", characterToken.getData());
    }

    @Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("Test CData");

        assertEquals("cData", cDataToken.tokenType());
        assertEquals("Test CData", cDataToken.getData());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidToken() {
        new Token();
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals("EOF", eofToken.tokenType().name());
    }
}
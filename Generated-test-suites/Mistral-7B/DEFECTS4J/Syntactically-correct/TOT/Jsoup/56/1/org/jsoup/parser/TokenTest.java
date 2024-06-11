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
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("doctype", doctypeToken.getClass().getSimpleName());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals(doctypeToken, resetDoctypeToken);

        doctypeToken.name("html");
        assertEquals("html", doctypeToken.getName());
        assertEquals("HTML", doctypeToken.asDoctype().normalName());

        doctypeToken.systemIdentifier("about:blank");
        assertEquals("about:blank", doctypeToken.asDoctype().getSystemIdentifier());

        doctypeToken.forceQuirks(true);
        assertTrue(doctypeToken.asDoctype().isForceQuirks());

        Token resetForceQuirksDoctypeToken = doctypeToken.reset();
        assertFalse(resetForceQuirksDoctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        assertEquals("StartTag", tagToken.tokenType().name());
        assertEquals("StartTag", tagToken.getClass().getSimpleName());

        Token resetTagToken = tagToken.reset();
        assertEquals(tagToken, resetTagToken);

        tagToken.name("html");
        assertEquals("html", tagToken.name());
        assertEquals("HTML", tagToken.asStartTag().normalName());

        tagToken.attributes(new Attributes().put("class", "myClass"));
        assertEquals("class=myClass", tagToken.asStartTag().toString());

        tagToken.selfClosing(true);
        assertTrue(tagToken.asStartTag().isSelfClosing());

        Token resetSelfClosingTagToken = tagToken.reset();
        assertFalse(resetSelfClosingTagToken.asStartTag().isSelfClosing());

        tagToken.newAttribute();
        tagToken.attributeName("id").attributeValue("myId");
        assertEquals("id=myId", tagToken.asStartTag().toString());

        tagToken.finaliseTag();
        assertEquals("html id=myId", tagToken.asStartTag().toString());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType().name());
        assertEquals("Comment", commentToken.getClass().getSimpleName());

        Token resetCommentToken = commentToken.reset();
        assertEquals(commentToken, resetCommentToken);

        commentToken.data("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
        assertEquals("<!--This is a comment-->", commentToken.toString());

        Token resetDataCommentToken = commentToken.reset();
        assertNull(resetDataCommentToken.getData());
        assertNull(resetDataCommentToken.toString());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType().name());
        assertEquals("Character", characterToken.getClass().getSimpleName());

        Token resetCharacterToken = characterToken.reset();
        assertEquals(characterToken, resetCharacterToken);

        characterToken.data("&amp;");
        assertEquals("&amp;", characterToken.getData());
        assertEquals("&amp;", characterToken.toString());

        characterToken.data("&lt;");
        assertEquals("&lt;", characterToken.getData());
        assertEquals("<", characterToken.toString());

        characterToken.data("&gt;");
        assertEquals("&gt;", characterToken.getData());
        assertEquals(">", characterToken.toString());

        characterToken.data("&apos;");
        assertEquals("'", characterToken.getData());
        assertEquals("'", characterToken.toString());

        characterToken.data("&#160;");
        assertEquals("\u00A0", characterToken.getData());
        assertEquals("\u00A0", characterToken.toString());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType().name());
        assertEquals("EOF", eofToken.getClass().getSimpleName());

        Token resetEOFToken = eofToken.reset();
        assertEquals(eofToken, resetEOFToken);
    }
}
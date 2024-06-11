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

        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.getName("html");
        String name = doctypeToken.getName();
        assertEquals("html", name);

        doctypeToken.setPubSysKey("pubSysKey");
        String pubSysKey = doctypeToken.getPubSysKey();
        assertEquals("pubSysKey", pubSysKey);

        doctypeToken.getPublicIdentifier().append("publicIdentifier");
        String publicIdentifier = doctypeToken.getPublicIdentifier().toString();
        assertEquals("publicIdentifier", publicIdentifier);

        doctypeToken.getSystemIdentifier().append("systemIdentifier");
        String systemIdentifier = doctypeToken.getSystemIdentifier().toString();
        assertEquals("systemIdentifier", systemIdentifier);

        doctypeToken.isForceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        Token resetTagToken = tagToken.reset();

        assertEquals("StartTag", resetTagToken.tokenType().name());
        assertNull(tagToken.getName());
        assertNull(tagToken.attributes);
        assertFalse(tagToken.isSelfClosing());

        tagToken.name("div");
        String tagName = tagToken.name();
        assertEquals("div", tagName);

        tagToken.name("div").attributes().put("class", "class1 class2");
        String attributesString = tagToken.toString();
        assertTrue(attributesString.contains("class=\"class1 class2\""));

        tagToken.isSelfClosing(true);
        assertTrue(tagToken.isSelfClosing());

        // Test error scenario: setting attributes after setting self-closing tag is not allowed
        tagToken.isSelfClosing(true);
        tagToken.attributes().put("class", "class3");
        String errorMessage = tagToken.toString();
        assertTrue(errorMessage.contains("Can't set attributes on self-closing tag"));
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();

        assertEquals("Comment", resetCommentToken.tokenType().name());
        assertNull(commentToken.getData());

        commentToken.getData().append("This is a comment");
        String commentData = commentToken.getData().toString();
        assertEquals("This is a comment", commentData);

        assertTrue(commentToken.toString().contains("<!--This is a comment-->"));
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();

        assertEquals("Character", resetCharacterToken.tokenType().name());
        assertNull(characterToken.getData());

        characterToken.getData("&nbsp;").append("&nbsp;");
        String characterData = characterToken.getData().toString();
        assertEquals("nbsp;nbsp;", characterData);

        assertTrue(characterToken.toString().equals("&nbsp;"));
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();

        assertEquals("EOF", resetEofToken.tokenType().name());
        assertNull(eofToken);
    }
}
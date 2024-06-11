import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("Doctype", resetToken.tokenType());

        TokenType tokenType = token.isDoctype();
        assertTrue(tokenType == TokenType.Doctype);

        String name = new Token.Doctype().getName();
        assertEquals("", name);

        Token doctypeWithName = new Token.Doctype().name("HTML");
        name = doctypeWithName.getName();
        assertEquals("HTML", name);

        Token doctypeWithPublicIdentifier = new Token.Doctype().name("HTML");
        doctypeWithPublicIdentifier.getPublicIdentifier().append("public");
        String publicIdentifier = doctypeWithPublicIdentifier.getPublicIdentifier().toString();
        assertEquals("public", publicIdentifier);

        Token doctypeWithSystemIdentifier = new Token.Doctype().name("HTML");
        doctypeWithSystemIdentifier.getSystemIdentifier().append("system.html");
        String systemIdentifier = doctypeWithSystemIdentifier.getSystemIdentifier().toString();
        assertEquals("system.html", systemIdentifier);

        Token doctypeWithForceQuirks = new Token.Doctype().name("HTML").isForceQuirks(true);
        assertTrue(doctypeWithForceQuirks.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        assertEquals("StartTag", tagToken.tokenType());

        Token resetTagToken = tagToken.reset();
        assertEquals("StartTag", resetTagToken.tokenType());

        TokenType tagTokenType = tagToken.isStartTag();
        assertTrue(tagTokenType == TokenType.StartTag);

        String tagName = new Token.StartTag().name("p");
        assertEquals("p", tagName);

        Token startTagWithAttributes = new Token.StartTag().name("input").attribute("type", "text").attribute("name", "username");
        assertEquals("input", startTagWithAttributes.name());
        Attributes attributes = startTagWithAttributes.getAttributes();
        assertEquals(2, attributes.size());
        Attribute typeAttribute = attributes.get("type");
        assertEquals("text", typeAttribute.getValue());
        Attribute nameAttribute = attributes.get("name");
        assertEquals("username", nameAttribute.getValue());

        Token endTag = new Token.EndTag().name("p");
        assertEquals("p", endTag.name());

        String tagNameWithLowerCase = new Token.StartTag().name("p").normalName();
        assertEquals("p", tagNameWithLowerCase);

        Token tagWithSelfClosing = new Token.StartTag().name("img").selfClosing(true);
        assertTrue(tagWithSelfClosing.isSelfClosing());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        String data = new Token.Comment().getData();
        assertEquals("", data);

        Token commentWithData = new Token.Comment().data("This is a comment");
        data = commentWithData.getData();
        assertEquals("This is a comment", data);

        Token bogusComment = new Token.Comment().bogus(true);
        assertTrue(bogusComment.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        String characterData = new Token.Character().getData();
        assertEquals("", characterData);

        Token characterWithData = new Token.Character().data("A");
        characterData = characterWithData.getData();
        assertEquals("A", characterData);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertEquals("EOF", resetEofToken.tokenType());

        TokenType eofTokenType = eofToken.isEOF();
        assertTrue(eofTokenType == TokenType.EOF);
    }
}
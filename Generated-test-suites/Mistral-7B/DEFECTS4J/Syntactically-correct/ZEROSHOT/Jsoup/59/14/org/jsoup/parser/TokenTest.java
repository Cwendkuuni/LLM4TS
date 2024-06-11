import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        TokenType doctypeType = doctypeToken.isDoctype();
        assertTrue(doctypeType == TokenType.Doctype);

        String name = new Token.Doctype().reset().getName();
        assertEquals("", name);

        Token doctypeWithName = new Token.Doctype().name("html").reset();
        name = doctypeWithName.getName();
        assertEquals("html", name);

        Token doctypeWithPublicId = new Token.Doctype().name("html").pubSysKey("DTD HTML").reset();
        String pubSysKey = doctypeWithPublicId.getPubSysKey();
        assertEquals("DTD HTML", pubSysKey);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag().name("p").reset();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        TokenType startTagType = startTagToken.isStartTag();
        assertTrue(startTagType == TokenType.StartTag);

        String name = startTagToken.name();
        assertEquals("p", name);

        String normalName = startTagToken.normalName();
        assertEquals("p", normalName);

        Token startTagWithAttributes = new Token.StartTag().name("input").attributes(new Attributes().put("type", "text")).reset();
        Attributes attributes = startTagWithAttributes.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("type");
        assertEquals("text", attribute.getValue());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag().name("p").reset();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        TokenType endTagType = endTagToken.isEndTag();
        assertTrue(endTagType == TokenType.EndTag);

        String name = endTagToken.name();
        assertEquals("p", name);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment().data("This is a comment").reset();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        TokenType commentType = commentToken.isComment();
        assertTrue(commentType == TokenType.Comment);

        String data = commentToken.getData();
        assertEquals("This is a comment", data);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character().data("A").reset();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        TokenType characterType = characterToken.isCharacter();
        assertTrue(characterType == TokenType.Character);

        String data = characterToken.getData();
        assertEquals("A", data);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF().reset();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertEquals("EOF", resetEofToken.tokenType());

        TokenType eofType = eofToken.isEOF();
        assertTrue(eofType == TokenType.EOF);
    }
}
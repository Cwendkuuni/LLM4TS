import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("Doctype", resetToken.tokenType());

        TokenType tokenType = token.isDoctype();
        assertTrue(tokenType == TokenType.Doctype);

        String name = "html";
        token = new Token.Doctype().name(name);
        assertEquals(name, token.asDoctype().getName());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("StartTag", resetToken.tokenType());

        TokenType tokenType = token.isStartTag();
        assertTrue(tokenType == TokenType.StartTag);

        String tagName = "html";
        token = new Token.StartTag().name(tagName);
        assertEquals(tagName, token.asStartTag().name());
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("EndTag", resetToken.tokenType());

        TokenType tokenType = token.isEndTag();
        assertTrue(tokenType == TokenType.EndTag);

        String tagName = "html";
        token = new Token.EndTag().name(tagName);
        assertEquals(tagName, token.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("Comment", resetToken.tokenType());

        TokenType tokenType = token.isComment();
        assertTrue(tokenType == TokenType.Comment);

        String data = "This is a comment";
        token = new Token.Comment().data(data);
        assertEquals(data, token.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("Character", resetToken.tokenType());

        TokenType tokenType = token.isCharacter();
        assertTrue(tokenType == TokenType.Character);

        String data = "ABC";
        token = new Token.Character().data(data);
        assertEquals(data, token.asCharacter().getData());
    }

    @Test
    public void testEofToken() {
        Token token = new Token.EOF();
        assertEquals("EOF", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("EOF", resetToken.tokenType());

        TokenType tokenType = token.isEOF();
        assertTrue(tokenType == TokenType.EOF);
    }
}
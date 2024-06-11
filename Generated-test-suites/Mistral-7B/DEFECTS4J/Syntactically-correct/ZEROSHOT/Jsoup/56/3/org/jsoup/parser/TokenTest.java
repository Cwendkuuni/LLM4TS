import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        TokenType doctypeType = doctypeToken.isDoctype();
        assertTrue(doctypeType == TokenType.Doctype);

        String name = new Token.Doctype().reset().getName();
        assertNull(name);

        // Test getters for Doctype token
        Token doctypeTokenWithData = new Token.Doctype();
        doctypeTokenWithData.asDoctype().getName().append("html");
        doctypeTokenWithData.asDoctype().setForceQuirks(true);
        assertEquals("Doctype", doctypeTokenWithData.tokenType());
        assertEquals("html", doctypeTokenWithData.asDoctype().getName());
        assertTrue(doctypeTokenWithData.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        TokenType startTagType = startTagToken.isStartTag();
        assertTrue(startTagType == TokenType.StartTag);

        String tagName = new Token.StartTag().name("div");
        assertEquals("div", tagName);

        // Test getters and setters for StartTag token
        Token startTagTokenWithAttributes = new Token.StartTag();
        startTagTokenWithAttributes.name("p").attributes().put("class", "text");
        assertEquals("StartTag", startTagTokenWithAttributes.tokenType());
        assertEquals("p", startTagTokenWithAttributes.name());
        assertEquals("class", startTagTokenWithAttributes.getAttributes().keySet().iterator().next());
        assertEquals("text", startTagTokenWithAttributes.getAttributes().get("class"));
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        TokenType endTagType = endTagToken.isEndTag();
        assertTrue(endTagType == TokenType.EndTag);

        String tagName = new Token.EndTag().name("p");
        assertEquals("p", tagName);

        // Test getters for EndTag token
        Token endTagTokenWithData = new Token.EndTag();
        endTagTokenWithData.name("div");
        assertEquals("EndTag", endTagTokenWithData.tokenType());
        assertEquals("div", endTagTokenWithData.name());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        TokenType commentType = commentToken.isComment();
        assertTrue(commentType == TokenType.Comment);

        // Test getters for Comment token
        Token commentTokenWithData = new Token.Comment();
        commentTokenWithData.asComment().getData().append("This is a comment.");
        assertEquals("Comment", commentTokenWithData.tokenType());
        assertEquals("This is a comment.", commentTokenWithData.asComment().getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        TokenType characterType = characterToken.isCharacter();
        assertTrue(characterType == TokenType.Character);

        // Test getters for Character token
        Token characterTokenWithData = new Token.Character();
        characterTokenWithData.asCharacter().data("!");
        assertEquals("Character", characterTokenWithData.tokenType());
        assertEquals("!", characterTokenWithData.asCharacter().getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEOFToken = eofToken.reset();
        assertEquals("EOF", resetEOFToken.tokenType());

        TokenType eofType = eofToken.isEOF();
        assertTrue(eofType == TokenType.EOF);
    }
}
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

        String name = doctypeToken.asDoctype().getName();
        assertNull(name);

        doctypeToken.asDoctype().getName("html");
        name = doctypeToken.asDoctype().getName();
        assertEquals("html", name);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        TokenType startTagType = startTagToken.isStartTag();
        assertTrue(startTagType == TokenType.StartTag);

        String name = startTagToken.asStartTag().name();
        assertNull(name);

        startTagToken.asStartTag().name("div");
        name = startTagToken.asStartTag().name();
        assertEquals("div", name);

        Attributes attributes = new Attributes();
        attributes.put("id", "someId");
        startTagToken.asStartTag().nameAttr("div", attributes);
        assertEquals("div", startTagToken.asStartTag().name());
        assertEquals("id", startTagToken.asStartTag().getAttributes().get("id").getName());
        assertEquals("someId", startTagToken.asStartTag().getAttributes().get("id").getValue());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        TokenType endTagType = endTagToken.isEndTag();
        assertTrue(endTagType == TokenType.EndTag);

        String name = endTagToken.asEndTag().name();
        assertNull(name);

        endTagToken.asEndTag().name("div");
        name = endTagToken.asEndTag().name();
        assertEquals("div", name);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        TokenType commentType = commentToken.isComment();
        assertTrue(commentType == TokenType.Comment);

        String data = commentToken.asComment().getData();
        assertNull(data);

        commentToken.asComment().getData("This is a comment");
        data = commentToken.asComment().getData();
        assertEquals("This is a comment", data);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        TokenType characterType = characterToken.isCharacter();
        assertTrue(characterType == TokenType.Character);

        String data = characterToken.asCharacter().getData();
        assertNull(data);

        characterToken.asCharacter().data("A");
        data = characterToken.asCharacter().getData();
        assertEquals("A", data);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertEquals("EOF", resetEofToken.tokenType());

        TokenType eofType = eofToken.isEOF();
        assertTrue(eofType == TokenType.EOF);
    }
}
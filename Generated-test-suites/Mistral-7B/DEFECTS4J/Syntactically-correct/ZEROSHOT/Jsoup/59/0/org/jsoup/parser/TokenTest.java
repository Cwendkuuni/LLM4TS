import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokenTest {

    private Token doctypeToken;
    private Token startTagToken;
    private Token endTagToken;
    private Token commentToken;
    private Token characterToken;
    private Token eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeToken() {
        assertEquals(TokenType.Doctype, doctypeToken.type);
        assertNotNull(doctypeToken.asDoctype());
        assertEquals("Doctype", doctypeToken.tokenType());
        doctypeToken.reset();
        assertNull(doctypeToken.asDoctype());
    }

    @Test
    public void testStartTagToken() {
        assertEquals(TokenType.StartTag, startTagToken.type);
        assertNotNull(startTagToken.asStartTag());
        assertEquals("StartTag", startTagToken.tokenType());
        startTagToken.reset();
        assertNull(startTagToken.asStartTag());
    }

    @Test
    public void testEndTagToken() {
        assertEquals(TokenType.EndTag, endTagToken.type);
        assertNotNull(endTagToken.asEndTag());
        assertEquals("EndTag", endTagToken.tokenType());
        endTagToken.reset();
        assertNull(endTagToken.asEndTag());
    }

    @Test
    public void testCommentToken() {
        assertEquals(TokenType.Comment, commentToken.type);
        assertNotNull(commentToken.asComment());
        assertEquals("Comment", commentToken.tokenType());
        commentToken.reset();
        assertNull(commentToken.asComment());
    }

    @Test
    public void testCharacterToken() {
        assertEquals(TokenType.Character, characterToken.type);
        assertNotNull(characterToken.asCharacter());
        assertEquals("Character", characterToken.tokenType());
        characterToken.reset();
        assertNull(characterToken.asCharacter());
    }

    @Test
    public void testEOFToken() {
        assertEquals(TokenType.EOF, eofToken.type);
        assertNotNull(eofToken.asEOF());
        assertEquals("EOF", eofToken.tokenType());
        eofToken.reset();
        assertNull(eofToken.asEOF());
    }
}
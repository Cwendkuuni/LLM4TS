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
    public void setup() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeTokenReset() {
        doctypeToken.reset();
        assertEquals(TokenType.Doctype, doctypeToken.isDoctype());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testDoctypeTokenSetProperties() {
        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().setPublicIdentifier("Public ID");
        doctypeToken.asDoctype().setSystemIdentifier("System ID");
        doctypeToken.asDoctype().setForceQuirks(true);

        assertEquals("html", doctypeToken.asDoctype().getName());
        assertEquals("Public ID", doctypeToken.asDoctype().getPublicIdentifier());
        assertEquals("System ID", doctypeToken.asDoctype().getSystemIdentifier());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagTokenReset() {
        startTagToken.reset();
        assertEquals(TokenType.StartTag, startTagToken.isStartTag());
        assertNull(startTagToken.name());
        assertNull(startTagToken.normalName());
        assertNull(startTagToken.getAttributes());
        assertFalse(startTagToken.isSelfClosing());
    }

    @Test
    public void testStartTagTokenSetProperties() {
        startTagToken.name("div");
        startTagToken.normalName("div");
        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2");
        startTagToken.setAttributes(attributes);
        startTagToken.setSelfClosing(true);

        assertEquals("div", startTagToken.name());
        assertEquals("div", startTagToken.normalName());
        assertEquals("class1 class2", startTagToken.getAttributes().toString());
        assertTrue(startTagToken.isSelfClosing());
    }

    @Test
    public void testEndTagTokenReset() {
        endTagToken.reset();
        assertEquals(TokenType.EndTag, endTagToken.isEndTag());
        assertNull(endTagToken.name());
        assertNull(endTagToken.normalName());
        assertNull(endTagToken.getAttributes());
        assertFalse(endTagToken.isSelfClosing());
    }

    @Test
    public void testEndTagTokenSetProperties() {
        endTagToken.name("div");
        endTagToken.normalName("div");
        assertNull(endTagToken.getAttributes());
        assertFalse(endTagToken.isSelfClosing());

        assertEquals("div", endTagToken.name());
        assertEquals("div", endTagToken.normalName());
    }

    @Test
    public void testCommentTokenReset() {
        commentToken.reset();
        assertEquals(TokenType.Comment, commentToken.isComment());
        assertNull(commentToken.getData());
        assertFalse(commentToken.isSelfClosing());
    }

    @Test
    public void testCommentTokenSetProperties() {
        commentToken.getData().append("This is a comment");

        assertEquals("This is a comment", commentToken.getData());
    }

    @Test
    public void testCharacterTokenReset() {
        characterToken.reset();
        assertEquals(TokenType.Character, characterToken.isCharacter());
        assertNull(characterToken.getData());
        assertFalse(characterToken.isSelfClosing());
    }

    @Test
    public void testCharacterTokenSetProperties() {
        characterToken.getData().append("A");

        assertEquals("A", characterToken.getData());
    }

    @Test
    public void testEOFTokenReset() {
        eofToken.reset();
        assertEquals(TokenType.EOF, eofToken.isEOF());
    }

    @Test(expected = AssertionError.class)
    public void testTokenInvalidOperations() {
        Token invalidToken = new Token();
        invalidToken.reset();
        invalidToken.asDoctype(); // Doctype methods should not be called on non-Doctype tokens
        invalidToken.getName(); // Name methods should only be called on StartTag and EndTag tokens
    }
}
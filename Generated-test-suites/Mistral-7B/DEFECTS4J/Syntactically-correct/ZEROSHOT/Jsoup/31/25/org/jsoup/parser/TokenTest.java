import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctype() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        Token doctypeTokenWithName = new Token.Doctype();
        doctypeTokenWithName.asDoctype().getName().append("html");
        assertEquals("Doctype[html]", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());

        Token doctypeTokenWithNameAndForceQuirks = new Token.Doctype();
        doctypeTokenWithNameAndForceQuirks.asDoctype().getName().append("html");
        doctypeTokenWithNameAndForceQuirks.asDoctype().setForceQuirks(true);
        assertEquals("Doctype[html][forceQuirks=true]", doctypeTokenWithNameAndForceQuirks.tokenType());
        assertTrue(doctypeTokenWithNameAndForceQuirks.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTag() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());
        assertNull(startTagToken.asStartTag().getName());
        assertFalse(startTagToken.asStartTag().isSelfClosing());
        assertNull(startTagToken.asStartTag().getAttributes());

        Token startTagTokenWithName = new Token.StartTag();
        startTagTokenWithName.asStartTag().name("div");
        assertEquals("StartTag[div]", startTagTokenWithName.tokenType());
        assertEquals("div", startTagTokenWithName.asStartTag().getName());
        assertFalse(startTagTokenWithName.asStartTag().isSelfClosing());
        assertNull(startTagTokenWithName.asStartTag().getAttributes());

        Token startTagTokenWithNameAndAttributes = new Token.StartTag();
        startTagTokenWithNameAndAttributes.asStartTag().name("div").attributes(new Attributes().put("class", "class1").put("id", "id1"));
        assertEquals("StartTag[div][class=class1 id=id1]", startTagTokenWithNameAndAttributes.tokenType());
        assertEquals("div", startTagTokenWithNameAndAttributes.asStartTag().getName());
        assertFalse(startTagTokenWithNameAndAttributes.asStartTag().isSelfClosing());
        assertEquals("class1 id1", startTagTokenWithNameAndAttributes.asStartTag().getAttributes().toString());
    }

    @org.junit.Test
    public void testEndTag() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());
        assertNull(endTagToken.asEndTag().getName());

        Token endTagTokenWithName = new Token.EndTag();
        endTagTokenWithName.asEndTag().name("div");
        assertEquals("EndTag[div]", endTagTokenWithName.tokenType());
        assertEquals("div", endTagTokenWithName.asEndTag().getName());

        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("div");
        Token endTagTokenWithSameName = new Token.EndTag();
        endTagTokenWithSameName.asEndTag().name("div");
        Token tokens[] = {startTagToken, endTagTokenWithSameName};
        Token expectedToken = new Token.EndTag();
        expectedToken.asEndTag().name("div");
        assertEquals(expectedToken, tokens[1]);
    }

    @org.junit.Test
    public void testComment() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());
        assertNull(commentToken.asComment().getData());

        Token commentTokenWithData = new Token.Comment();
        commentTokenWithData.asComment().getData().append("This is a comment");
        assertEquals("Comment[This is a comment]", commentTokenWithData.tokenType());
        assertEquals("This is a comment", commentTokenWithData.asComment().getData());
    }

    @org.junit.Test
    public void testCharacter() {
        Token characterToken = new Token.Character('A');
        assertEquals("Character[A]", characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());

        Token characterTokenWithMultipleCharacters = new Token.Character("AB");
        assertEquals("Character[AB]", characterTokenWithMultipleCharacters.tokenType());
        assertEquals("AB", characterTokenWithMultipleCharacters.asCharacter().getData());
    }

    @org.junit.Test
    public void testEOF() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());
        assertTrue(eofToken.isEOF());
    }

    @org.junit.Test
    public void testTokenTypeRecognition() {
        Token[] tokens = new Token[]{
                new Token.Doctype(),
                new Token.Comment(),
                new Token.StartTag(),
                new Token.EndTag(),
                new Token.Character('A'),
                new Token.Character('\n'),
                new Token.EOF()
        };
        TokenType expectedTokenType[] = {
                TokenType.Doctype,
                TokenType.Comment,
                TokenType.StartTag,
                TokenType.EndTag,
                TokenType.Character,
                TokenType.Character,
                TokenType.EOF
        };
        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];
            TokenType expectedTokenType = expectedTokenType[i];
            assertEquals(expectedTokenType, token.getType());
        }
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeWithEmptyName() {
        Doctype doctype = new Doctype();
        doctype.newName(); // call constructor
        assertEquals("Doctype", doctype.tokenType()); // name is empty by default
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeWithEmptyPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.newName(); // call constructor
        doctype.newPublicIdentifier(" "); // set public identifier
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeWithEmptySystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.newName(); // call constructor
        doctype.newSystemIdentifier(" "); // set system identifier
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeWithNullName() {
        Doctype doctype = new Doctype();
        doctype.newName(null); // set name to null
        fail("Doctype should not be created with a null name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeWithNullPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.newPublicIdentifier(null); // set public identifier to null
        fail("Doctype should not be created with a null public identifier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeWithNullSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.newSystemIdentifier(null); // set system identifier to null
        fail("Doctype should not be created with a null system identifier");
    }

    @Test
    public void testDoctypeWithForceQuirksTrue() {
        Doctype doctype = new Doctype();
        doctype.newForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeWithInvalidNameCharacters() {
        Doctype doctype = new Doctype();
        doctype.newName("&<"); // set name to invalid characters
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeWithInvalidSystemIdentifierCharacters() {
        Doctype doctype = new Doctype();
        doctype.newName("Doctype"); // set name
        doctype.newSystemIdentifier("&<"); // set system identifier to invalid characters
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag("div");
        assertEquals("StartTag", startTag.tokenType());
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals("div", startTag.name());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag("div");
        assertEquals("EndTag", endTag.tokenType());
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals("div", endTag.name());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType());
        assertEquals(TokenType.Comment, comment.type);
        assertNotNull(comment.getData());
        assertEquals("", comment.getData());
    }

    @Test
    public void testCharacter() {
        Character character = new Character("A");
        assertEquals("Character", character.tokenType());
        assertEquals(TokenType.Character, character.type);
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType());
        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTypes() {
        Token doctype = new Doctype();
        Token startTag = new StartTag("div");
        Token endTag = new EndTag("div");
        Token comment = new Comment();
        Token character = new Character('A');
        Token eof = new EOF();

        Token[] tokens = {doctype, startTag, endTag, comment, character, eof};

        for (Token token : tokens) {
            String tokenType = token.tokenType();
            TokenType expectedTokenType = token.type;
            assertEquals(tokenType, expectedTokenType.name());
        }
    }
}
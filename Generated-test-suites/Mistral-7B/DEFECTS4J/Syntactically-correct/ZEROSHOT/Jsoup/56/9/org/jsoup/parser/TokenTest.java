import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertTrue(doctypeToken.isDoctype());
        Doctype doctype = (Doctype) doctypeToken;
        doctype.getName("html");
        assertEquals("html", doctype.getName());
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testResetDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName("html");
        doctypeToken.setForceQuirks(true);
        doctypeToken.reset();
        assertFalse(doctypeToken.isDoctype());
        assertNull(doctypeToken.asDoctype().getName());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.name("div");
        assertTrue(startTagToken.isStartTag());
        StartTag divTag = (StartTag) startTagToken;
        assertEquals("div", divTag.name());
        divTag.newAttribute().name("class").value("test");
        assertEquals("class", divTag.getAttributes().getClassAttr());
        divTag.finaliseTag();
        assertEquals("<div class=\"test\">", divTag.toString());
    }

    @Test
    public void testResetStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.name("div");
        StartTag divTag = (StartTag) startTagToken;
        divTag.newAttribute().name("class").value("test");
        divTag.finaliseTag();
        assertEquals("<div class=\"test\">", divTag.toString());
        divTag.reset();
        assertFalse(divTag.isStartTag());
        assertNull(divTag.getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.name("div");
        assertTrue(endTagToken.isEndTag());
        EndTag divEndTag = (EndTag) endTagToken;
        assertEquals("div", divEndTag.name());
        assertEquals("</div>", divEndTag.toString());
    }

    @Test
    public void testResetEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.name("div");
        EndTag divEndTag = (EndTag) endTagToken;
        assertEquals("</div>", divEndTag.toString());
        divEndTag.reset();
        assertFalse(divEndTag.isEndTag());
        assertNull(divEndTag.name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertTrue(commentToken.isComment());
        Comment comment = (Comment) commentToken;
        comment.getData("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testResetCommentToken() {
        Token commentToken = new Token.Comment();
        Comment comment = (Comment) commentToken;
        comment.getData("This is a comment");
        comment.reset();
        assertFalse(commentToken.isComment());
        assertNull(comment.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.data("A");
        assertTrue(characterToken.isCharacter());
        Character character = (Character) characterToken;
        assertEquals("A", character.getData());
        assertEquals("A", character.toString());
    }

    @Test
    public void testResetCharacterToken() {
        Token characterToken = new Token.Character();
        Character character = (Character) characterToken;
        character.data("A");
        character.reset();
        assertFalse(characterToken.isCharacter());
        assertNull(character.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertTrue(eofToken.isEOF());
        assertEquals("", eofToken.toString());
    }

    @Test
    public void testResetEOFToken() {
        Token eofToken = new Token.EOF();
        assertFalse(eofToken.isEOF());
    }
}
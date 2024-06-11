import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.CData;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());
        doctype.reset();
        assertNull(doctype.getName());
        doctype.getName("html");
        assertEquals("html", doctype.getName());
        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
        doctype.getPublicIdentifier("publicId");
        assertEquals("publicId", doctype.getPublicIdentifier());
        doctype.reset();
        assertNull(doctype.getSystemIdentifier());
        doctype.getSystemIdentifier("systemId");
        assertEquals("systemId", doctype.getSystemIdentifier());
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        assertEquals("StartTag", startTag.tokenType().name());
        startTag.reset();
        assertNull(startTag.tagName);
        startTag.name("div");
        assertEquals("div", startTag.tagName);
        startTag.reset();
        assertNull(startTag.normalName);
        startTag.name("DIV");
        assertEquals("div", startTag.normalName());
        startTag.reset();
        assertFalse(startTag.isSelfClosing());
        startTag.isSelfClosing(true);
        assertTrue(startTag.isSelfClosing());
        startTag.reset();
        assertNull(startTag.getAttributes());
        startTag.nameAttr("id", "idValue");
        Tag tag = startTag.asStartTag();
        assertEquals("div id=\"idValue\"", tag.toString());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        assertEquals("EndTag", endTag.tokenType().name());
        endTag.reset();
        assertEquals("", endTag.tagName);
        endTag.name("div");
        assertEquals("div", endTag.tagName);
        endTag.reset();
        assertEquals("", endTag.normalName());
        endTag.name("DIV");
        assertEquals("div", endTag.normalName());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType().name());
        comment.reset();
        assertEquals("", comment.getData());
        comment.getData("commentData");
        assertEquals("commentData", comment.getData());
        comment.reset();
        assertFalse(comment.isBogus());
        comment.bogus(true);
        assertTrue(comment.isBogus());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        assertEquals("Character", character.tokenType().name());
        character.reset();
        assertNull(character.getData());
        character.data("test");
        assertEquals("test", character.getData());
    }

    @Test
    public void testCData() {
        CData cData = new CData("test");
        assertEquals("Character", cData.tokenType().name());
        assertEquals("<![CDATA[test]]>", cData.toString());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();
        invalidToken.reset();
        invalidToken.getData("invalidToken");
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType().name());
    }
}
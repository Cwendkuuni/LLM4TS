import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.newName("html");
        assertEquals("html", doctype.asDoctype().getName());

        doctype.newPubSysKey("DTD XHTML 1.0");
        assertEquals("DTD XHTML 1.0", doctype.asDoctype().getPubSysKey());

        doctype.newPublicIdentifier("//W3C//DTD XHTML 1.0 Strict//EN");
        assertEquals("//W3C//DTD XHTML 1.0 Strict//EN", doctype.asDoctype().getPublicIdentifier());

        doctype.newSystemIdentifier("/path/to/myfile.xhtml");
        assertEquals("/path/to/myfile.xhtml", doctype.asDoctype().getSystemIdentifier());

        doctype.newForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());

        Token resetDoctype = doctype.reset();
        assertFalse(resetDoctype.isForceQuirks());
        assertEquals(TokenType.Doctype, resetDoctype.type);
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.type);

        startTag.newName("html").newAttributes();
        assertEquals("html", startTag.asStartTag().name());
        assertNull(startTag.asStartTag().getAttributes());

        startTag.newAttribute("lang").setValue("en");
        assertEquals("en", startTag.asStartTag().getAttributes().getValue("lang"));

        startTag.newAttribute("class").setValue("myClass");
        assertEquals("myClass", startTag.asStartTag().getAttributes().getValue("class"));

        startTag.newAttribute("id").setValue("myId");
        assertEquals("myId", startTag.asStartTag().getAttributes().getValue("id"));

        startTag.newEmptyAttribute("xmlns");
        assertTrue(startTag.asStartTag().getAttributes().containsKey("xmlns"));
        assertNull(startTag.asStartTag().getAttributes().getValue("xmlns"));

        Token resetStartTag = startTag.reset();
        assertNull(resetStartTag.asStartTag().name());
        assertNull(resetStartTag.asStartTag().getAttributes());
        assertEquals(TokenType.StartTag, resetStartTag.type);
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);

        endTag.newName("html");
        assertEquals("html", endTag.asEndTag().name());

        Token resetEndTag = endTag.reset();
        assertNull(resetEndTag.asEndTag().name());
        assertEquals(TokenType.EndTag, resetEndTag.type);
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);

        comment.newData("This is a comment");
        assertEquals("This is a comment", comment.asComment().getData());

        Token resetComment = comment.reset();
        assertNull(resetComment.asComment().getData());
        assertEquals(TokenType.Comment, resetComment.type);
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        assertEquals(TokenType.Character, character.type);

        character.newData("Hello World!");
        assertEquals("Hello World!", character.asCharacter().getData());

        Token resetCharacter = character.reset();
        assertNull(resetCharacter.asCharacter().getData());
        assertEquals(TokenType.Character, resetCharacter.type);
    }

    @Test
    public void testCDataToken() {
        Token cData = new Token.CData("<![CDATA[Hello World!]]>");
        assertEquals(TokenType.Character, cData.type);
        assertTrue(cData instanceof Token.CData);
        assertEquals("<![CDATA[Hello World!]]>", cData.toString());

        Token resetCData = cData.reset();
        assertNull(resetCData.getData());
        assertEquals(TokenType.Character, resetCData.type);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();
        invalidToken.type = null;
        invalidToken.reset();
    }
}
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.tokenType());
        doctype.reset();
        assertNull(doctype.getName());
        assertFalse(doctype.isForceQuirks());

        doctype.getName("html");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertFalse(doctype.isForceQuirks());

        doctype.setForceQuirks(true);
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
        doctype.pubSysKey("foo");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("foo", doctype.getPubSysKey());
        assertFalse(doctype.isForceQuirks());

        doctype.systemIdentifier("bar.html");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("foo", doctype.getPubSysKey());
        assertEquals("bar.html", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tag = new Token.StartTag();
        assertEquals(TokenType.StartTag, tag.tokenType());
        tag.reset();
        assertNull(tag.tagName);
        assertFalse(tag.isSelfClosing());

        tag.tagName("div");
        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.tagName());
        assertEquals("div", tag.normalName());
        assertFalse(tag.isSelfClosing());

        tag.newAttribute();
        tag.newAttribute().name("class").value("foo");
        tag.newAttribute();
        tag.newAttribute().name("id").value("bar");
        assertEquals("div", tag.tagName());
        assertEquals("div", tag.normalName());
        assertEquals("class=foo id=bar", tag.toString());

        tag.finaliseTag();
        assertEquals("div", tag.tagName());
        assertEquals("div", tag.normalName());
        assertNull(tag.attributes);

        tag.reset();
        tag.tagName("span").selfClosing(true);
        assertEquals("StartTag", tag.tokenType());
        assertEquals("span", tag.tagName());
        assertEquals("span", tag.normalName());
        assertTrue(tag.isSelfClosing());
        assertNull(tag.attributes);
        assertEquals("/span", tag.toString());

        tag.reset();
        tag.tagName("input").attributes(new Attributes().put("type", "text"));
        assertEquals("StartTag", tag.tokenType());
        assertEquals("input", tag.tagName());
        assertEquals("input", tag.normalName());
        assertFalse(tag.isSelfClosing());
        assertEquals("input type=text", tag.toString());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.tokenType());
        comment.reset();
        assertFalse(comment.isForceQuirks());

        comment.data("This is a comment");
        assertEquals("Comment", comment.tokenType());
        assertEquals("", comment.tagName());
        assertFalse(comment.isForceQuirks());
        assertEquals("<!--This is a comment-->", comment.toString());

        comment.reset();
        comment.bogus(true);
        assertEquals("Comment", comment.tokenType());
        assertEquals("", comment.tagName());
        assertTrue(comment.isForceQuirks());
        assertEquals("<!--", comment.toString());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        character.reset();
        assertNull(character.getData());

        character.data("abc");
        assertEquals("Character", character.tokenType());
        assertEquals("", character.tagName());
        assertEquals("abc", character.getData());
        assertEquals("abc", character.toString());

        character.reset();
        character.data("\n");
        assertEquals("Character", character.tokenType());
        assertEquals("", character.tagName());
        assertEquals("\n", character.getData());
        assertEquals("\n", character.toString());

        character.reset();
        character.data('&');
        assertEquals("Character", character.tokenType());
        assertEquals("", character.tagName());
        assertEquals("&", character.getData());
        assertEquals("&", character.toString());
    }

    @Test
    public void testCDataToken() {
        Token cdata = new Token.CData("[cdatab]cdatac[/cdatab]");
        assertEquals(TokenType.Character, cdata.tokenType());
        assertEquals("", cdata.tagName());
        assertEquals("[cdatab]cdatac[/cdatab]", cdata.getData());
        assertEquals("<![CDATA[cdatab]cdatac[/CDATA[>", cdata.toString());

        cdata.reset();
        cdata.data("<![CDATA[foo]]>");
        assertEquals("Character", cdata.tokenType());
        assertEquals("", cdata.tagName());
        assertEquals("<![CDATA[foo]]>", cdata.getData());
        assertEquals("<![CDATA[foo]]>", cdata.toString());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.tokenType());
        eof.reset();
        assertEquals("EOF", eof.tokenType());
        assertNull(eof.getData());
        assertEquals("", eof.toString());
    }
}
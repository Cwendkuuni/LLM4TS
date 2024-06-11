import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        Assert.assertNull(doctype.asDoctype().getName());
        Assert.assertNull(doctype.asDoctype().getPubSysKey());
        Assert.assertNull(doctype.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctype.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctype.asDoctype().isForceQuirks());

        Token resetDoctype = doctype.reset();
        assertEquals("Doctype", resetDoctype.tokenType());
        Assert.assertNull(resetDoctype.asDoctype().getName());
        Assert.assertNull(resetDoctype.asDoctype().getPubSysKey());
        Assert.assertNull(resetDoctype.asDoctype().getPublicIdentifier());
        Assert.assertNull(resetDoctype.asDoctype().getSystemIdentifier());
        Assert.assertFalse(resetDoctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.asStartTag().getAttributes());

        Token resetStartTag = startTag.reset();
        assertEquals("StartTag", resetStartTag.tokenType());
        assertFalse(resetStartTag.isSelfClosing());
        assertNull(resetStartTag.asStartTag().getAttributes());

        Token startTagName = startTag.name("html");
        assertEquals("html", startTagName.asStartTag().name());
        assertEquals("HTML", startTagName.asStartTag().normalName());
        assertEquals("html", startTagName.toString());

        Token startTagAttributes = startTagName.attributes().put("class", "TestClass");
        assertEquals("html class=\"TestClass\"", startTagAttributes.toString());

        Token resetStartTagAttributes = startTagAttributes.reset();
        assertNull(resetStartTagAttributes.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("html", endTag.asEndTag().name());
        assertEquals("HTML", endTag.asEndTag().normalName());
        assertEquals("</html>", endTag.toString());

        Token resetEndTag = endTag.reset();
        assertEquals("EndTag", resetEndTag.tokenType());
        assertEquals("", resetEndTag.toString());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        Assert.assertNull(comment.asComment().getData());
        assertEquals("<!-- -->", comment.toString());

        Token resetComment = comment.reset();
        assertEquals("Comment", resetComment.tokenType());
        Assert.assertNull(resetComment.asComment().getData());
        assertEquals("", resetComment.toString());

        Token commentData = comment.appendAttributeValue("Test Comment");
        assertEquals("<!--Test Comment -->", commentData.toString());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        character.appendAttributeValue('A');
        assertEquals('A', character.getData().charAt(0));
        assertEquals('A', character.toString());

        Token resetCharacter = character.reset();
        assertFalse(resetCharacter.isCharacter());
    }

    @Test
    public void testCDataToken() {
        Token cData = new Token.CData("Test CData");
        assertEquals("Character", cData.tokenType());
        assertEquals("<![CDATA[Test CData]]>", cData.toString());

        Token resetCData = cData.reset();
        assertFalse(resetCData.isCData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertFalse(eof.isEOF());
        assertEquals("", eof.toString());
    }
}
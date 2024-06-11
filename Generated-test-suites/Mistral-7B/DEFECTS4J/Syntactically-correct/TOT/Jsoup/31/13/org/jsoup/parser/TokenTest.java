import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.isDoctype());
        Assert.assertNull(doctype.asDoctype().getName());
        Assert.assertNull(doctype.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctype.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctype.asDoctype().isForceQuirks());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().getName().append("html");
        Assert.assertEquals("Doctype", doctypeWithName.tokenType());
        Assert.assertEquals("html", doctypeWithName.asDoctype().getName());
        Assert.assertNull(doctypeWithName.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctypeWithName.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctypeWithName.asDoctype().isForceQuirks());

        Token doctypeWithNameAndIdentifier = new Token.Doctype();
        doctypeWithNameAndIdentifier.asDoctype().getName().append("html");
        doctypeWithNameAndIdentifier.asDoctype().getPublicIdentifier().append("PUBLIC");
        doctypeWithNameAndIdentifier.asDoctype().getSystemIdentifier().append("http://www.w3.org/TR/html4/strict.dtd");
        Assert.assertEquals("Doctype", doctypeWithNameAndIdentifier.tokenType());
        Assert.assertEquals("html", doctypeWithNameAndIdentifier.asDoctype().getName());
        Assert.assertEquals("PUBLIC", doctypeWithNameAndIdentifier.asDoctype().getPublicIdentifier());
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctypeWithNameAndIdentifier.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctypeWithNameAndIdentifier.asDoctype().isForceQuirks());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().setForceQuirks(true);
        Assert.assertEquals("Doctype", doctypeWithForceQuirks.tokenType());
        Assert.assertNull(doctypeWithForceQuirks.asDoctype().getName());
        Assert.assertNull(doctypeWithForceQuirks.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctypeWithForceQuirks.asDoctype().getSystemIdentifier());
        Assert.assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, startTag.isStartTag());
        Assert.assertNull(startTag.asStartTag().name());
        Assert.assertNull(startTag.asStartTag().getAttributes());
        Assert.assertFalse(startTag.asStartTag().isSelfClosing());

        Token startTagWithName = new Token.StartTag("div");
        Assert.assertEquals("StartTag", startTagWithName.tokenType());
        Assert.assertEquals("div", startTagWithName.asStartTag().name());
        Assert.assertNull(startTagWithName.asStartTag().getAttributes());
        Assert.assertFalse(startTagWithName.asStartTag().isSelfClosing());

        Token startTagWithNameAndAttributes = new Token.StartTag("div").name("class").newAttribute().put("id", "test");
        Assert.assertEquals("StartTag", startTagWithNameAndAttributes.tokenType());
        Assert.assertEquals("div", startTagWithNameAndAttributes.asStartTag().name());
        Assert.assertNotNull(startTagWithNameAndAttributes.asStartTag().getAttributes());
        Assert.assertEquals(1, startTagWithNameAndAttributes.asStartTag().getAttributes().size());
        Assert.assertEquals("class", startTagWithNameAndAttributes.asStartTag().getAttributes().keySet().iterator().next().toString());
        Assert.assertEquals("test", startTagWithNameAndAttributes.asStartTag().getAttributes().get("class").getValue());
        Assert.assertEquals("id", startTagWithNameAndAttributes.asStartTag().getAttributes().keySet().iterator().next().toString());
        Assert.assertEquals("test", startTagWithNameAndAttributes.asStartTag().getAttributes().get("id").getValue());
        Assert.assertFalse(startTagWithNameAndAttributes.asStartTag().isSelfClosing());

        Token startTagWithSelfClosing = new Token.StartTag("img").name("src").newAttribute().put("src", "image.png").newAttribute().put("alt", "Image").isSelfClosing(true);
        Assert.assertEquals("StartTag", startTagWithSelfClosing.tokenType());
        Assert.assertEquals("img", startTagWithSelfClosing.asStartTag().name());
        Assert.assertNotNull(startTagWithSelfClosing.asStartTag().getAttributes());
        Assert.assertEquals(2, startTagWithSelfClosing.asStartTag().getAttributes().size());
        Assert.assertEquals("src", startTagWithSelfClosing.asStartTag().getAttributes().keySet().iterator().next().toString());
        Assert.assertEquals("image.png", startTagWithSelfClosing.asStartTag().getAttributes().get("src").getValue());
        Assert.assertEquals("alt", startTagWithSelfClosing.asStartTag().getAttributes().keySet().iterator().next().toString());
        Assert.assertEquals("Image", startTagWithSelfClosing.asStartTag().getAttributes().get("alt").getValue());
        Assert.assertTrue(startTagWithSelfClosing.asStartTag().isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        Assert.assertEquals(TokenType.EndTag, endTag.isEndTag());
        Assert.assertNull(endTag.asEndTag().name());
        Assert.assertNull(endTag.asEndTag().getAttributes());

        Token endTagWithName = new Token.EndTag("div");
        Assert.assertEquals("EndTag", endTagWithName.tokenType());
        Assert.assertEquals("div", endTagWithName.asEndTag().name());
        Assert.assertNull(endTagWithName.asEndTag().getAttributes());

        Token endTagWithNameAndAttributes = new Token.EndTag("p").newAttribute().put("class", "text");
        Assert.assertEquals("EndTag", endTagWithNameAndAttributes.tokenType());
        Assert.assertEquals("p", endTagWithNameAndAttributes.asEndTag().name());
        Assert.assertNotNull(endTagWithNameAndAttributes.asEndTag().getAttributes());
        Assert.assertEquals(1, endTagWithNameAndAttributes.asEndTag().getAttributes().size());
        Assert.assertEquals("class", endTagWithNameAndAttributes.asEndTag().getAttributes().keySet().iterator().next().toString());
        Assert.assertEquals("text", endTagWithNameAndAttributes.asEndTag().getAttributes().get("class").getValue());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, comment.isComment());
        Assert.assertNotNull(comment.asComment().getData());
        Assert.assertEquals("", comment.asComment().getData().toString());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment.");
        Assert.assertEquals("Comment", commentWithData.tokenType());
        Assert.assertNotNull(commentWithData.asComment().getData());
        Assert.assertEquals("This is a comment.", commentWithData.asComment().getData().toString());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character(' ');
        Assert.assertEquals(TokenType.Character, character.isCharacter());
        Assert.assertEquals(" ", character.asCharacter().getData());

        Token characterWithData = new Token.Character('&');
        characterWithData.asCharacter().getData().append("amp;");
        Assert.assertEquals("Character", characterWithData.tokenType());
        Assert.assertEquals("&amp;", characterWithData.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eof.isEOF());
    }
}
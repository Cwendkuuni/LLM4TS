import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctypeToken.tokenType());

        doctypeToken.asDoctype().reset();
        Assert.assertNull(doctypeToken.asDoctype().getName());
        Assert.assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.asDoctype().getName("html");
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
        Assert.assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.asDoctype().setName("html");
        doctypeToken.asDoctype().getPublicIdentifier().append("publicIdentifier");
        doctypeToken.asDoctype().getSystemIdentifier().append("systemIdentifier");
        doctypeToken.asDoctype().isForceQuirks(true);

        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
        Assert.assertEquals("publicIdentifier", doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertEquals("systemIdentifier", doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, tagToken.tokenType());

        tagToken.asStartTag().reset();
        Assert.assertNull(tagToken.asStartTag().getName());
        Assert.assertNull(tagToken.asStartTag().getAttributes());
        Assert.assertFalse(tagToken.asStartTag().isSelfClosing());

        tagToken.asStartTag().name("div").attributes(new Attributes());
        Assert.assertEquals("div", tagToken.asStartTag().getName());
        Assert.assertNotNull(tagToken.asStartTag().getAttributes());
        Assert.assertFalse(tagToken.asStartTag().isSelfClosing());

        tagToken.asStartTag().name("img").attributes().put("src", "image.jpg");
        Assert.assertEquals("img", tagToken.asStartTag().getName());
        Assert.assertNotNull(tagToken.asStartTag().getAttributes());
        Assert.assertFalse(tagToken.asStartTag().isSelfClosing());
        Assert.assertEquals("image.jpg", tagToken.asStartTag().getAttributes().get("src"));

        tagToken.asStartTag().name("input").attributes().put("type", "text").put("name", "username").isSelfClosing(true);
        Assert.assertEquals("input", tagToken.asStartTag().getName());
        Assert.assertNotNull(tagToken.asStartTag().getAttributes());
        Assert.assertTrue(tagToken.asStartTag().isSelfClosing());
        Assert.assertEquals("text", tagToken.asStartTag().getAttributes().get("type"));
        Assert.assertEquals("username", tagToken.asStartTag().getAttributes().get("name"));
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, commentToken.tokenType());

        commentToken.asComment().reset();
        Assert.assertNull(commentToken.asComment().getData());
        Assert.assertFalse(commentToken.asComment().isBogus());

        commentToken.asComment().getData("This is a comment.");
        Assert.assertEquals("This is a comment.", commentToken.asComment().getData());
        Assert.assertFalse(commentToken.asComment().isBogus());

        commentToken.asComment().setData("This is a bogus comment.");
        Assert.assertEquals("This is a bogus comment.", commentToken.asComment().getData());
        Assert.assertTrue(commentToken.asComment().isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Assert.assertEquals(TokenType.Character, characterToken.tokenType());

        characterToken.asCharacter().reset();
        Assert.assertNull(characterToken.asCharacter().getData());

        characterToken.asCharacter().data("A");
        Assert.assertEquals("A", characterToken.asCharacter().getData());

        characterToken.asCharacter().setData("B");
        Assert.assertEquals("B", characterToken.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eofToken.tokenType());

        Assert.assertFalse(eofToken.isDoctype());
        Assert.assertFalse(eofToken.isStartTag());
        Assert.assertFalse(eofToken.isEndTag());
        Assert.assertFalse(eofToken.isComment());
        Assert.assertFalse(eofToken.isCharacter());
    }
}
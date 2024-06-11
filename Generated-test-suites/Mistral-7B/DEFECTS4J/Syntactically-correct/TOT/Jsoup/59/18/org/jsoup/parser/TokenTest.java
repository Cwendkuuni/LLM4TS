import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.reset();

        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().setPubSysKey("public");
        doctypeToken.asDoctype().getPublicIdentifier().append("W3C");
        doctypeToken.asDoctype().getSystemIdentifier().append("/TR/REC-html401-20001219.dtd");
        doctypeToken.asDoctype().isForceQuirks(true);

        Assert.assertEquals("Doctype", doctypeToken.tokenType());
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
        Assert.assertNull(doctypeToken.asDoctype().getPubSysKey());
        Assert.assertEquals("W3C", doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertEquals("/TR/REC-html401-20001219.dtd", doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.reset();
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.reset();

        startTagToken.asStartTag().name("html");
        startTagToken.asStartTag().nameAttr("xmlns:xhtml", "http://www.w3.org/1999/xhtml");

        Assert.assertEquals("StartTag", startTagToken.tokenType());
        Assert.assertEquals("html", startTagToken.asStartTag().name());
        Assert.assertNotNull(startTagToken.asStartTag().getAttributes());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", startTagToken.asStartTag().getAttributes().getValue("xmlns:xhtml"));

        startTagToken.reset();
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.reset();

        endTagToken.asEndTag().name("html");

        Assert.assertEquals("EndTag", endTagToken.tokenType());
        Assert.assertEquals("html", endTagToken.asEndTag().name());

        endTagToken.reset();
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.reset();

        commentToken.asComment().getData().append("This is a comment");

        Assert.assertEquals("Comment", commentToken.tokenType());
        Assert.assertEquals("This is a comment", commentToken.asComment().getData());

        commentToken.reset();
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.reset();

        characterToken.asCharacter().data("&amp;");

        Assert.assertEquals("Character", characterToken.tokenType());
        Assert.assertEquals("&amp;", characterToken.asCharacter().getData());

        characterToken.reset();
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();
        invalidToken.reset();

        // This should throw an AssertionError as the token type is not defined
        Assert.assertEquals("InvalidToken", invalidToken.tokenType());
    }

    @Test
    public void testResetToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().setPubSysKey("public");
        doctypeToken.asDoctype().getPublicIdentifier().append("W3C");
        doctypeToken.asDoctype().getSystemIdentifier().append("/TR/REC-html401-20001219.dtd");
        doctypeToken.asDoctype().isForceQuirks(true);

        doctypeToken.reset();

        // All data should be reset
        Assert.assertEquals("Doctype", doctypeToken.tokenType());
        Assert.assertNull(doctypeToken.asDoctype().getName());
        Assert.assertNull(doctypeToken.asDoctype().getPubSysKey());
        Assert.assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        Assert.assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }
}
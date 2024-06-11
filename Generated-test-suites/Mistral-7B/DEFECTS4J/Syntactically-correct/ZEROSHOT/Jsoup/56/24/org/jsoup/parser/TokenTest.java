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
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        Assert.assertEquals(TokenType.StartTag, startTagToken.tokenType());

        startTagToken.asStartTag().name("html");

        Assert.assertEquals("html", startTagToken.asStartTag().name());
        Assert.assertEquals("html", startTagToken.asStartTag().name().toLowerCase());
        Assert.assertNull(startTagToken.asStartTag().attributes);

        startTagToken.asStartTag().newAttribute();
        startTagToken.asStartTag().appendAttributeName("class");
        startTagToken.asStartTag().appendAttributeValue("main-page");

        Assert.assertEquals("class", startTagToken.asStartTag().getPendingAttributeName());
        Assert.assertEquals("main-page", startTagToken.asStartTag().getPendingAttributeValue());
        Assert.assertNotNull(startTagToken.asStartTag().getAttributes());
        Assert.assertEquals("class", startTagToken.asStartTag().getAttributes().getClass().getSimpleName());
        Assert.assertEquals("main-page", startTagToken.asStartTag().getAttributes().get("class"));

        startTagToken.asStartTag().finaliseTag();

        Assert.assertNull(startTagToken.asStartTag().getPendingAttributeName());
        Assert.assertNull(startTagToken.asStartTag().getPendingAttributeValue());
        Assert.assertNull(startTagToken.asStartTag().getPendingAttributeValueS());
        Assert.assertFalse(startTagToken.asStartTag().hasEmptyAttributeValue());
        Assert.assertFalse(startTagToken.asStartTag().hasPendingAttributeValue());
        Assert.assertNull(startTagToken.asStartTag().attributes);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        Assert.assertEquals(TokenType.EndTag, endTagToken.tokenType());

        endTagToken.asEndTag().name("html");

        Assert.assertEquals("html", endTagToken.asEndTag().name());
        Assert.assertEquals("html", endTagToken.asEndTag().name().toLowerCase());

        endTagToken.asEndTag().finaliseTag();

        Assert.assertNull(endTagToken.asEndTag().getName());
        Assert.assertNull(endTagToken.asEndTag().getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        Assert.assertEquals(TokenType.Comment, commentToken.tokenType());

        commentToken.asComment().appendAttributeValue("This is a comment.");

        Assert.assertEquals("This is a comment.", commentToken.asComment().getData());
        Assert.assertEquals("<!--This is a comment.-->", commentToken.toString());

        commentToken.asComment().reset();

        Assert.assertNull(commentToken.getData());
        Assert.assertFalse(commentToken.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        Assert.assertEquals(TokenType.Character, characterToken.tokenType());

        characterToken.asCharacter().data("A");

        Assert.assertEquals('A', characterToken.getData().charAt(0));
        Assert.assertEquals("A", characterToken.toString());

        characterToken.asCharacter().reset();

        Assert.assertNull(characterToken.getData());
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
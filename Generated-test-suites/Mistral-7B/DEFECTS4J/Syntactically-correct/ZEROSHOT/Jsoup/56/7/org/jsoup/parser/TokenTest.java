import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctypeToken.type);

        StringBuilder name = doctypeToken.asDoctype().getName();
        Assert.assertEquals("Doctype", name.toString());

        doctypeToken.asDoctype().reset();
        Assert.assertFalse(name.toString().equals(doctypeToken.asDoctype().getName().toString()));

        doctypeToken.asDoctype().setName("newName");
        Assert.assertEquals("newName", doctypeToken.asDoctype().getName().toString());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, startTagToken.type);

        String tagName = startTagToken.asStartTag().getName();
        Assert.assertNull(tagName);

        startTagToken.asStartTag().name("testTag");
        Assert.assertEquals("testTag", startTagToken.asStartTag().getName().toString());

        startTagToken.asStartTag().reset();
        Assert.assertFalse(tagName.equals(startTagToken.asStartTag().getName().toString()));

        startTagToken.asStartTag().name("testTag");
        startTagToken.asStartTag().attributes().put("testAttribute", "testValue");
        Assert.assertEquals("testTag testAttribute=\"testValue\"", startTagToken.toString());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        Assert.assertEquals(TokenType.EndTag, endTagToken.type);

        String tagName = endTagToken.asEndTag().getName();
        Assert.assertNull(tagName);

        endTagToken.asEndTag().name("testTag");
        Assert.assertEquals("</testTag>", endTagToken.toString());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, commentToken.type);

        StringBuilder data = commentToken.asComment().getData();
        Assert.assertEquals("", data.toString());

        commentToken.asComment().getData("-- This is a comment --");
        Assert.assertEquals("This is a comment --", data.toString());

        commentToken.asComment().reset();
        Assert.assertFalse(data.toString().equals(commentToken.asComment().getData().toString()));
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Assert.assertEquals(TokenType.Character, characterToken.type);

        characterToken.asCharacter().data("test");
        Assert.assertEquals("test", characterToken.asCharacter().getData().toString());

        characterToken.asCharacter().reset();
        Assert.assertFalse(characterToken.asCharacter().getData().toString().equals(characterToken.asCharacter().getData().toString()));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidTokenType() {
        Token unknownToken = new Object();
        Validate.isFalse(unknownToken instanceof Token);
        Assert.assertFalse(unknownToken.type instanceof TokenType);
    }
}
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        doctypeToken.asDoctype().getName("html");
        assertEquals("html", doctypeToken.name());

        doctypeToken.asDoctype().getPublicIdentifier().append("public-id");
        assertEquals("public-id", doctypeToken.asDoctype().getPublicIdentifier().toString());

        doctypeToken.asDoctype().getSystemIdentifier().append("system-id");
        assertEquals("system-id", doctypeToken.asDoctype().getSystemIdentifier().toString());

        doctypeToken.asDoctype().isForceQuirks();
        Assert.assertFalse(doctypeToken.isForceQuirks());

        Token resetDoctype = doctypeToken.reset();
        assertSame(doctypeToken, resetDoctype);
    }

    @Test
    public void testTagTokenStart() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        startTagToken.asStartTag().nameAttr("html", null);
        assertEquals("html", startTagToken.name());

        Token resetStartTag = startTagToken.reset();
        assertSame(startTagToken, resetStartTag);
    }

    @Test
    public void testTagTokenEnd() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        endTagToken.asEndTag().nameAttr("html", null);
        assertEquals("html", endTagToken.name());

        Token resetEndTag = endTagToken.reset();
        assertSame(endTagToken, resetEndTag);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        commentToken.asComment().getData().append("This is a comment");
        assertEquals("This is a comment", commentToken.getData());

        Token resetComment = commentToken.reset();
        assertSame(commentToken, resetComment);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        characterToken.asCharacter().data("A");
        assertEquals("A", characterToken.getData());

        Token resetCharacter = characterToken.reset();
        assertSame(characterToken, resetCharacter);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType().toString());

        Token resetEOF = eofToken.reset();
        assertSame(eofToken, resetEOF);
    }

    @Test
    public void testTagTokenAttributes() {
        Token startTagToken = new Token.StartTag();
        Attributes attributes = new Attributes();

        startTagToken.asStartTag().nameAttr("html", attributes);
        startTagToken.asStartTag().appendAttributeName("class");
        startTagToken.asStartTag().appendAttributeValue("class-value");
        startTagToken.asStartTag().appendAttributeValue("class-value-2");

        Token resetStartTag = startTagToken.reset();
        assertNotSame(startTagToken, resetStartTag);

        Attribute classAttribute = startTagToken.asStartTag().getAttributes().get("class");
        assertEquals("class-value class-value-2", classAttribute.getValue());

        startTagToken.newAttribute();
        startTagToken.asStartTag().appendAttributeName("id");
        startTagToken.asStartTag().appendAttributeValue("id-value");

        startTagToken.newAttribute();
        startTagToken.asStartTag().appendAttributeName("title");
        startTagToken.asStartTag().appendAttributeValue("title-value");

        Token finaliseTag = startTagToken.finaliseTag();
        assertEquals("html class=\"class-value class-value-2\" id=\"id-value\" title=\"title-value\">", finaliseTag.toString());
    }
}
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        Token doctypeTokenWithName = new Token.Doctype();
        doctypeTokenWithName.asDoctype().name("html");
        assertEquals("Doctype", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
        assertNull(doctypeTokenWithName.asDoctype().getPublicIdentifier());
        assertNull(doctypeTokenWithName.asDoctype().getSystemIdentifier());
        assertFalse(doctypeTokenWithName.asDoctype().isForceQuirks());

        Token doctypeTokenWithNameAndPublicId = new Token.Doctype();
        doctypeTokenWithNameAndPublicId.asDoctype().name("html");
        doctypeTokenWithNameAndPublicId.asDoctype().publicIdentifier("_public");
        assertEquals("Doctype", doctypeTokenWithNameAndPublicId.tokenType());
        assertEquals("html", doctypeTokenWithNameAndPublicId.asDoctype().getName());
        assertEquals("_public", doctypeTokenWithNameAndPublicId.asDoctype().getPublicIdentifier());
        assertNull(doctypeTokenWithNameAndPublicId.asDoctype().getSystemIdentifier());
        assertFalse(doctypeTokenWithNameAndPublicId.asDoctype().isForceQuirks());

        Token doctypeTokenWithNamePublicIdAndSystemId = new Token.Doctype();
        doctypeTokenWithNamePublicIdAndSystemId.asDoctype().name("html");
        doctypeTokenWithNamePublicIdAndSystemId.asDoctype().publicIdentifier("_public");
        doctypeTokenWithNamePublicIdAndSystemId.asDoctype().systemIdentifier("_system");
        assertEquals("Doctype", doctypeTokenWithNamePublicIdAndSystemId.tokenType());
        assertEquals("html", doctypeTokenWithNamePublicIdAndSystemId.asDoctype().getName());
        assertEquals("_public", doctypeTokenWithNamePublicIdAndSystemId.asDoctype().getPublicIdentifier());
        assertEquals("_system", doctypeTokenWithNamePublicIdAndSystemId.asDoctype().getSystemIdentifier());
        assertFalse(doctypeTokenWithNamePublicIdAndSystemId.asDoctype().isForceQuirks());

        Token doctypeTokenWithForceQuirks = new Token.Doctype();
        doctypeTokenWithForceQuirks.asDoctype().name("html");
        doctypeTokenWithForceQuirks.asDoctype().forceQuirks(true);
        assertEquals("Doctype", doctypeTokenWithForceQuirks.tokenType());
        assertEquals("html", doctypeTokenWithForceQuirks.asDoctype().getName());
        assertNull(doctypeTokenWithForceQuirks.asDoctype().getPublicIdentifier());
        assertNull(doctypeTokenWithForceQuirks.asDoctype().getSystemIdentifier());
        assertTrue(doctypeTokenWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());
        assertNull(startTagToken.asStartTag().getName());
        assertNull(startTagToken.asStartTag().getAttributes());
        assertFalse(startTagToken.asStartTag().isSelfClosing());

        Token startTagTokenWithName = new Token.StartTag();
        startTagTokenWithName.asStartTag().name("div");
        assertEquals("StartTag", startTagTokenWithName.tokenType());
        assertEquals("div", startTagTokenWithName.asStartTag().getName());
        assertNull(startTagTokenWithName.asStartTag().getAttributes());
        assertFalse(startTagTokenWithName.asStartTag().isSelfClosing());

        Token startTagTokenWithNameAndAttributes = new Token.StartTag();
        startTagTokenWithNameAndAttributes.asStartTag().name("div").attributes(new Token.Attributes());
        assertEquals("StartTag", startTagTokenWithNameAndAttributes.tokenType());
        assertEquals("div", startTagTokenWithNameAndAttributes.asStartTag().getName());
        assertNotNull(startTagTokenWithNameAndAttributes.asStartTag().getAttributes());
        assertFalse(startTagTokenWithNameAndAttributes.asStartTag().isSelfClosing());

        Token startTagTokenWithNameAttributesAndSelfClosing = new Token.StartTag();
        startTagTokenWithNameAttributesAndSelfClosing.asStartTag().name("img").selfClosing(true).attributes(new Token.Attributes());
        assertEquals("StartTag", startTagTokenWithNameAttributesAndSelfClosing.tokenType());
        assertEquals("img", startTagTokenWithNameAttributesAndSelfClosing.asStartTag().getName());
        assertNotNull(startTagTokenWithNameAttributesAndSelfClosing.asStartTag().getAttributes());
        assertTrue(startTagTokenWithNameAttributesAndSelfClosing.asStartTag().isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());
        assertNull(endTagToken.asEndTag().getName());

        Token endTagTokenWithName = new Token.EndTag();
        endTagTokenWithName.asEndTag().name("div");
        assertEquals("EndTag", endTagTokenWithName.tokenType());
        assertEquals("div", endTagTokenWithName.asEndTag().getName());

        Token endTagTokenWithNameAndAttributes = new Token.EndTag();
        endTagTokenWithNameAndAttributes.asEndTag().name("div").attributes(new Token.Attributes());
        assertEquals("EndTag", endTagTokenWithNameAndAttributes.tokenType());
        assertEquals("div", endTagTokenWithNameAndAttributes.asEndTag().getName());
        assertNotNull(endTagTokenWithNameAndAttributes.asEndTag().getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());
        assertNotNull(commentToken.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character('A');
        assertEquals("Character", characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());

        Token characterTokenWithMultiChar = new Token.Character("ABC");
        assertEquals("Character", characterTokenWithMultiChar.tokenType());
        assertEquals("ABC", characterTokenWithMultiChar.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());
    }

    @Test(expected = AssertionError.class)
    public void testTokenTypeMismatch() {
        Token doctypeToken = new Token.StartTag();
        doctypeToken.asDoctype();
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeGettersAfterConstruction() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().name("html");
        doctypeToken.asDoctype().publicIdentifier("_public");
        doctypeToken.asDoctype().systemIdentifier("_system");
        doctypeToken.asDoctype().forceQuirks(true);
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testStartTagGettersAfterConstruction() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("div");
        startTagToken.asStartTag().selfClosing(true);
        startTagToken.asStartTag().attributes(new Token.Attributes());
        startTagToken.asStartTag().appendAttributeName("class").appendAttributeValue("test");
        assertNull(startTagToken.getName());
        assertNull(startTagToken.getAttributes());
        assertFalse(startTagToken.isSelfClosing());
    }
}
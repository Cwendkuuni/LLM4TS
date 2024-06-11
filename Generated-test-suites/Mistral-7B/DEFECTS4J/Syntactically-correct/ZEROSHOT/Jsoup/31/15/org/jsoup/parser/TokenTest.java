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
    public void testDoctypeCreation() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.asDoctype().name("html");
        doctypeWithPublicIdentifier.asDoctype().publicIdentifier().append("ISO-8859-1");
        assertEquals("Doctype", doctypeWithPublicIdentifier.tokenType());
        assertEquals("html", doctypeWithPublicIdentifier.asDoctype().getName());
        assertEquals("ISO-8859-1", doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        assertFalse(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());
        assertNull(doctype.asDoctype());
        assertNull(doctype.asStartTag());
        assertNull(doctype.asEndTag());
        assertNull(doctype.asComment());
        assertNull(doctype.asCharacter());
        assertNull(doctype.asEOF());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name("html");
        assertTrue(doctypeWithName.isDoctype());
        assertSame(doctypeWithName, doctypeWithName.asDoctype());
        assertNull(doctypeWithName.asStartTag());
        assertNull(doctypeWithName.asEndTag());
        assertNull(doctypeWithName.asComment());
        assertNull(doctypeWithName.asCharacter());
        assertNull(doctypeWithName.asEOF());

        Token doctypeWithNamePublicIdentifier = new Token.Doctype();
        doctypeWithNamePublicIdentifier.asDoctype().name("html");
        doctypeWithNamePublicIdentifier.asDoctype().publicIdentifier().append("ISO-8859-1");
        assertTrue(doctypeWithNamePublicIdentifier.isDoctype());
        assertSame(doctypeWithNamePublicIdentifier, doctypeWithNamePublicIdentifier.asDoctype());
        assertNull(doctypeWithNamePublicIdentifier.asStartTag());
        assertNull(doctypeWithNamePublicIdentifier.asEndTag());
        assertNull(doctypeWithNamePublicIdentifier.asComment());
        assertNull(doctypeWithNamePublicIdentifier.asCharacter());
        assertNull(doctypeWithNamePublicIdentifier.asEOF());
        assertEquals("ISO-8859-1", doctypeWithNamePublicIdentifier.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Token doctype = new Token.Doctype();
        assertFalse(doctype.asDoctype().isForceQuirks());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().name("html");
        doctypeWithForceQuirks.asDoctype().forceQuirks(true);
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagCreation() {
        Token tag = new Token.StartTag();
        assertEquals(TokenType.StartTag, tag.type);
        assertEquals("StartTag", tag.tokenType());

        Token tagWithName = new Token.StartTag();
        tagWithName.asStartTag().name("div");
        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tagWithName.asStartTag().name());

        Token tagWithNameAndAttribute = new Token.StartTag();
        tagWithNameAndAttribute.asStartTag().name("div").appendAttributeName("class").appendAttributeValue("test");
        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tagWithNameAndAttribute.asStartTag().name());
        assertEquals("test", tagWithNameAndAttribute.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testTagGetters() {
        Token tag = new Token.StartTag();
        assertFalse(tag.isDoctype());
        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isEOF());
        assertNull(tag.asDoctype());
        assertNull(tag.asStartTag());
        assertNull(tag.asEndTag());
        assertNull(tag.asComment());
        assertNull(tag.asCharacter());
        assertNull(tag.asEOF());

        Token tagWithName = new Token.StartTag();
        tagWithName.asStartTag().name("div");
        assertFalse(tag.isDoctype());
        assertSame(tagWithName, tagWithName.asStartTag());
        assertNull(tagWithName.asDoctype());
        assertNull(tagWithName.asEndTag());
        assertNull(tagWithName.asComment());
        assertNull(tagWithName.asCharacter());
        assertNull(tagWithName.asEOF());
        assertEquals("div", tagWithName.asStartTag().name());

        Token tagWithNameAndAttribute = new Token.StartTag();
        tagWithNameAndAttribute.asStartTag().name("div").appendAttributeName("class").appendAttributeValue("test");
        assertFalse(tag.isDoctype());
        assertSame(tagWithNameAndAttribute, tagWithNameAndAttribute.asStartTag());
        assertNull(tagWithNameAndAttribute.asDoctype());
        assertNull(tagWithNameAndAttribute.asEndTag());
        assertNull(tagWithNameAndAttribute.asComment());
        assertNull(tagWithNameAndAttribute.asCharacter());
        assertNull(tagWithNameAndAttribute.asEOF());
        assertEquals("div", tagWithNameAndAttribute.asStartTag().name());
        assertEquals("test", tagWithNameAndAttribute.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testTagSelfClosing() {
        Token tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());

        Token tagSelfClosing = new Token.StartTag();
        tagSelfClosing.asStartTag().name("img").selfClosing(true);
        assertTrue(tagSelfClosing.isSelfClosing());

        Token tagSelfClosingWithName = new Token.StartTag();
        tagSelfClosingWithName.asStartTag().name("img").selfClosing(true);
        assertTrue(tagSelfClosingWithName.isSelfClosing());
        assertEquals("img", tagSelfClosingWithName.asStartTag().name());
    }

    @Test
    public void testTagToString() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        tag.asStartTag().appendAttributeName("class").appendAttributeValue("test");
        assertEquals("<div class=\"test\">", tag.toString());
    }

    @Test
    public void testEndTagCreation() {
        Token tag = new Token.EndTag();
        assertEquals(TokenType.EndTag, tag.type);
        assertEquals("EndTag", tag.tokenType());

        Token tagWithName = new Token.EndTag();
        tagWithName.asEndTag().name("div");
        assertEquals("EndTag", tag.tokenType());
        assertEquals("div", tagWithName.asEndTag().name());
    }

    @Test
    public void testEndTagGetters() {
        Token tag = new Token.EndTag();
        assertFalse(tag.isDoctype());
        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isEOF());
        assertNull(tag.asDoctype());
        assertNull(tag.asStartTag());
        assertSame(tag, tag.asEndTag());
        assertNull(tag.asComment());
        assertNull(tag.asCharacter());
        assertNull(tag.asEOF());

        Token tagWithName = new Token.EndTag();
        tagWithName.asEndTag().name("div");
        assertFalse(tag.isDoctype());
        assertSame(tagWithName, tagWithName.asEndTag());
        assertNull(tagWithName.asDoctype());
        assertNull(tagWithName.asStartTag());
        assertSame(tagWithName, tagWithName.asEndTag());
        assertNull(tagWithName.asComment());
        assertNull(tagWithName.asCharacter());
        assertNull(tagWithName.asEOF());
        assertEquals("div", tagWithName.asEndTag().name());
    }

    @Test
    public void testCommentCreation() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment");
        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment", commentWithData.asComment().getData().toString());
    }

    @Test
    public void testCommentGetters() {
        Token comment = new Token.Comment();
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());
        assertNull(comment.asDoctype());
        assertNull(comment.asStartTag());
        assertNull(comment.asEndTag());
        assertSame(comment, comment.asComment());
        assertNull(comment.asCharacter());
        assertNull(comment.asEOF());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment");
        assertFalse(comment.isDoctype());
        assertSame(commentWithData, commentWithData.asComment());
        assertNull(commentWithData.asDoctype());
        assertNull(commentWithData.asStartTag());
        assertNull(commentWithData.asEndTag());
        assertSame(commentWithData, commentWithData.asComment());
        assertNull(commentWithData.asCharacter());
        assertNull(commentWithData.asEOF());
        assertEquals("This is a comment", commentWithData.asComment().getData().toString());
    }

    @Test
    public void testCharacterCreation() {
        Token character = new Token.Character("Hello World!");
        assertEquals(TokenType.Character, character.type);
        assertEquals("Character", character.tokenType());

        Token characterWithData = new Token.Character("Hello World!");
        assertEquals("Hello World!", characterWithData.getData());
    }

    @Test
    public void testCharacterGetters() {
        Token character = new Token.Character("Hello World!");
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isCharacter());
        assertFalse(character.isEOF());
        assertNull(character.asDoctype());
        assertNull(character.asStartTag());
        assertNull(character.asEndTag());
        assertNull(character.asComment());
        assertSame(character, character.asCharacter());
        assertNull(character.asEOF());
        assertEquals("Hello World!", character.getData());

        Token characterWithData = new Token.Character("Hello World!");
        assertEquals("Hello World!", characterWithData.getData());
        assertFalse(characterWithData.isDoctype());
        assertNull(characterWithData.asDoctype());
        assertNull(characterWithData.asStartTag());
        assertNull(characterWithData.asEndTag());
        assertNull(characterWithData.asComment());
        assertSame(characterWithData, characterWithData.asCharacter());
        assertNull(characterWithData.asEOF());
    }

    @Test
    public void testEofCreation() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
        assertEquals("EOF", eof.tokenType());

        Token eofWithName = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertSame(eof, eof.asEOF());
        assertNull(eof.asDoctype());
        assertNull(eof.asStartTag());
        assertNull(eof.asEndTag());
        assertNull(eof.asComment());
        assertNull(eof.asCharacter());
    }

    @Test
    public void testEofGetters() {
        Token eof = new Token.EOF();
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isEOF());
        assertNull(eof.asDoctype());
        assertNull(eof.asStartTag());
        assertNull(eof.asEndTag());
        assertNull(eof.asComment());
        assertSame(eof, eof.asEOF());
        assertNull(eof.asCharacter());
    }
}
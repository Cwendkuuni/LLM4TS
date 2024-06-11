import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.Attribute;
import org.jsoup.parser.Attributes;
import org.jsoup.parser.BooleanAttribute;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public identifier");
        doctype.systemIdentifier.append("system identifier");
        doctype.forceQuirks = true;

        Token expectedToken = new Doctype();
        expectedToken.asDoctype().getName().equals("html");
        expectedToken.asDoctype().getPublicIdentifier().equals("public identifier");
        expectedToken.asDoctype().getSystemIdentifier().equals("system identifier");
        expectedToken.asDoctype().isForceQuirks().equals(true);

        Assertions.assertEquals(expectedToken, doctype);
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("html");

        Token expectedToken = new StartTag();
        expectedToken.asStartTag().getName().equals("html");

        Assertions.assertEquals(expectedToken, startTag);
    }

    @Test
    public void testStartTagWithAttributes() {
        StartTag startTag = new StartTag();
        startTag.name("p").attributes(new Attributes().put("class", "example"));

        Token expectedToken = new StartTag();
        expectedToken.asStartTag().getName().equals("p");
        expectedToken.asStartTag().getAttributes().toString().equals("class=\"example\"");

        Assertions.assertEquals(expectedToken, startTag);
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("p");

        Token expectedToken = new EndTag();
        expectedToken.asEndTag().getName().equals("p");

        Assertions.assertEquals(expectedToken, endTag);
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.data.append("This is a comment.");

        Token expectedToken = new Comment();
        expectedToken.asComment().getData().equals("This is a comment.");

        Assertions.assertEquals(expectedToken, comment);
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("&amp;");

        Token expectedToken = new Character();
        expectedToken.asCharacter().getData().equals("&amp;");

        Assertions.assertEquals(expectedToken, character);
    }

    @Test
    public void testResetDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public identifier");
        doctype.systemIdentifier.append("system identifier");
        doctype.forceQuirks = true;

        doctype.reset();

        Assertions.assertFalse(doctype.asDoctype().getName().hasValue());
        Assertions.assertFalse(doctype.asDoctype().getPublicIdentifier().hasValue());
        Assertions.assertFalse(doctype.asDoctype().getSystemIdentifier().hasValue());
        Assertions.assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testResetStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("html");

        startTag.reset();

        Assertions.assertNull(startTag.tagName);
        Assertions.assertNull(startTag.normalName);
        Assertions.assertNull(startTag.pendingAttributeName);
        Assertions.assertNull(startTag.pendingAttributeValue);
        Assertions.assertNull(startTag.pendingAttributeValueS);
        Assertions.assertFalse(startTag.hasEmptyAttributeValue);
        Assertions.assertFalse(startTag.hasPendingAttributeValue);
        Assertions.assertFalse(startTag.selfClosing);
        Assertions.assertNull(startTag.attributes);
    }

    @Test
    public void testResetEndTagToken() {
        EndTag endTag = new EndTag();

        endTag.reset();

        Assertions.assertNull(endTag.tagName);
        Assertions.assertNull(endTag.normalName);
        Assertions.assertNull(endTag.pendingAttributeName);
        Assertions.assertNull(endTag.pendingAttributeValue);
        Assertions.assertNull(endTag.pendingAttributeValueS);
        Assertions.assertFalse(endTag.hasEmptyAttributeValue);
        Assertions.assertFalse(endTag.hasPendingAttributeValue);
        Assertions.assertFalse(endTag.selfClosing);
        Assertions.assertNull(endTag.attributes);
    }

    @Test
    public void testResetCommentToken() {
        Comment comment = new Comment();
        comment.data.append("This is a comment.");

        comment.reset();

        Assertions.assertFalse(comment.getData().hasValue());
        Assertions.assertFalse(comment.bogus);
    }

    @Test
    public void testResetCharacterToken() {
        Character character = new Character();
        character.data("&amp;");

        character.reset();

        Assertions.assertNull(character.data);
    }
}
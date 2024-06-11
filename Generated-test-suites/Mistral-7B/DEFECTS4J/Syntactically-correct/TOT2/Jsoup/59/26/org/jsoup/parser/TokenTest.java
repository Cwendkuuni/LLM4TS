import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testTokenTypeDoctype() {
        Doctype doctype = new Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testResetDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;
        Doctype resetDoctype = doctype.reset();
        Assert.assertNull(doctype.name.toString());
        Assert.assertNull(doctype.pubSysKey);
        Assert.assertNull(doctype.publicIdentifier.toString());
        Assert.assertFalse(doctype.forceQuirks);
        Assert.assertEquals(doctype, resetDoctype);
    }

    @Test
    public void testGetNameDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testGetPubSysKeyDoctype() {
        Doctype doctype = new Doctype();
        doctype.pubSysKey = "pubSysKey";
        Assert.assertEquals("pubSysKey", doctype.getPubSysKey());
    }

    @Test
    public void testGetPublicIdentifierDoctype() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier.append("publicId");
        Assert.assertEquals("publicId", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifierDoctype() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("systemId");
        Assert.assertEquals("systemId", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirksDoctype() {
        Doctype doctype = new Doctype();
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        Assert.assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTokenTypeStartTag() {
        StartTag startTag = new StartTag();
        Assert.assertEquals(TokenType.StartTag, startTag.type);
    }

    // Add test cases for other methods, edge cases, and error scenarios here

    @Test
    public void testResetTag() {
        Tag tag = new StartTag();
        tag.tagName = "tagName";
        tag.normalName = "tagName";
        tag.pendingAttributeName = "attrName";
        tag.pendingAttributeValue.append("attrValue");
        tag.pendingAttributeValueS = "attrValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        Tag resetTag = tag.reset();

        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertNull(tag.pendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);

        Assert.assertEquals(tag, resetTag);
    }

    @Test
    public void testFinaliseTag() {
        Tag tag = new StartTag();
        tag.tagName = "tagName";
        tag.normalName = "tagName";
        tag.pendingAttributeName = "attrName";
        tag.newAttribute();
        tag.finaliseTag();

        Assert.assertNotNull(tag.attributes);
        Assert.assertFalse(tag.pendingAttributeName == null || tag.pendingAttributeName.length() == 0);
    }

    // Add other test cases for various Token subclasses and their methods here

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        Assert.assertTrue(token.isDoctype());
        Token doctypeToken = (Doctype) token;

        token = new StartTag();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        StartTag startTagToken = (StartTag) token;

        token = new EndTag();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertTrue(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        EndTag endTagToken = (EndTag) token;

        token = new Comment();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertTrue(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Comment commentToken = (Comment) token;

        token = new Character();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertTrue(token.isCharacter());
        Character characterToken = (Character) token;

        token = new EOF();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Assert.assertTrue(token.isEOF());
    }
}
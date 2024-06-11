import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public id");
        doctype.systemIdentifier.append("system id");
        doctype.forceQuirks = true;

        // Test getters
        Assert.assertEquals("Doctype", doctype.tokenType());
        Assert.assertEquals("html", doctype.getName());
        Assert.assertEquals("public id", doctype.getPublicIdentifier());
        Assert.assertEquals("system id", doctype.getSystemIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());

        // Test reset()
        Doctype resetDoctype = doctype.reset();
        Assert.assertNotSame(doctype, resetDoctype);
        Assert.assertEquals("Doctype", resetDoctype.tokenType());
        Assert.assertNull(resetDoctype.name);
        Assert.assertNull(resetDoctype.publicIdentifier);
        Assert.assertNull(resetDoctype.systemIdentifier);
        Assert.assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.name("html");

        // Test getters
        Assert.assertEquals("StartTag", startTag.tokenType());
        Assert.assertEquals("html", startTag.name());
        Assert.assertNull(startTag.normalName());
        Assert.assertNull(startTag.getAttributes());
        Assert.assertFalse(startTag.isSelfClosing());

        // Test newAttribute()
        startTag.newAttribute().name("class").value("test");
        Attributes attributes = startTag.getAttributes();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("class", attributes.keySet().iterator().next());
        Assert.assertEquals("test", attributes.get("class").toString());

        // Test finaliseTag()
        startTag.finaliseTag();
        Assert.assertEquals(1, startTag.getAttributes().size());

        // Test reset()
        StartTag resetStartTag = startTag.reset();
        Assert.assertNotSame(startTag, resetStartTag);
        Assert.assertEquals("StartTag", resetStartTag.tokenType());
        Assert.assertNull(resetStartTag.name());
        Assert.assertNull(resetStartTag.normalName());
        Assert.assertNull(resetStartTag.getAttributes());
        Assert.assertFalse(resetStartTag.isSelfClosing());
    }
}
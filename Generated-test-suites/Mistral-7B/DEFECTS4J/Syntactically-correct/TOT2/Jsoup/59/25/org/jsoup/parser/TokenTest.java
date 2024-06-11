import org.junit.Test;
import org.junit.Assert.*;
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
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.pubSysKey("DTD HTML 4.01 Transitional");
        doctype.publicIdentifier("HTML_4_Transitional");
        doctype.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks(true);

        Doctype resetDoctype = doctype.reset();
        Assert.assertEquals("html", resetDoctype.getName());
        Assert.assertNull(resetDoctype.getPubSysKey());
        Assert.assertNull(resetDoctype.getPublicIdentifier());
        Assert.assertNull(resetDoctype.getSystemIdentifier());
        Assert.assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.name("html");
        Attributes attributes = new Attributes();
        attributes.put("lang", "en");
        startTag.attributes(attributes);

        Tag resetStartTag = startTag.reset();
        Assert.assertEquals("html", resetStartTag.name());
        Assert.assertNull(resetStartTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        endTag.name("html");

        Tag resetEndTag = endTag.reset();
        Assert.assertEquals("html", resetEndTag.name());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.data("This is a comment");

        Comment resetComment = comment.reset();
        Assert.assertEquals("<!--This is a comment-->", resetComment.toString());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        character.data("a");

        Character resetCharacter = character.reset();
        Assert.assertEquals("a", resetCharacter.getData());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();

        EOF resetEOF = eof.reset();
        // EOF token doesn't have any data or properties to reset
        Assert.assertNull(resetEOF);
    }

    @Test
    public void testResetAllTokens() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        // Test Doctype reset
        Doctype resetDoctype = doctype.reset();
        Assert.assertEquals("Doctype", resetDoctype.tokenType());

        // Test StartTag reset
        StartTag resetStartTag = startTag.reset();
        Assert.assertEquals("StartTag", resetStartTag.tokenType().name());

        // Test EndTag reset
        EndTag resetEndTag = endTag.reset();
        Assert.assertEquals("EndTag", resetEndTag.tokenType().name());

        // Test Comment reset
        Comment resetComment = comment.reset();
        Assert.assertEquals("Comment", resetComment.tokenType().name());

        // Test Character reset
        Character resetCharacter = character.reset();
        Assert.assertEquals("Character", resetCharacter.tokenType().name());

        // Test EOF reset
        EOF resetEOF = eof.reset();
        Assert.assertEquals("EOF", resetEOF.tokenType().name());
    }

    @Test
    public void testEdgeCases() {
        Doctype doctype = new Doctype();
        Doctype resetDoctype = doctype.reset();

        doctype.name("HTML");
        doctype.pubSysKey("DTD HTML 4.01 Transitional");
        doctype.publicIdentifier("HTML_4_Transitional");
        doctype.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks(true);

        // Test null values
        Doctype nullNameDoctype = new Doctype();
        Doctype nullPubSysKeyDoctype = new Doctype();
        Doctype nullPublicIdentifierDoctype = new Doctype();
        Doctype nullSystemIdentifierDoctype = new Doctype();
        Doctype nullForceQuirksDoctype = new Doctype();

        nullNameDoctype.name(null);
        nullPubSysKeyDoctype.pubSysKey(null);
        nullPublicIdentifierDoctype.publicIdentifier(null);
        nullSystemIdentifierDoctype.systemIdentifier(null);
        nullForceQuirksDoctype.forceQuirks(null);

        // Test empty strings
        Doctype emptyNameDoctype = new Doctype();
        Doctype emptyPubSysKeyDoctype = new Doctype();
        Doctype emptyPublicIdentifierDoctype = new Doctype();
        Doctype emptySystemIdentifierDoctype = new Doctype();
        Doctype emptyForceQuirksDoctype = new Doctype();

        emptyNameDoctype.name("");
        emptyPubSysKeyDoctype.pubSysKey("");
        emptyPublicIdentifierDoctype.publicIdentifier("");
        emptySystemIdentifierDoctype.systemIdentifier("");
        emptyForceQuirksDoctype.forceQuirks("");

        // Test resetting edge cases
        Doctype resetNullNameDoctype = nullNameDoctype.reset();
        Doctype resetEmptyNameDoctype = emptyNameDoctype.reset();
        Doctype resetNullPubSysKeyDoctype = nullPubSysKeyDoctype.reset();
        Doctype resetEmptyPubSysKeyDoctype = emptyPubSysKeyDoctype.reset();
        Doctype resetNullPublicIdentifierDoctype = nullPublicIdentifierDoctype.reset();
        Doctype resetEmptyPublicIdentifierDoctype = emptyPublicIdentifierDoctype.reset();
        Doctype resetNullSystemIdentifierDoctype = nullSystemIdentifierDoctype.reset();
        Doctype resetEmptySystemIdentifierDoctype = emptySystemIdentifierDoctype.reset();
        Doctype resetNullForceQuirksDoctype = nullForceQuirksDoctype.reset();
        Doctype resetEmptyForceQuirksDoctype = emptyForceQuirksDoctype.reset();

        Assert.assertEquals("Doctype", resetDoctype.tokenType());
        Assert.assertEquals("Doctype", resetNullNameDoctype.tokenType());
        Assert.assertEquals("Doctype", resetEmptyNameDoctype.tokenType());
        Assert.assertNull(resetNullPubSysKeyDoctype);
        Assert.assertNull(resetEmptyPubSysKeyDoctype);
        Assert.assertNull(resetNullPublicIdentifierDoctype);
        Assert.assertNull(resetEmptyPublicIdentifierDoctype);
        Assert.assertNull(resetNullSystemIdentifierDoctype);
        Assert.assertNull(resetEmptySystemIdentifierDoctype);
        Assert.assertFalse(resetNullForceQuirksDoctype.isForceQuirks());
        Assert.assertFalse(resetEmptyForceQuirksDoctype.isForceQuirks());
    }

    @Test
    public void testExceptionHandling() {
        Doctype doctype = new Doctype();

        try {
            doctype.name(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }

        try {
            doctype.name("&lt;script&gt;");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}
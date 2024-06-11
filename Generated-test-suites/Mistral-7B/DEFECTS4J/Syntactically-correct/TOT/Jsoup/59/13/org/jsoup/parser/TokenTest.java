import org.jsoup.parser.Attribute;
import org.jsoup.parser.Attributes;
import org.jsoup.parser.BooleanAttribute;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private Doctype doctype;
    private StartTag startTag;
    private Token character;
    private Token eof;

    @Before
    public void setUp() {
        doctype = new Doctype();
        startTag = new StartTag();
        character = new Character();
        eof = new EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.getName().equals("doctype");
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetName() {
        doctype.getName("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypePubSysKey() {
        doctype.setPubSysKey("pubsyskey");
        assertEquals("pubsyskey", doctype.getPubSysKey());
        doctype.reset();
        assertNull(doctype.getPubSysKey());
    }

    @Test
    public void testDoctypePublicIdentifier() {
        doctype.setPublicIdentifier("publicidentifier");
        assertEquals("publicidentifier", doctype.getPublicIdentifier());
        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifier() {
        doctype.setSystemIdentifier("systemidentifier");
        assertEquals("systemidentifier", doctype.getSystemIdentifier());
        doctype.reset();
        assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeForceQuirks() {
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        startTag.name("tag");
        assertSame("tag", startTag.name());
        startTag.reset();
        assertNull(startTag.name());
    }

    @Test
    public void testTagName() {
        startTag.name("Tag");
        assertEquals("Tag", startTag.name());
        startTag.name("tag");
        assertEquals("tag", startTag.name());
    }

    @Test
    public void testTagNormalName() {
        startTag.name("Tag").normalName("tag");
        assertEquals("tag", startTag.normalName());
    }

    @Test
    public void testTagSelfClosing() {
        startTag.selfClosing(true);
        assertTrue(startTag.isSelfClosing());
        startTag.reset();
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testTagAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attr", "value"));
        startTag.attributes(attributes);
        assertTrue(startTag.hasAttributes());
        assertEquals(1, startTag.attributes().size());
        assertEquals("value", startTag.attributes().get("attr").getValue());

        startTag.newAttribute();
        assertFalse(startTag.hasAttributes());
    }

    @Test
    public void testTokenType() {
        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals(TokenType.Character, character.type);
        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testCharacterData() {
        character.data("data");
        assertEquals("data", character.getData());
    }

    @Test
    public void testEOF() {
        assertEquals(TokenType.EOF, eof.type);
    }
}
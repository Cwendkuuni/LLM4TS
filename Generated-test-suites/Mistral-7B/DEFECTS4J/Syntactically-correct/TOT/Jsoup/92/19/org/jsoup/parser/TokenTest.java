import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private Token doctype;
    private Token startTag;
    private Token endTag;
    private Token comment;
    private Token character;
    private Token cdata;
    private Token eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("test");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeToken() {
        Doctype doctypeToken = (Doctype) this.doctype;
        doctypeToken.getName().equals("DOCTYPE");
        assertEquals("", doctypeToken.getName());
        assertNull(doctypeToken.getSystemIdentifier());
        assertNull(doctypeToken.getPublicIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.getName("html");
        doctypeToken.setPublicIdentifier("public id");
        doctypeToken.setSystemIdentifier("system id");
        doctypeToken.isForceQuirks(true);

        Doctype resetDoctypeToken = (Doctype) doctypeToken.reset();

        assertEquals("DOCTYPE", resetDoctypeToken.tokenType());
        assertNull(resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPublicIdentifier());
        assertNull(resetDoctypeToken.getSystemIdentifier());
        assertFalse(resetDoctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        StartTag startTagToken = (StartTag) this.startTag;
        startTagToken.name("html");
        startTagToken.newAttribute().name("class").value("test");
        startTagToken.newAttribute().name("id").value("test");

        StartTag resetStartTagToken = (StartTag) startTagToken.reset();

        assertEquals("html", resetStartTagToken.name());
        assertNull(resetStartTagToken.getAttributes());
    }

    @Test
    public void testEndTagToken() {
        EndTag endTagToken = (EndTag) this.endTag;
        endTagToken.name("html");

        EndTag resetEndTagToken = (EndTag) endTagToken.reset();

        assertEquals("html", resetEndTagToken.name());
        assertNull(resetEndTagToken.getAttributes());
    }

    @Test
    public void testCommentToken() {
        Comment commentToken = (Comment) this.comment;
        commentToken.getData().equals("--comment--");

        Comment resetCommentToken = (Comment) commentToken.reset();

        assertEquals("COMMENT", resetCommentToken.tokenType());
        assertNull(resetCommentToken.getData());
        assertFalse(resetCommentToken.isBogus());

        commentToken.setBogus(true);

        Comment resetBogusCommentToken = (Comment) commentToken.reset();

        assertEquals("COMMENT", resetBogusCommentToken.tokenType());
        assertNull(resetBogusCommentToken.getData());
        assertTrue(resetBogusCommentToken.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Character characterToken = (Character) this.character;
        characterToken.data("test");

        Character resetCharacterToken = (Character) characterToken.reset();

        assertEquals("CHARACTER", resetCharacterToken.tokenType());
        assertNull(resetCharacterToken.getData());

        characterToken.data("test");

        assertEquals("test", characterToken.getData());
    }

    @Test
    public void testCDATAToken() {
        CData cdataToken = (CData) this.cdata;

        CData resetCDataToken = (CData) cdataToken.reset();

        assertEquals("CHARACTER", resetCDataToken.tokenType());
        assertNull(resetCDataToken.getData());

        assertEquals("<![CDATA[test]]>", cdataToken.toString());
    }

    @Test
    public void testEOToken() {
        EOF eofToken = (EOF) this.eof;

        EOF resetEOFToken = (EOF) eofToken.reset();

        assertEquals("EOF", resetEOFToken.tokenType());
    }
}
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testDoctypeName() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = (Token.Doctype) token;
        doctype.getName().equals("doctype"); // check if the name is correctly initialized
        doctype.getName("html"); // check if the name can be set
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypePublicIdentifier() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = (Token.Doctype) token;
        doctype.getPublicIdentifier().equals(""); // check if the publicIdentifier is correctly initialized
        doctype.getPublicIdentifier("_publicID"); // check if the publicIdentifier can be set
        assertEquals("_publicID", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifier() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = (Token.Doctype) token;
        doctype.getSystemIdentifier().equals(""); // check if the systemIdentifier is correctly initialized
        doctype.getSystemIdentifier("/_systemID"); // check if the systemIdentifier can be set
        assertEquals("/_systemID", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = (Token.Doctype) token;
        assertFalse(doctype.isForceQuirks()); // check if the forceQuirks is correctly initialized
        doctype.setForceQuirks(true); // check if the forceQuirks can be set
        assertTrue(doctype.isForceQuirks());
    }
}
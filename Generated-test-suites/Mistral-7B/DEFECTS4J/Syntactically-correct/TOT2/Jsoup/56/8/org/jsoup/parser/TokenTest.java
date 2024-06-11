import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Doctype;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public id");
        doctype.systemIdentifier.append("system id");
        doctype.forceQuirks = true;

        doctype.reset();

        // Check that all fields have been reset
        assertTrue(doctype.name.length() == 0);
        assertTrue(doctype.publicIdentifier.length() == 0);
        assertTrue(doctype.systemIdentifier.length() == 0);
        assertFalse(doctype.forceQuirks);
    }
}
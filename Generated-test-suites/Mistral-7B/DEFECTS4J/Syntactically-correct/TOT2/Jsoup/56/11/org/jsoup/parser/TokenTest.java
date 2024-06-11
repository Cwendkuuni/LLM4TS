import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Doctype;

public class TokenTest {

    // 

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("public id");
        doctype.setSystemIdentifier("system id");
        doctype.setForceQuirks(true);

        Doctype resetDoctype = doctype.reset();

        assertSame(doctype.getClass(), resetDoctype.getClass());
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    // 
}
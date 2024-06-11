import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    @Test
    public void testDoctypeCreation() {
        Token doctype = new Token.Doctype();
        String expectedType = Token.TokenType.Doctype.toString();
        String actualType = doctype.tokenType();
        assert(expectedType.equals(actualType));
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getPublicIdentifier().append("XHTML");
        doctype.setSystemIdentifier("/index.html");
        doctype.setForceQuirks(true);

        String expectedName = "html";
        String actualName = doctype.getName().toString();
        assert(expectedName.equals(actualName));

        String expectedPublicIdentifier = "XHTML";
        String actualPublicIdentifier = doctype.getPublicIdentifier().toString();
        assert(expectedPublicIdentifier.equals(actualPublicIdentifier));

        String expectedSystemIdentifier = "/index.html";
        String actualSystemIdentifier = doctype.getSystemIdentifier();
        assert(expectedSystemIdentifier.equals(actualSystemIdentifier));

        boolean expectedForceQuirks = true;
        boolean actualForceQuirks = doctype.isForceQuirks();
        assert(expectedForceQuirks == actualForceQuirks);
    }
}
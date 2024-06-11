import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        String name = "html";
        doctype.getName().equals(name); // Basic functionality test

        doctype.reset();
        assertNull(doctype.getName()); // Test reset method
        name = "html5";
        doctype.getName(name);
        assertEquals(name, doctype.getName()); // Test setting name after reset
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.isForceQuirks(true);
        doctype.isForceQuirks(); // Basic functionality test

        doctype.reset();
        doctype.isForceQuirks(false);
        doctype.isForceQuirks(); // Test reset method

        doctype.isForceQuirks(true); // Test setting forceQuirks after reset
        assertFalse(doctype.isForceQuirks()); // Exception handling: forceQuirks should be false by default
    }

    // Test other methods in Doctype class similarly and add test cases for other Token subclasses
}
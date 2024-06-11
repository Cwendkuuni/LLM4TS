import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctypeName() {
        Token token = new Token.Doctype();
        Token expected = new Token.Doctype();
        expected.name = "html";
        assertEquals(expected.toString(), token.asDoctype().toString());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeForceQuirks() {
        Token token = new Token.Doctype();
        token.asDoctype().isForceQuirks = true;
        assertFalse(token.asDoctype().isForceQuirks()); // should throw AssertionError
    }
}
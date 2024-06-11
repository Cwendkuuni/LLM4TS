import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenReset() {
        Token token = new Doctype();
        token.reset();
        assertTrue(token.isDoctype());
        assertNull(token.getName());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());

        token = new StartTag();
        token.reset();
        assertTrue(token.isStartTag());
        assertNull(token.name());
        assertNull(token.getAttributes());

        // ... add test cases for other subclasses
    }

    @Test
    public void testTokenAttribute() {
        StartTag startTag = new StartTag().name("html");
        startTag.newAttribute().key("id").value("root");
        Attributes attributes = startTag.getAttributes();
        assertEquals("id", attributes.key("id"));
        assertEquals("root", attributes.value("id"));

        // ... add test cases for other edge cases and error scenarios
    }

    // ... add test cases for other methods and subclasses
}
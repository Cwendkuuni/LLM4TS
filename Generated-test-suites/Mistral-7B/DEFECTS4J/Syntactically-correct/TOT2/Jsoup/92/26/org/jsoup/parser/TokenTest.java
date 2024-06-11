import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTest {
    // Extract and list all the public methods including their signatures
    // ...

    @Test
    public void testDoctype() {
        // Given
        Token doctype = new Token.Doctype();

        // When
        String name = doctype.getName();
        String pubSysKey = doctype.getPubSysKey();
        String publicIdentifier = doctype.getPublicIdentifier();
        String systemIdentifier = doctype.getSystemIdentifier();
        boolean forceQuirks = doctype.isForceQuirks();

        // Then
        assertEquals("doctype", name);
        assertNull(pubSysKey);
        assertNull(publicIdentifier);
        assertNull(systemIdentifier);
        assertFalse(forceQuirks);

        // Edge case: test with values set
        doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.setPubSysKey("pubSysKey");
        doctype.getPublicIdentifier().append("publicIdentifier");
        doctype.setSystemIdentifier("systemIdentifier");
        doctype.setForceQuirks(true);

        assertEquals("doctype", name);
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
}
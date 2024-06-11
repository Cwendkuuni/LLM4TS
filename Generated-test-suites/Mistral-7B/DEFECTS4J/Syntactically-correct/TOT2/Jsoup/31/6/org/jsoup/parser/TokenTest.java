import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        String name = "HTML";
        String publicIdentifier = "W3C";
        doctype.getName().equals(name);
        doctype.getPublicIdentifier().equals(publicIdentifier);
    }
}
import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.JDOMFactory;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JDOMResultTest {

    @Test
    public void testJDOMResultConstructor() {
        JDOMResult result = new JDOMResult();
        assertNotNull(result.getFactory());
        assertNotNull(result.getDocument());
        assertNotNull(result.getResultlist());
    }

    @Test(expected = JDOMException.class)
    public void testJDOMResultConstructorWithException() {
        JDOMResult result = new JDOMResult();
        result.setResult(null); // Set result to null to trigger the exception
    }
}
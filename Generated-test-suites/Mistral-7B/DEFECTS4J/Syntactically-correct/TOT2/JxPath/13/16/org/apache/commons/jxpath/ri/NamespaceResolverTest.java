import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NamespaceResolverTest {

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        String uri = resolver.getNamespaceURI("xsi");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", uri);
    }
}
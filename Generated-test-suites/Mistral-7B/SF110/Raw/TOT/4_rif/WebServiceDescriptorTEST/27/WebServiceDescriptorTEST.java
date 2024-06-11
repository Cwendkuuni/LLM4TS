import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    // 
    
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Client.class, "http://example.com/ns1", "http://example.com/ns2");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(Client.class, serviceClazz);
    }

    // 
}
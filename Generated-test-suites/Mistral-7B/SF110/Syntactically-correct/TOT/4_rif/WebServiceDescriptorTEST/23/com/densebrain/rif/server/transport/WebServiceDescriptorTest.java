import static org.junit.Assert.assertEquals;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebService.class; // Assuming WebService is the actual service class
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = descriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }
    
}
import static org.junit.Assert.assertEquals;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebService.class; // Replace with the actual service class
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnServiceClazz);
    }
    
}
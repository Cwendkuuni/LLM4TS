import static org.junit.Assert.assertEquals;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {
    
    
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebService.class; // Assume WebService is the class passed to the constructor
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();

        assertEquals(serviceClazz, returnedServiceClazz);
    }

    
}
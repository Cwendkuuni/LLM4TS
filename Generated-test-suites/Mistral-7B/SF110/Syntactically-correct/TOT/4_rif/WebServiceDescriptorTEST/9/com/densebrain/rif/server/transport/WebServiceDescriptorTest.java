import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class; // Assuming WebServiceDescriptor is the test class
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = webServiceServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }
}
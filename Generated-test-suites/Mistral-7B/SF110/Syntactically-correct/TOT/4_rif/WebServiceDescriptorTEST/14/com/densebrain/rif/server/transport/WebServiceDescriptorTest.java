import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Class returnedServiceClazz = webServiceServiceDescriptor.getServiceClazz();

        assertEquals(serviceClazz, returnedServiceClazz);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class actualServiceClazz = descriptor.getServiceClazz();
        assertEquals(serviceClazz, actualServiceClazz);
    }
}
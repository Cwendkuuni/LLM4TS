import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, actualServiceClazz);
    }
}
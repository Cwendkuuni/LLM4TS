import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class<WebServiceDescriptor> serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertNotNull(descriptor);
    }
}
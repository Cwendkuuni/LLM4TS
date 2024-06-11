import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(WebService.class, "targetNamespace", "typesNamespace");
        Class expectedServiceClass = WebService.class;
        Class actualServiceClass = descriptor.getServiceClazz();
        assertEquals(expectedServiceClass, actualServiceClass);
    }
}
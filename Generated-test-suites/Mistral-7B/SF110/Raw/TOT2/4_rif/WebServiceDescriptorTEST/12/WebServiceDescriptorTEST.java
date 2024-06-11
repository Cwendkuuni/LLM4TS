import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(SomeService.class, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = descriptor.getServiceClazz();
        assertEquals(SomeService.class, returnedServiceClazz);
    }
}
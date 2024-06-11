import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    // ... Previous test cases

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "targetNamespace", "typesNamespace");
        Class expectedServiceClazz = MyService.class;
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(expectedServiceClazz, actualServiceClazz);
    }
}
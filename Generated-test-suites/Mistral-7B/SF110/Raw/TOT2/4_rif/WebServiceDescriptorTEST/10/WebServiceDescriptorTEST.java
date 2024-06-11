import static org.junit.Assert.assertEquals;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        Class expectedServiceClazz = String.class;
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(expectedServiceClazz, actualServiceClazz);
    }
}
import static org.junit.Assert.assertEquals;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyService.class, "targetNamespace", "typesNamespace");
        Class expectedServiceClazz = MyService.class;
        Class actualServiceClazz = descriptor.getServiceClazz();
        assertEquals(expectedServiceClazz, actualServiceClazz);
    }
}
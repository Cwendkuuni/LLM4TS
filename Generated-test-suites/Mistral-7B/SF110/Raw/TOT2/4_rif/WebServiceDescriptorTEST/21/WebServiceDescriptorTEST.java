import static org.junit.Assert.assertEquals;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyServiceClass.class, "targetNamespace", "typesNamespace");
        Class expectedServiceClass = MyServiceClass.class;
        Class actualServiceClass = descriptor.getServiceClazz();
        assertEquals(expectedServiceClass, actualServiceClass);
    }
}
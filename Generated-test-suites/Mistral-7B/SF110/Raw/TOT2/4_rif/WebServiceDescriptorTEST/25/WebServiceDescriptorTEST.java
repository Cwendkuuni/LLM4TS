import org.junit.Test;
import org.junit.Assert;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        Assert.assertEquals(serviceClazz, returnedServiceClazz);
    }
}
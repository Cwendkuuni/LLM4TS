import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {
    
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }
    
}
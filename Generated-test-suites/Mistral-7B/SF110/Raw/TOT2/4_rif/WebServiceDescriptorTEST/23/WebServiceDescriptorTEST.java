import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(StringService.class, "targetNamespace", "typesNamespace");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();
        Assert.assertEquals(StringService.class, serviceClazz);
    }
}
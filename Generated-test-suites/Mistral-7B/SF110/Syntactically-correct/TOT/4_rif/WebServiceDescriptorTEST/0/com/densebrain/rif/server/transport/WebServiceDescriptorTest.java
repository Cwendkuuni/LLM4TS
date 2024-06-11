import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorWithValidArguments() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals("Expected service class to match", serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals("Expected target namespaces to match", targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals("Expected types namespaces to match", typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithNullArguments() {
        Class serviceClazz = null;
        String targetNamespace = null;
        String typesNamespace = null;

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        // null serviceClazz should not cause an error, but the getServiceClazz() method should return null
        assertNull("Expected getServiceClazz() to return null with null serviceClazz", webServiceDescriptor.getServiceClazz());
        assertEquals("Expected targetNamespace to be set to null", null, webServiceDescriptor.getTargetNamespace());
        assertEquals("Expected typesNamespace to be set to null", null, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithEmptyStrings() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "";
        String typesNamespace = "";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals("Expected service class to match", serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals("Expected target namespace to be empty string", "", webServiceDescriptor.getTargetNamespace());
        assertEquals("Expected types namespace to be empty string", "", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithNonEmptyStrings() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        // two WebServiceDescriptors with the same arguments should be equal
        assertTrue("Expected two WebServiceDescriptors to be equal", webServiceDescriptor1.equals(webServiceDescriptor2));

        // two WebServiceDescriptors with different arguments should not be equal
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(Object.class, targetNamespace, typesNamespace);
        assertFalse("Expected two WebServiceDescriptors to be unequal", webServiceDescriptor1.equals(webServiceDescriptor3));
    }
}
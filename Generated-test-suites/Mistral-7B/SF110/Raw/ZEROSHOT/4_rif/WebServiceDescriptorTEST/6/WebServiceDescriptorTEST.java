import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest
{
    @Test
    public void testWebServiceDescriptorConstructor()
    {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz()
    {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz()
    {
        Class originalServiceClazz = Mockito.mock(Class.class);
        Class newServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(originalServiceClazz, null, null);

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace()
    {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);

        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace()
    {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        String newTargetNamespace = "newTargetNamespace";

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);

        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace()
    {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");

        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace()
    {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        String newTypesNamespace = "newTypesNamespace";

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCodeEquals()
    {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, null, null);

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }
}
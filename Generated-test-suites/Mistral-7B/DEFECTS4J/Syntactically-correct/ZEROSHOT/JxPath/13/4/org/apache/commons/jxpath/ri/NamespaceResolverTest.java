import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testCreateNewNamespaceResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getParent());
        Assert.assertEquals(0, namespaceResolver.getNamespaceMap().size());
        Assert.assertEquals(0, namespaceResolver.getReverseMap().size());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testCreateNamespaceResolverWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
        Assert.assertSame(parentResolver, namespaceResolver.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        Assert.assertEquals(1, namespaceResolver.getNamespaceMap().size());
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodePointer nodePointer = new NodePointer("test");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Assert.assertSame(nodePointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodePointer nodePointer = new NodePointer("test");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Assert.assertSame(nodePointer, namespaceResolver.getNamespaceContextPointer());
        Assert.assertNull(namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsd"));

        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsd"));

        NodePointer nodePointer = new NodePointer("test");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Assert.assertEquals("xml", namespaceResolver.getNamespaceURI("xml"));
        Assert.assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getPrefix("xsi"));
        Assert.assertNull(namespaceResolver.getPrefix("xsd"));

        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        Assert.assertNull(namespaceResolver.getPrefix("xsd"));

        NodePointer nodePointer = new NodePointer("test");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Assert.assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        Assert.assertNull(namespaceResolver.getPrefix("xsd"));

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        Assert.assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());

        namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertFalse(namespaceResolver.isSealed());
        Assert.assertFalse(clonedResolver.isSealed());
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", clonedResolver.getNamespaceURI("xsi"));
    }

}
import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;

    @Before
    public void setup() {
        String prefix1 = "prefix1";
        String prefix2 = "prefix2";
        String prefix3 = "prefix3";
        String uri1 = "uri1";
        String uri2 = "uri2";
        String uri3 = "uri3";

        Namespace ns1 = new Namespace(prefix1, uri1);
        Namespace ns2 = new Namespace(prefix2, uri2);
        Namespace ns3 = new Namespace(prefix3, uri3);

        List<Namespace> seed = Arrays.asList(ns1, Namespace.XML_NAMESPACE);
        namespaceStack = new NamespaceStack(seed.toArray(new Namespace[seed.size()]));
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(namespaceStack);
        Assert.assertNotNull(namespaceStack.scope);
        Assert.assertNotNull(namespaceStack.added);
        Assert.assertEquals(-1, namespaceStack.depth);
    }

    @Test
    public void testPushElement() {
        Element element = new Element("root", ns1);
        namespaceStack.push(element);
        Namespace[] expectedScope = new Namespace[]{ns1, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, namespaceStack.scope[namespaceStack.depth]);
        Assert.assertEquals(1, namespaceStack.depth);

        Element elementWithAdditionalNamespaces = new Element("root", ns1);
        elementWithAdditionalNamespaces.setAdditionalNamespaces(Arrays.asList(ns2, ns3));
        namespaceStack.push(elementWithAdditionalNamespaces);
        Namespace[] expectedScope2 = new Namespace[]{ns3, ns2, ns1, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope2, namespaceStack.scope[namespaceStack.depth]);
        Assert.assertEquals(2, namespaceStack.depth);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushElementWithNegativeDepth() {
        namespaceStack.pop();
        namespaceStack.pop();
        namespaceStack.push(new Element("root", ns1));
    }

    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("attr", ns1.getURI(), "value");
        namespaceStack.push(attribute);
        Namespace[] expectedScope = new Namespace[]{ns1, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, namespaceStack.scope[namespaceStack.depth]);
        Assert.assertEquals(1, namespaceStack.depth);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushAttributeWithNegativeDepth() {
        namespaceStack.pop();
        namespaceStack.push(new Attribute("attr", ns1.getURI(), "value"));
    }

    @Test
    public void testPop() {
        Element element = new Element("root", ns1);
        namespaceStack.push(element);
        namespaceStack.push(new Attribute("attr", ns2.getURI(), "value"));
        Assert.assertEquals(2, namespaceStack.depth);

        namespaceStack.pop();
        Assert.assertEquals(1, namespaceStack.depth);
        Namespace[] expectedScope = new Namespace[]{ns1, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, namespaceStack.scope[namespaceStack.depth]);

        namespaceStack.pop();
        Assert.assertEquals(0, namespaceStack.depth);
        Assert.assertNull(namespaceStack.scope[namespaceStack.depth]);
        Assert.assertNull(namespaceStack.added[namespaceStack.depth]);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWithNegativeDepth() {
        namespaceStack.pop();
        namespaceStack.pop();
        namespaceStack.pop();
    }

    @Test
    public void testAddedForward() {
        Element element = new Element("root", ns1);
        namespaceStack.push(element);
        namespaceStack.push(new Attribute("attr", ns2.getURI(), "value"));

        Iterable<Namespace> namespaces = namespaceStack.addedForward();
        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(ns2);
        expectedNamespaces.add(ns1);
        Assert.assertEquals(expectedNamespaces, namespaces);
    }

    @Test
    public void testAddedReverse() {
        Element element = new Element("root", ns1);
        namespaceStack.push(element);
        namespaceStack.push(new Attribute("attr", ns2.getURI(), "value"));

        Iterable<Namespace> namespaces = namespaceStack.addedReverse();
        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(ns1);
        expectedNamespaces.add(ns2);
        Assert.assertEquals(expectedNamespaces, namespaces);
    }

    @Test
    public void testIsInScope() {
        Element element = new Element("root", ns1);
        namespaceStack.push(element);

        Assert.assertTrue(namespaceStack.isInScope(ns1));
        Assert.assertTrue(namespaceStack.isInScope(ns2)); // should return false

        namespaceStack.push(new Attribute("attr", ns2.getURI(), "value"));

        Assert.assertTrue(namespaceStack.isInScope(ns1));
        Assert.assertTrue(namespaceStack.isInScope(ns2));
    }
}
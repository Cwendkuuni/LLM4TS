import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesFactory;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        // Test creating a node with a base URI and attributes
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesFactory()
                .put("id", "myId")
                .put("class", "myClass")
                .put("title", "My Title")
                .create();
        Node node = new Element("div", baseUri, attributes);

        Assert.assertEquals("div", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());

        // Test creating a node without a base URI or attributes
        node = new Element("div");
        Assert.assertEquals("div", node.nodeName());
        Assert.assertNull(node.baseUri());
        Assert.assertNull(node.attributes());

        // Test creating an empty node
        node = new Element();
        Assert.assertEquals("", node.nodeName());
        Assert.assertNull(node.baseUri());
        Assert.assertNull(node.attributes());
        Assert.assertFalse(node.hasChildren());
    }

    @Test
    public void testAttr() {
        Element element = new Element("div");
        element.attr("id", "myId");
        Assert.assertEquals("myId", element.attr("id"));
        Assert.assertEquals("", element.attr("nonExistentAttr"));

        element.attr("class", "class1 class2");
        Assert.assertEquals("class1 class2", element.attr("class"));

        element.attr("title", "My Title");
        Assert.assertEquals("My Title", element.attr("title"));

        element.attr("abs:href", "http://example.com/page.html");
        Assert.assertEquals("http://example.com/page.html", element.absUrl("href"));
    }

    @Test
    public void testAttributes() {
        Element element = new Element("div");
        element.attr("id", "myId");
        element.attr("class", "myClass");
        element.attr("title", "My Title");

        Attributes attributes = element.attributes();
        Assert.assertEquals("myId", attributes.get("id"));
        Assert.assertEquals("class1 class2", attributes.get("class"));
        Assert.assertEquals("My Title", attributes.get("title"));

        // Test empty attributes
        element = new Element();
        attributes = element.attributes();
        Assert.assertTrue(attributes.isEmpty());
    }

    @Test
    public void testChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");

        parent.addChild(child);
        Node childNode = parent.childNode(0);
        Assert.assertEquals(child, childNode);

        // Test getting empty childNodes list
        Element emptyParent = new Element("div");
        Node emptyChildNodes[] = emptyParent.childNodesAsArray();
        Assert.assertArrayEquals(new Node[0], emptyChildNodes);
    }

    @Test
    public void testHasAttr() {
        Element element = new Element("div");
        element.attr("id", "myId");

        // Test has attribute
        Assert.assertTrue(element.hasAttr("id"));
        Assert.assertTrue(element.hasAttr("class")); // by default, all elements have a "class" attribute

        // Test missing attribute
        Assert.assertFalse(element.hasAttr("nonExistentAttr"));

        // Test case-insensitive attribute checking
        Assert.assertTrue(element.hasAttr("id"));
        Assert.assertTrue(element.hasAttr("Id"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = new Element("div");
        element.attr("id", "myId");

        // Test removing attribute
        element.removeAttr("id");
        Assert.assertFalse(element.hasAttr("id"));

        // Test removing non-existent attribute
        element.removeAttr("nonExistentAttr");
        Assert.assertFalse(element.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testAbsUrl() {
        Element element = new Element("a");
        element.attr("abs:href", "http://example.com/page.html");

        // Test absolute URL
        Assert.assertEquals("http://example.com/page.html", element.absUrl("href"));

        // Test relative URL
        String baseUri = "http://example.com";
        element = new Element("a", baseUri);
        element.attr("href", "/page.html");
        Assert.assertEquals("http://example.com/page.html", element.absUrl("href"));

        // Test missing attribute
        element = new Element("a", baseUri);
        Assert.assertEquals("", element.absUrl("href"));
    }
}
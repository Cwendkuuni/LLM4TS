public class LeafNodeTest {
    // ... (import statements, class definition)

    @Test
    public void testHasAttributesWithEmptyNode() {
        LeafNode node = new Elements().emptyTag("html");
        assertFalse(node.hasAttributes());
    }

    @Test
    public void testHasAttributesWithStringValueNode() {
        LeafNode node = new Elements().textNode("Hello World");
        assertFalse(node.hasAttributes());
    }

    @Test
    public void testHasAttributesWithAttributeMapValueNode() {
        LeafNode node = new Elements().tag("div").attr("id", "test");
        assertTrue(node.hasAttributes());
    }

    // ...
}
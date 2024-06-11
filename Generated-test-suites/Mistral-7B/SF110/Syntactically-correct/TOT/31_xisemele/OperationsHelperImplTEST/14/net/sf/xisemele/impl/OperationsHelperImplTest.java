
package net.sf.xisemele.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.w3c.dom.Node.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        String xml = "<root><child1></child1><child2><grandchild></grandchild></child2></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        Node root = document.getDocumentElement();
        List<Node> children = new OperationsHelperImpl().children(root);
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testFind() {
        String xml = "<root><child1></child1><child2><grandchild></grandchild></child2></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        List<Node> testCases = Arrays.asList(
            new Object[]{"/root", document.getDocumentElement()},
            new Object[]{"/root/child1", document.getDocumentElement().getChildNodes().item(0)},
            new Object[]{"/root/child2/grandchild", document.getDocumentElement().getChildNodes().item(1).getChildNodes().item(0)}
        );
        for (Object[] testCase : testCases) {
            String path = (String) testCase[0];
            Document doc = (Document) testCase[1];
            Node expected = (Node) testCase[1];
            Node actual = new OperationsHelperImpl().find(doc, path);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testNodeAndSublistWithName() {
        String xml = "<root><child1></child1><child2><grandchild></grandchild></child2></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());

        Node nodeWithName = new OperationsHelperImpl().nodeWithName(children, "child2");
        List<Node> sublistWithName = new OperationsHelperImpl().sublistWithName(children, "child2");

        assertEquals(1, children.size());
        assertEquals(nodeWithName, children.get(0));
        assertEquals(1, sublistWithName.size());
        assertEquals(nodeWithName, sublistWithName.get(0));

        List<Node> emptyList = new ArrayList<>();
        assertNull(new OperationsHelperImpl().nodeWithName(emptyList, "child2"));
        assertNull(new OperationsHelperImpl().sublistWithName(emptyList, "child2"));
    }
}
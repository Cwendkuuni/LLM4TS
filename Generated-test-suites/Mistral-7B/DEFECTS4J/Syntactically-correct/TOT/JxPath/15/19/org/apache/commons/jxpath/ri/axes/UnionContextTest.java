import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;

    @Before
    public void setUp() {
        contextA = new EvalContext(createDocument(), null);
        contextB = new EvalContext(createDocument(), null);
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testNextSet_NoNodesInBothContexts() {
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testNextSet_AtLeastOneNodeInEachContext() {
        contextA.pushContext(new BasicNodeSet());
        contextA.setCurrentNode(createElement("a", null));
        contextB.pushContext(new BasicNodeSet());
        contextB.setCurrentNode(createElement("b", null));
        unionContext.nextSet();

        assertTrue(unionContext.nextNode());
        assertTrue(unionContext.getCurrentNodePointer().getNode().getNodeName().equals("a"));
        unionContext.nextNode();
        unionContext.nextNode();
        assertTrue(unionContext.getCurrentNodePointer().getNode().getNodeName().equals("b"));
        unionContext.nextNode(); // should return false
    }

    @Test
    public void testNextSet_AtLeastOneNodeInOneContext() {
        contextA.pushContext(new BasicNodeSet());
        contextA.setCurrentNode(createElement("a", null));
        unionContext = new UnionContext(null, new EvalContext[]{contextA});

        unionContext.nextSet();
        assertTrue(unionContext.nextNode());
        assertTrue(unionContext.getCurrentNodePointer().getNode().getNodeName().equals("a"));
        unionContext.nextNode(); // should return false

        contextA.pushContext(new BasicNodeSet());
        contextA.setCurrentNode(createElement("c", null));
        unionContext.nextSet();
        assertTrue(unionContext.nextNode());
        assertTrue(unionContext.getCurrentNodePointer().getNode().getNodeName().equals("c"));
        unionContext.nextNode(); // should return false
    }

    @Test
    public void testNextSet_NoNodesInOneContext() {
        contextA.pushContext(new BasicNodeSet());
        contextA.nextSet();
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

        unionContext.nextSet();
        assertTrue(unionContext.nextNode());
        assertTrue(unionContext.getCurrentNodePointer().getNode().getNodeName().equals("a"));
        unionContext.nextNode(); // should return false

        contextB.pushContext(new BasicNodeSet());
        contextB.setCurrentNode(createElement("b", null));
        unionContext.nextSet();
        assertTrue(unionContext.nextNode());
        assertTrue(unionContext.getCurrentNodePointer().getNode().getNodeName().equals("b"));
        unionContext.nextNode(); // should return false
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_PositionOutOfBounds() {
        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.setPosition(-1);
    }

    private Document createDocument() {
        Document document = JXPathContext.newDocument();
        document.appendChild(createElement("root", document));
        return document;
    }

    private Element createElement(String name, Document document) {
        Element element = document.createElement(name);
        document.getDocumentElement().appendChild(element);
        return element;
    }

    private List<NodePointer> createNodePointers(int size) {
        List<NodePointer> nodePointers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            nodePointers.add(new NodePointer(createElement("node" + i, null), null));
        }
        return nodePointers;
    }

    private List<NodePointer> getCurrentNodePointers(EvalContext context) {
        List<NodePointer> nodePointers = new ArrayList<>();
        while (context.nextSet()) {
            nodePointers.addAll((List<NodePointer>) context.getCurrentNodes());
        }
        return nodePointers;
    }

    @Test
    public void testSetPosition_PositionWithinBounds() {
        Document document = createDocument();
        EvalContext contextA = new EvalContext(document, null);
        EvalContext contextB = new EvalContext(document, null);
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

        List<NodePointer> nodePointersA = createNodePointers(2);
        List<NodePointer> nodePointersB = createNodePointers(2);

        unionContext.nextSet();
        unionContext.setPosition(0);
        List<NodePointer> currentNodePointers = getCurrentNodePointers(unionContext);
        assertTrue(currentNodePointers.size() == 4);
        assertTrue(currentNodePointers.get(0).getNode().getNodeName().equals("root"));
        assertTrue(currentNodePointers.get(1).getNode().getNodeName().equals("a"));
        assertTrue(currentNodePointers.get(2).getNode().getNodeName().equals("a"));
        assertTrue(currentNodePointers.get(3).getNode().getNodeName().equals("b"));
    }

    @Test
    public void testSetPosition_PositionWithinBoundsForContextB() {
        Document document = createDocument();
        EvalContext contextA = new EvalContext(document, null);
        EvalContext contextB = new EvalContext(document, null);
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

        List<NodePointer> nodePointersA = createNodePointers(2);
        List<NodePointer> nodePointersB = createNodePointers(2);

        unionContext.nextSet();
        unionContext.setPosition(2);
        List<NodePointer> currentNodePointers = getCurrentNodePointers(unionContext);
        assertTrue(currentNodePointers.size() == 2);
        assertTrue(currentNodePointers.get(0).getNode().getNodeName().equals("b"));
        assertTrue(currentNodePointers.get(1).getNode().getNodeName().equals("b"));
    }
}
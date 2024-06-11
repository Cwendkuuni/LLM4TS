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

    private Document document;
    private EvalContext contextA;
    private EvalContext contextB;
    private UnionContext unionContext;

    @Before
    public void setUp() throws Exception {
        document = TestUtil.createDocument();

        contextA = new EvalContext(new JXPathContext(document));
        contextA.addVariable("a", new NodePointer(document.createElement("a")));
        contextA.addVariable("b", new NodePointer(document.createElement("b")));

        contextB = new EvalContext(new JXPathContext(document));
        contextB.addVariable("c", new NodePointer(document.createElement("c")));
        contextB.addVariable("a", new NodePointer(document.createElement("a")));

        unionContext = new UnionContext(contextA, contextB);
    }

    @Test
    public void testEmptyContexts() {
        unionContext.nextSet();
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testSingleContextWithEmptySet() {
        contextA.setDocumentOrder(0);
        contextA.nextSet();
        unionContext.nextSet();
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testSingleContextWithNonEmptySet() {
        contextA.setDocumentOrder(0);
        contextA.nextSet();
        contextA.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();
        assertTrue(unionContext.getCurrentNode().getNode().getNodeType() == Node.ELEMENT_NODE);
    }

    @Test
    public void testBothContextsWithEmptySets() {
        contextA.nextSet();
        contextB.nextSet();
        unionContext.nextSet();
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testBothContextsWithNonEmptySets() {
        contextA.nextSet();
        contextA.nextNode();
        contextB.nextSet();
        contextB.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();
        assertTrue(unionContext.getCurrentNode().getDocument() == document);
        assertTrue(unionContext.getCurrentNode().getDocument().getDocumentElement() != unionContext.getCurrentNode().getPreviousNode().getDocument().getDocumentElement());
    }

    @Test
    public void testBothContextsWithOverlappingNodes() {
        contextA.nextSet();
        contextA.nextNode();
        contextB.nextSet();
        contextB.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeA = unionContext.getCurrentNode();
        unionContext.nextNode();
        NodePointer nodeB = unionContext.getCurrentNode();
        assertTrue(nodeA.equals(nodeB));
    }

    @Test(expected = IllegalStateException.class)
    public void testBothContextsWithNonOverlappingNodes() {
        contextA.nextSet();
        contextA.nextNode();
        contextB.nextSet();
        contextB.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.nextNode();
    }
}
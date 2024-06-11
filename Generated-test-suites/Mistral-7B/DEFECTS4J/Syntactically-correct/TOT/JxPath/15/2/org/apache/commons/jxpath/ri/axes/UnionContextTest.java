import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;

    @Before
    public void setUp() {
        Document doc = createTestDocument();
        contextA = new EvalContext(doc, null);
        contextB = new EvalContext(doc, null);
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_nextSetInContextA_returnTrue() {
        contextA.nextSet();
        assertTrue(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_nextNodeInContextA_returnTrue() {
        contextA.nextSet();
        contextA.nextNode();
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPosition_nextNodeInContextB_returnTrue() {
        contextB.nextSet();
        contextB.nextNode();
        assertTrue(unionContext.setPosition(2));
    }

    @Test
    public void testSetPosition_positionOutOfRange_returnFalse() {
        contextA.nextSet();
        contextA.nextNode();
        contextB.nextSet();
        contextB.nextNode();
        assertFalse(unionContext.setPosition(3));
    }

    @Test
    public void testSetPosition_positionInContextA_returnCurrentNode() {
        contextA.nextSet();
        contextA.nextNode();
        NodePointer nodeA = contextA.getCurrentNodePointer();
        unionContext.setPosition(1);
        NodePointer nodeB = unionContext.getCurrentNodePointer();
        assertSame(nodeA, nodeB);
    }

    @Test
    public void testSetPosition_positionInContextB_returnCurrentNode() {
        contextB.nextSet();
        contextB.nextNode();
        NodePointer nodeB = contextB.getCurrentNodePointer();
        unionContext.setPosition(2);
        NodePointer nodeC = unionContext.getCurrentNodePointer();
        assertSame(nodeB, nodeC);
    }

    @Test
    public void testCurrentNodePointer_positionOutOfRange_returnNull() {
        unionContext.setPosition(3);
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test(expected = EvaluationException.class)
    public void testNextSet_contextsAreEmpty() {
        unionContext = new UnionContext(null, new EvalContext[0]);
        unionContext.nextSet();
    }

    private Document createTestDocument() {
        Document doc = DocumentBuilderFactory.newInstance().newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        Element a = doc.createElement("a");
        a.setTextContent("A");
        root.appendChild(a);

        Element b = doc.createElement("b");
        b.setTextContent("B");
        root.appendChild(b);

        Element c = doc.createElement("c");
        root.appendChild(c);

        return doc;
    }

    @Test
    public void testNextNode_contextA_hasMoreNodes_returnTrue() {
        contextA.nextSet();
        contextA.nextNode();
        assertTrue(contextA.hasNextNode());
    }

    @Test
    public void testNextNode_contextA_noMoreNodes_returnFalse() {
        contextA.nextSet();
        contextA.nextNode();
        contextA.nextNode();
        assertFalse(contextA.hasNextNode());
    }

    @Test
    public void testNextNode_contextB_hasMoreNodes_returnTrue() {
        contextB.nextSet();
        contextB.nextNode();
        assertTrue(contextB.hasNextNode());
    }

    @Test
    public void testNextNode_contextB_noMoreNodes_returnFalse() {
        contextB.nextSet();
        contextB.nextNode();
        contextB.nextNode();
        assertFalse(contextB.hasNextNode());
    }
}
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

    private EvalContext contextA, contextB;
    private UnionContext unionContext;
    private Document document;

    @Before
    public void setUp() throws Exception {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element a = document.createElement("a");
        root.appendChild(a);

        Element b1 = document.createElement("b1");
        root.appendChild(b1);

        Element b2 = document.createElement("b2");
        root.appendChild(b2);

        contextA = new EvalContext(null, new NodePointer(document, document.getDocumentElement()), new BasicNodeSet(document.getDocumentElement()));
        contextB = new EvalContext(null, new NodePointer(document, b1), new BasicNodeSet(b1));

        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testPosition_FirstContext() {
        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertEquals(document.getDocumentElement(), nodeA.getNode());

        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testPosition_FirstContext_SecondNode() {
        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertEquals(document.getDocumentElement(), nodeA.getNode());

        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        unionContext.nextNode();
        NodePointer nodeB2 = unionContext.getCurrentNodePointer();
        assertEquals(b2, nodeB2.getNode());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testPosition_SecondContext_FirstNode() {
        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB2 = unionContext.getCurrentNodePointer();
        assertEquals(b2, nodeB2.getNode());

        unionContext.nextNode();
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertEquals(document.getDocumentElement(), nodeA.getNode());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testPosition_SecondContext_SecondNode() {
        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB2 = unionContext.getCurrentNodePointer();
        assertEquals(b2, nodeB2.getNode());

        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testPosition_BothContexts_FirstNode() {
        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertEquals(document.getDocumentElement(), nodeA.getNode());

        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        unionContext.nextNode();
        NodePointer nodeB2 = unionContext.getCurrentNodePointer();
        assertEquals(b2, nodeB2.getNode());

        unionContext.nextNode();
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertEquals(document.getDocumentElement(), nodeA.getNode());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testPosition_BothContexts_SecondNode() {
        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertEquals(document.getDocumentElement(), nodeA.getNode());

        unionContext.nextSet();
        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(b1, nodeB1.getNode());

        unionContext.nextNode();
        NodePointer nodeB2 = unionContext.getCurrentNodePointer();
        assertEquals(b2, nodeB2.getNode());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testPosition_EmptyContext() {
        EvalContext emptyContext = new EvalContext(null, new NodePointer(document, document.getDocumentElement()), new BasicNodeSet(document.getDocumentElement()));
        UnionContext emptyUnionContext = new UnionContext(null, new EvalContext[]{emptyContext});

        assertFalse(emptyUnionContext.nextSet());
    }

    @Test
    public void testPosition_NullContext() {
        UnionContext nullUnionContext = new UnionContext(null, null);

        assertFalse(nullUnionContext.nextSet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContexts() {
        new UnionContext(null, (EvalContext[]) null);
    }

    @Test
    public void testPosition_ZeroContexts() {
        UnionContext zeroContextUnion = new UnionContext(null, new EvalContext[0]);

        assertFalse(zeroContextUnion.nextSet());
    }

    @Test
    public void testPosition_EmptyContexts() {
        UnionContext emptyContextsUnion = new UnionContext(null, new EvalContext[0]);

        assertFalse(emptyContextsUnion.nextSet());
    }

    @Test
    public void testToString() {
        String unionContextString = unionContext.toString();
        assertTrue(unionContextString.contains("UnionContext"));

        String contextsToString = unionContextString.substring(unionContextString.indexOf("contexts=") + 7);
        List<String> contextStrings = new ArrayList<>(Arrays.asList(contextStrings.split(", ")));
        assertEquals(2, contextStrings.size());
        assertTrue(contextStrings.contains("EvalContext@"));
        assertTrue(contextStrings.contains("EvalContext@"));
    }
}
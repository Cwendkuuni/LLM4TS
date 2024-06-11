import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UnionContext tests")
public class UnionContextTest {

    @DisplayName("Test empty contexts union")
    @Test
    public void testEmptyContextsUnion() {
        EvalContext ctx1 = new UnionContext(new DocumentEvalContext(), new EvalContext[0]);
        assertEquals(0, ctx1.getSize());
        assertNull(ctx1.getCurrentNode());
        assertFalse(ctx1.nextSet());
    }

    @DisplayName("Test single context union")
    @Test
    public void testSingleContextUnion() {
        Document document = createTestDocument();
        EvalContext ctx1 = new DocumentEvalContext(document);
        EvalContext ctx2 = new UnionContext(new DocumentEvalContext(document), new EvalContext[]{ctx1});
        assertEquals(ctx1.getSize(), ctx2.getSize());
        Node node1 = ctx1.nextNode();
        Node node2 = ctx2.nextNode();
        assertEquals(node1, node2);
        assertTrue(ctx2.nextSet());
    }

    @DisplayName("Test multiple contexts union")
    @Test
    public void testMultipleContextsUnion() {
        Document document = createTestDocument();
        EvalContext ctx1 = new DocumentEvalContext(document);
        EvalContext ctx2 = new DocumentEvalContext(document);
        EvalContext ctx3 = new DocumentEvalContext(document);
        EvalContext unionContext = new UnionContext(new DocumentEvalContext(document), new EvalContext[]{ctx1, ctx2, ctx3});

        int size = 0;
        while (unionContext.nextSet()) {
            size += unionContext.getSize();
        }

        assertEquals(3 * 3 + 2, size); // 3 sets in each context and 2 extra nodes in the first context
    }

    @DisplayName("Test set position in union context")
    @Test
    public void testSetPositionInUnionContext() {
        Document document = createTestDocument();
        EvalContext ctx1 = new DocumentEvalContext(document);
        EvalContext ctx2 = new DocumentEvalContext(document);
        EvalContext unionContext = new UnionContext(new DocumentEvalContext(document), new EvalContext[]{ctx1, ctx2});

        int position = 0;
        while (unionContext.nextSet()) {
            for (int i = 0; i < unionContext.getSize(); i++) {
                Node node = unionContext.nextNode();
                if (node.getNodeValue().equals("three")) {
                    position = i;
                    break;
                }
            }
        }

        assertTrue(unionContext.setPosition(position));
        Node node = unionContext.nextNode();
        assertEquals("three", node.getNodeValue());
    }

    @DisplayName("Test set position out of bounds in union context")
    @Test
    public void testSetPositionOutOfBoundsInUnionContext() {
        Document document = createTestDocument();
        EvalContext ctx1 = new DocumentEvalContext(document);
        EvalContext ctx2 = new DocumentEvalContext(document);
        EvalContext unionContext = new UnionContext(new DocumentEvalContext(document), new EvalContext[]{ctx1, ctx2});

        int position = unionContext.getSize();
        assertFalse(unionContext.setPosition(position));
    }

    @DisplayName("Test null contexts union")
    @Test
    public void testNullContextsUnion() {
        EvalContext ctx = new UnionContext(new DocumentEvalContext(new Document()), null);
        assertEquals(0, ctx.getSize());
        assertNull(ctx.getCurrentNode());
        assertFalse(ctx.nextSet());
    }

    @DisplayName("Test empty context union with non-empty context")
    @Test
    public void testEmptyContextUnionWithNonEmptyContext() {
        Document document = createTestDocument();
        EvalContext ctx1 = new DocumentEvalContext(document);
        EvalContext unionContext = new UnionContext(new DocumentEvalContext(document), new EvalContext[0]);
        unionContext.setParentContext(ctx1);

        int size = 0;
        while (unionContext.nextSet()) {
            size += unionContext.getSize();
        }

        assertEquals(0, size);
    }

    @DisplayName("Test union context with non-union parent context")
    @Test
    public void testUnionContextWithNonUnionParentContext() {
        Document document = createTestDocument();
        EvalContext parentContext = new DocumentEvalContext(document);
        EvalContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        int size = 0;
        while (unionContext.nextSet()) {
            size += unionContext.getSize();
        }

        assertEquals(0, size);
    }

    @DisplayName("Test union context with null parent context")
    @Test
    public void testUnionContextWithNullParentContext() {
        EvalContext unionContext = new UnionContext(null, new EvalContext[0]);
        assertEquals(0, unionContext.getSize());
        assertNull(unionContext.getCurrentNode());
        assertFalse(unionContext.nextSet());
    }

    private Document createTestDocument() {
        Document document = JXPathContext.newDocument("<?xml version=\"1.0\"?>", "<root><a>one</a><a>two</a><b>three</b></root>");
        return document;
    }

    private static class DocumentEvalContext extends EvalContext {
        public DocumentEvalContext(Document document) {
            super(new BasicNodeSet(), document, null, null);
        }

        @Override
        public NodePointer getCurrentNodePointer() {
            return getNodeSet().getNodePointer(getCurrentIndex());
        }

        @Override
        public boolean nextNode() {
            return getCurrentIndex() < getNodeSet().getSize();
        }

        @Override
        public boolean nextSet() {
            return getCurrentIndex() < getNodeSet().getSize() / getNodeSet().getLength();
        }
    }
}
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private EvalContext parentContext;
    private List<EvalContext> contexts;
    private UnionContext unionContext;

    public UnionContextTest(EvalContext[] contexts) {
        this.contexts = Arrays.asList(contexts);
    }

    @Before
    public void setUp() {
        EvalContext ctx1 = new EvalContext(new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };
        EvalContext ctx2 = new EvalContext(new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 2;
            }
        };
        parentContext = new EvalContext(new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };
        unionContext = new UnionContext(parentContext, contexts.toArray(new EvalContext[0]));
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_noContexts() {
        unionContext = new UnionContext(parentContext, new EvalContext[0]);
        assertFalse(unionContext.setPosition(0));
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_singleContext() {
        unionContext = new UnionContext(parentContext, new EvalContext[0]);
        contexts.set(0, new EvalContext(new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }

            @Override
            public NodePointer getCurrentNodePointer() {
                return new NodePointer(0, 0);
            }
        });
        assertTrue(unionContext.setPosition(0));
        assertSame(contexts.get(0).getCurrentNodePointer(), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_multipleContexts() {
        unionContext = new UnionContext(parentContext, contexts.toArray(new EvalContext[0]));
        int position = 0;
        for (EvalContext ctx : contexts) {
            while (ctx.nextSet()) {
                position++;
                while (ctx.nextNode()) {
                    if (!unionContext.setPosition(position)) {
                        assertSame(ctx.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
                        break;
                    }
                }
            }
        }
        assertSame(contexts.get(contexts.size() - 1).getCurrentNodePointer(), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_outOfBounds() {
        unionContext = new UnionContext(parentContext, contexts.toArray(new EvalContext[0]));
        assertFalse(unionContext.setPosition(-1));
        assertNull(unionContext.getCurrentNodePointer());

        int length = 0;
        for (EvalContext ctx : contexts) {
            while (ctx.nextSet()) {
                length += ctx.size();
            }
        }
        assertFalse(unionContext.setPosition(length + 1));
        assertNull(unionContext.getCurrentNodePointer());
    }
}
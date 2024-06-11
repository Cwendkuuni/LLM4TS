package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompareTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare.
 *
 * @author Dmitri Plotnikov
 */
public class CoreOperationCompareTest {

    /**
     * Test method for {@link CoreOperationCompare#CoreOperationCompare(Expression, Expression)}.
     */
    @Test
    public void testCoreOperationCompare() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new InitialContext();
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new InitialContext();
                }
            });
        assertNotNull(coreOperationCompare);
    }

    /**
     * Test method for {@link CoreOperationCompare#equal(EvalContext, Expression, Expression)}.
     */
    @Test
    public void testEqual() {
        Object left = new Integer(1);
        Object right = new Integer(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            });
        assertTrue(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new Integer(2);
        right = new Integer(1);
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new Integer(1);
        right = new Integer(2);
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new Integer(1);
        right = new String("1");
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new String("1");
        right = new Integer(1);
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new String("1");
        right = new String("1");
        assertTrue(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new String("1");
        right = new String("2");
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new String("1");
        right = null;
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = null;
        right = new String("1");
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new Pointer("1");
        right = new Pointer("1");
        assertTrue(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new Pointer("1");
        right = new Pointer("2");
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = new Pointer("1");
        right = null;
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));

        left = null;
        right = new Pointer("1");
        assertFalse(coreOperationCompare.equal(
            new EvalContext(),
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            }));
    }

    /**
     * Test method for {@link CoreOperationCompare#contains(Iterator, Object)}.
     */
    @Test
    public void testContains() {
        Collection collection = new ArrayList();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        Iterator iterator = collection.iterator();

        assertTrue(coreOperationCompare.contains(iterator, new Integer(1)));
        assertFalse(coreOperationCompare.contains(iterator, new Integer(3)));

        collection.clear();
        iterator = null;

        assertFalse(coreOperationCompare.contains(iterator, new Integer(1)));
    }

    /**
     * Test method for {@link CoreOperationCompare#findMatch(Iterator, Iterator)}.
     */
    @Test
    public void testFindMatch() {
        Collection collection1 = new ArrayList();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        Iterator iterator1 = collection1.iterator();

        Collection collection2 = new ArrayList();
        collection2.add(new Integer(1));
        collection2.add(new Integer(2));
        Iterator iterator2 = collection2.iterator();

        assertTrue(coreOperationCompare.findMatch(iterator1, iterator2));

        collection1.clear();
        iterator1 = null;

        assertFalse(coreOperationCompare.findMatch(iterator1, iterator2));

        collection2.clear();
        iterator2 = null;

        assertFalse(coreOperationCompare.findMatch(iterator1, iterator2));
    }

    /**
     * Test method for {@link CoreOperationCompare#equal(Object, Object)}.
     */
    @Test
    public void testEqual() {
        assertTrue(coreOperationCompare.equal(new Integer(1), new Integer(1)));
        assertFalse(coreOperationCompare.equal(new Integer(1), new Integer(2)));
        assertFalse(coreOperationCompare.equal(new Integer(1), new String("1")));
        assertFalse(coreOperationCompare.equal(new String("1"), new Integer(1)));
        assertFalse(coreOperationCompare.equal(new String("1"), new String("2")));
        assertFalse(coreOperationCompare.equal(new String("1"), null));
        assertFalse(coreOperationCompare.equal(null, new String("1")));
        assertTrue(coreOperationCompare.equal(new Pointer("1"), new Pointer("1")));
        assertFalse(coreOperationCompare.equal(new Pointer("1"), new Pointer("2")));
        assertFalse(coreOperationCompare.equal(new Pointer("1"), null));
        assertFalse(coreOperationCompare.equal(null, new Pointer("1")));
    }
}
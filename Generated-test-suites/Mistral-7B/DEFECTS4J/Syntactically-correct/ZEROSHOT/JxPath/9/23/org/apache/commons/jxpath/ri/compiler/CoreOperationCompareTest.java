package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.CoreOperationCompareTest;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private EvalContext context;
    private Expression arg1;
    private Expression arg2;
    private CoreOperationCompare compare;

    @Before
    public void setup() {
        context = new EvalContext();
        arg1 = new InitialContext();
        arg2 = new InitialContext();
        compare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqualTrue() {
        arg1.setValue(1);
        arg2.setValue(1);

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualFalse() {
        arg1.setValue(1);
        arg2.setValue(2);

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualBooleanTrueTrue() {
        arg1.setValue(new Boolean(true));
        arg2.setValue(new Boolean(true));

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualBooleanTrueFalse() {
        arg1.setValue(new Boolean(true));
        arg2.setValue(new Boolean(false));

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualBooleanFalseTrue() {
        arg1.setValue(new Boolean(false));
        arg2.setValue(new Boolean(true));

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualBooleanFalseFalse() {
        arg1.setValue(new Boolean(false));
        arg2.setValue(new Boolean(false));

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualNullNull() {
        arg1.setValue(null);
        arg2.setValue(null);

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualNullObject() {
        arg1.setValue(null);
        arg2.setValue(new Object());

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualObjectNull() {
        arg1.setValue(new Object());
        arg2.setValue(null);

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualStringEqual() {
        arg1.setValue("test");
        arg2.setValue("test");

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualStringNotEqual() {
        arg1.setValue("test");
        arg2.setValue("test2");

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualStringEmptyStringEmptyString() {
        arg1.setValue("");
        arg2.setValue("");

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualStringEmptyStringNonEmptyString() {
        arg1.setValue("");
        arg2.setValue("test");

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualStringNonEmptyStringEmptyString() {
        arg1.setValue("test");
        arg2.setValue("");

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualStringNonEmptyStringNonEmptyString() {
        arg1.setValue("test");
        arg2.setValue("test");

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualCollectionEmptyCollectionEmptyCollection() {
        arg1.setValue(Collections.emptyList());
        arg2.setValue(Collections.emptyList());

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualCollectionEmptyCollectionNonEmptyList() {
        arg1.setValue(Collections.emptyList());
        arg2.setValue(Arrays.asList("test"));

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualCollectionNonEmptyListEmptyCollection() {
        arg1.setValue(Arrays.asList("test"));
        arg2.setValue(Collections.emptyList());

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualCollectionNonEmptyListNonEmptyList() {
        arg1.setValue(Arrays.asList("test1", "test2"));
        arg2.setValue(Arrays.asList("test1", "test2"));

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualIteratorEmptyIteratorEmptyIterator() {
        arg1.setValue(Collections.emptyIterator());
        arg2.setValue(Collections.emptyIterator());

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualIteratorEmptyIteratorNonEmptyIterator() {
        arg1.setValue(Collections.emptyIterator());
        arg2.setValue(new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        });

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualIteratorNonEmptyIteratorEmptyIterator() {
        arg1.setValue(new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        });
        arg2.setValue(Collections.emptyIterator());

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualIteratorNonEmptyIteratorNonEmptyIterator() {
        arg1.setValue(new Iterator() {
            private Object[] elements = { "test1", "test2" };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        });

        arg2.setValue(new Iterator() {
            private Object[] elements = { "test1", "test2" };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        });

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testContainsTrue() {
        List list = Arrays.asList("test1", "test2", "test3");
        arg1.setValue(new Iterator() {
            private List elements = list;

            @Override
            public boolean hasNext() {
                return elements.iterator().hasNext();
            }

            @Override
            public Object next() {
                return elements.iterator().next();
            }
        });

        arg2.setValue("test2");

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testContainsFalse() {
        List list = Arrays.asList("test1", "test2", "test3");
        arg1.setValue(new Iterator() {
            private List elements = list;

            @Override
            public boolean hasNext() {
                return elements.iterator().hasNext();
            }

            @Override
            public Object next() {
                return elements.iterator().next();
            }
        });

        arg2.setValue("test4");

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatchTrue() {
        List list1 = Arrays.asList("test1", "test2", "test3");
        List list2 = Arrays.asList("test2", "test3", "test4");

        arg1.setValue(new Iterator() {
            private List elements = list1;

            @Override
            public boolean hasNext() {
                return elements.iterator().hasNext();
            }

            @Override
            public Object next() {
                return elements.iterator().next();
            }
        });

        arg2.setValue(new Iterator() {
            private List elements = list2;

            @Override
            public boolean hasNext() {
                return elements.iterator().hasNext();
            }

            @Override
            public Object next() {
                return elements.iterator().next();
            }
        });

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatchFalse() {
        List list1 = Arrays.asList("test1", "test2", "test3");
        List list2 = Arrays.asList("test1", "test4", "test5");

        arg1.setValue(new Iterator() {
            private List elements = list1;

            @Override
            public boolean hasNext() {
                return elements.iterator().hasNext();
            }

            @Override
            public Object next() {
                return elements.iterator().next();
            }
        });

        arg2.setValue(new Iterator() {
            private List elements = list2;

            @Override
            public boolean hasNext() {
                return elements.iterator().hasNext();
            }

            @Override
            public Object next() {
                return elements.iterator().next();
            }
        });

        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualPointerToSameObject() {
        Pointer pointer1 = new Pointer(new Object());
        Pointer pointer2 = new Pointer(new Object());

        arg1.setValue(pointer1);
        arg2.setValue(pointer2);

        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualPointerToDifferentObjects() {
        Pointer pointer1 = new Pointer(new Object());
        Pointer pointer2 = new Pointer(new Object());
        pointer1.setValue(new Object());
        pointer2.setValue(new Object());

        arg1.setValue(pointer1);
        arg2.setValue(pointer2);

        assertFalse(compare.evaluateToBoolean(context));
    }

}
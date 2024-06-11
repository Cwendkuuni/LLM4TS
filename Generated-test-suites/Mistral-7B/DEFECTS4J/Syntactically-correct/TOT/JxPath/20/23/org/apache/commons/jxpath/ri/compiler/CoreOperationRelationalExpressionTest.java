package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IteratorUtil;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(
                new LiteralExpression(5.0), new LiteralExpression(3.0));
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return IteratorUtil.emptyIterator();
            }
        });
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        SimpleNode left = new SimpleNode(5.0);
        SimpleNode right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SimpleNode("not_a_number");
        right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression. computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(
                new LiteralExpression(5.0), new LiteralExpression(3.0));
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return IteratorUtil.emptyIterator();
            }
        });
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        SimpleNode left = new SimpleNode(5.0);
        SimpleNode right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SimpleNode("not_a_number");
        right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SimpleNode(3.0);
        right = new SimpleNode(5.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(
                new LiteralExpression(3.0), new LiteralExpression(5.0));
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return IteratorUtil.emptyIterator();
            }
        });
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        SimpleNode left = new SimpleNode(5.0);
        SimpleNode right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new SimpleNode(3.0);
        right = new SimpleNode(5.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new SimpleNode("not_a_number");
        right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new SimpleNode(3.0);
        right = new SimpleNode("not_a_number");
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(
                new LiteralExpression(3.0), new LiteralExpression(5.0));
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return IteratorUtil.emptyIterator();
            }
        });
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        SimpleNode left = new SimpleNode(5.0);
        SimpleNode right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new SimpleNode(3.0);
        right = new SimpleNode(5.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SimpleNode("not_a_number");
        right = new SimpleNode(3.0);
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SimpleNode(3.0);
        right = new SimpleNode("not_a_number");
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return IteratorUtil.next(getValues());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    private Object[] getValues() {
                        Object[] values = new Object[2];
                        values[0] = left;
                        values[1] = right;
                        return values;
                    }
                };
            }
        });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}
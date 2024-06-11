import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationArg;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.Iter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Object left = new Integer(5);
        Object right = new Integer(3);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.GT),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(left)})),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThan_False() {
        Object left = new Integer(3);
        Object right = new Integer(5);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.GT),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(left)),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThan() {
        Object left = new Integer(3);
        Object right = new Integer(5);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.LT),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(left)),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThan_True() {
        Object left = new Integer(5);
        Object right = new Integer(3);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.LT),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(left)),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Object left = new Integer(5);
        Object right = new Integer(5);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.GTE),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(left)),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanOrEqual_False() {
        Object left = new Integer(3);
        Object right = new Integer(5);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.GTE),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(left)),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanOrEqual() {
        Object left = new Integer(3);
        Object right = new Integer(5);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.LTE),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(left)),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanOrEqual_True() {
        Object left = new Integer(5);
        Object right = new Integer(5);
        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.LTE),
                new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(new CoreOperationArg(left)),
                        new CoreOperationArg(new CoreOperationArg(right))})});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return new Iterator() {
                    private List list = new LinkedList(Arrays.asList(left, right));

                    @Override
                    public boolean hasNext() {
                        return list.size() > 0;
                    }

                    @Override
                    public Object next() {
                        return list.remove(0);
                    }

                    @Override
                    public void remove() {
                        list.remove(0);
                    }
                };
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testIteratorsWithSameElements() {
        Object left = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        Object right = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };

        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.EQ),
                new CoreOperationArg(new CoreOperationArg(left)),
                new CoreOperationArg(new CoreOperationArg(right))});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return null;
            }
        });

        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = AssertionError.class)
    public void testIteratorsWithDifferentElements() {
        Object left = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        Object right = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };

        CoreOperation op = new CoreOperationRelationalExpression(new CoreOperationArg[]{
                new CoreOperationArg(CoreOperation.EQ),
                new CoreOperationArg(new CoreOperationArg(left)),
                new CoreOperationArg(new CoreOperationArg(right))});

        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet() {
            @Override
            public Iterator iterate() {
                return null;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullArgs() {
        new CoreOperationRelationalExpression(new CoreOperationArg[]{
                null,
                new CoreOperationArg(new CoreOperationArg(new CoreOperation[]{new CoreOperationArg(null)}))});
    }
}
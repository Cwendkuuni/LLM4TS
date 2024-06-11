package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.INFOSET_TYPE;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner)
@PrepareForTest(CoreOperationGreaterThanOrEqual.class)
public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;
    private EvalContext context;
    private InfoSet infoSet;
    private NodeInfo nodeInfo;

    @Before
    public void setUp() {
        AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
        Class<?> clazz = classLoader.findClass(CoreOperationGreaterThanOrEqual.class.getName());
        operation = (CoreOperationGreaterThanOrEqual) ReflectionUtils.newInstance(clazz);

        context = ReflectionUtils.getFieldValue(EvalContext.class, CoreOperationGreaterThanOrEqual.class.getField("context"));
        context.setInfoSet(infoSet = new InfoSet(new INFOSET_TYPE() {
            @Override
            public INFOSET_TYPE getType() {
                return INFOSET_TYPE.DOCUMENT;
            }
        }));
        context.setCurrentNode(nodeInfo = new NodeInfo(new Object(), null, null, null, null, null, null, null));
    }

    @Test
    public void testComputeValueWithTwoDoubleValuesGreaterThanOrEqual() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(Double.class, 5.0);
        Expression arg2 = ReflectionUtils.newInstance(Double.class, 5.0);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithTwoDoubleValuesNotGreaterThanOrEqual() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(Double.class, 5.0);
        Expression arg2 = ReflectionUtils.newInstance(Double.class, 6.0);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithTwoDoubleValuesAndOneNullValue() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(Double.class, 5.0);
        Expression arg2 = ReflectionUtils.newInstance(Double.class, null);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithTwoIntegerValuesGreaterThanOrEqual() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(Integer.class, 5);
        Expression arg2 = ReflectionUtils.newInstance(Integer.class, 5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithTwoIntegerValuesNotGreaterThanOrEqual() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(Integer.class, 5);
        Expression arg2 = ReflectionUtils.newInstance(Integer.class, 6);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithTwoStringValuesGreaterThanOrEqual() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(String.class, "5");
        Expression arg2 = ReflectionUtils.newInstance(String.class, "5");
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithTwoStringValuesNotGreaterThanOrEqual() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(String.class, "5");
        Expression arg2 = ReflectionUtils.newInstance(String.class, "6");
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithMixedTypes() {
        // given
        Expression arg1 = ReflectionUtils.newInstance(Double.class, 5.0);
        Expression arg2 = ReflectionUtils.newInstance(Integer.class, 5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // when
        Object result = operation.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        // given
        operation = new CoreOperationGreaterThanOrEqual(ReflectionUtils.newInstance(Double.class, 5.0), ReflectionUtils.newInstance(Double.class, 5.0));

        // when
        String symbol = operation.getSymbol();

        // then
        assertEquals(">=", symbol);
    }
}
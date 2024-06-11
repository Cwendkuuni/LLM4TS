package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.FunctionUtils;
import org.apache.commons.math3.optim.nonlinear.vector.linear.AffineFunction;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;
    private FunctionUtils functionUtils;
    private AffineFunction affineFunction;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
        functionUtils = new FunctionUtils();
        affineFunction = new AffineFunction(new double[]{-1, 1}, new double[]{0});
    }

    @Test
    public void testConstructorDefault() {
        MultiDirectional optimizer = new MultiDirectional();
        assertNotNull(optimizer);
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    @Test
    public void testConstructorCustom() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        assertNotNull(optimizer);
        assertEquals(3.0, optimizer.khi, 0.001);
        assertEquals(0.8, optimizer.gamma, 0.001);
    }

    @Test
    public void testIterateSimplexWithOnePoint() throws Exception {
        double[] p0 = {1.0};
        RealPointValuePair initialPoint = new RealPointValuePair(p0, Double.NaN, false);
        PointValuePairCollection pvs = new PointValuePairCollection();
        multiDirectional.setMaxIterations(1);
        multiDirectional.minimize(affineFunction, new SimpleBounds(new double[]{-1, 1}), GoalType.MINIMIZE, initialPoint, pvs, null);
        assertEquals(1.0, pvs.getMinimum().getPoint()[0], 0.001);
    }

    @Test
    public void testIterateSimplexWithMultiplePoints() throws Exception {
        double[] p0 = {1.0, 1.0};
        RealPointValuePair initialPoint = new RealPointValuePair(p0, Double.NaN, false);
        PointValuePairCollection pvs = new PointValuePairCollection();
        multiDirectional.setMaxIterations(3);
        multiDirectional.minimize(affineFunction, new SimpleBounds(new double[]{-2, 2}), GoalType.MINIMIZE, initialPoint, pvs, null);
        PointValuePair expectedMinimum = new PointValuePair(new double[]{-1.5, -1.5}, 0.0);
        assertEquals(expectedMinimum.getPoint()[0], pvs.getMinimum().getPoint()[0], 0.001);
        assertEquals(expectedMinimum.getPoint()[1], pvs.getMinimum().getPoint()[1], 0.001);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testIterateSimplexWithUnboundedDomain() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.minimize(affineFunction, new SimpleBounds(null), GoalType.MINIMIZE, null, null, null);
    }

}
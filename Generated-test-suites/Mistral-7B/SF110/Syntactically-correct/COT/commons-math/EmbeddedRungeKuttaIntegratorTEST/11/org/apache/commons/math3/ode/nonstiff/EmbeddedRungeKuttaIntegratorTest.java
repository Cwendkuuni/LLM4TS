package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.ode.util.DoubleField;
import org.apache.commons.math3.ode.util.Unmodifiable;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ABSOLUTE_TOLERANCE = 1e-9;
    private static final double RELATIVE_TOLERANCE = 1e-6;

    private StepHandler<ExpandableStatefulODE> stepHandler;
    private ExpandableStatefulODE equations;

    @Before
    public void setup() throws Exception {
        this.equations = ODETestUtils.createTestEquations();
        this.stepHandler = new UnmodifiableStepHandler();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorFactory(
                "RKF45", true, new double[]{0.2369268850566713, 0.679140558575931, 0.2758620002886136, 1.2630010836046923},
                new double[][]{{-0.2548295447972266, 0.4531880263286687, -0.4531880263286687, 1.0},
                        {0.0, 1.0, 0.0, 0.0},
                        {0.0, 0.0, 1.0, 0.0},
                        {0.5555555555555556, 0.0, 0.0, 0.0}},
                new double[]{0.2795877464549278, 0.0, 0.0, 0.0},
                2.0, new DoubleField("safety", 0.9), new DoubleField("minReduction", 0.2), new DoubleField("maxGrowth", 10.0),
                new DoubleField("vecAbsoluteTolerance", new double[]{ABSOLUTE_TOLERANCE, ABSOLUTE_TOLERANCE}, null),
                new DoubleField("vecRelativeTolerance", new double[]{RELATIVE_TOLERANCE, RELATIVE_TOLERANCE}, null),
                new RungeKuttaStepInterpolator());
        integrator.setMinStep(1e-8);
        integrator.setMaxStep(1e3);
        this.stepHandler.setIntegrator(integrator);
    }

    @Test
    public void testGetOrder() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", true, new double[]{0.2369268850566713, 0.679140558575931, 0.2758620002886136, 1.2630010836046923},
                new double[][]{{-0.2548295447972266, 0.4531880263286687, -0.4531880263286687, 1.0},
                        {0.0, 1.0, 0.0, 0.0},
                        {0.0, 0.0, 1.0, 0.0},
                        {0.5555555555555556, 0.0, 0.0, 0.0}},
                new double[]{0.2795877464549278, 0.0, 0.0, 0.0},
                2.0, new DoubleField("safety", 0.9), new DoubleField("minReduction", 0.2), new DoubleField("maxGrowth", 10.0),
                new DoubleField("vecAbsoluteTolerance", new double[]{ABSOLUTE_TOLERANCE, ABSOLUTE_TOLERANCE}, null),
                new DoubleField("vecRelativeTolerance", new double[]{RELATIVE_TOLERANCE, RELATIVE_TOLERANCE}, null),
                new RungeKuttaStepInterpolator());
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", true, new double[]{0.2369268850566713, 0.679140558575931, 0.2758620002886136, 1.2630010836046923},
                new double[][]{{-0.2548295447972266, 0.4531880263286687, -0.4531880263286687, 1.0},
                        {0.0, 1.0, 0.0, 0.0},
                        {0.0, 0.0, 1.0, 0.0},
                        {0.5555555555555556, 0.0, 0.0, 0.0}},
                new double[]{0.2795877464549278, 0.0, 0.0, 0.0},
                2.0, new DoubleField("safety", 0.9), new DoubleField("minReduction", 0.2), new DoubleField("maxGrowth", 10.0),
                new DoubleField("vecAbsoluteTolerance", new double[]{ABSOLUTE_TOLERANCE, ABSOLUTE_TOLERANCE}, null),
                new DoubleField("vecRelativeTolerance", new double[]{RELATIVE_TOLERANCE, RELATIVE_TOLERANCE}, null),
                new RungeKuttaStepInterpolator());
        assertEquals(0.9, integrator.getSafety(), 1e-6);
    }

    @Test
    public void testSetSafety() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", true, new double[]{0.2369268850566713, 0.679140558575931, 0.2758620002886136, 1.2630010836046923},
                new double[][]{{-0.2548295447972266, 0.4531880263286687, -0.4531880263286687, 1.0},
                        {0.0, 1.0, 0.0, 0.0},
                        {0.0, 0.0, 1.0, 0.0},
                        {0.5555555555555556, 0.0, 0.0, 0.0}},
                new double[]{0.2795877464549278, 0.0, 0.0, 0.0},
                2.0, new DoubleField("safety", 0.9), new DoubleField("minReduction", 0.2), new DoubleField("maxGrowth", 10.0),
                new DoubleField("vecAbsoluteTolerance", new double[]{ABSOLUTE_TOLERANCE, ABSOLUTE_TOLERANCE}, null),
                new DoubleField("vecRelativeTolerance", new double[]{RELATIVE_TOLERANCE, RELATIVE_TOLERANCE}, null),
                new RungeKuttaStepInterpolator());
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 1e-6);
    }

    @Test
    public void testIntegrateWithAbsoluteTolerance() throws Exception {
        ExpandableStatefulODE equations = this.equations.clone();
        DoubleField y0 = new DoubleField("y0", new double[]{0.1, 0.1});
        DoubleField expected = new DoubleField("expected", new double[]{1.2345, 6.7890});
        equations.addState(y0);
        DoubleField result = new DoubleField("result", new double[y0.getDimension()]);
        integrator.initialize(equations, 0.0);
        integrator.integrate(equations, 1.0);
        assertEquals(expected.getValue(), result.getValue(), Precision.safeEqual(expected.getAbsoluteTolerance(), result.getAbsoluteTolerance()));
    }

    @Test
    public void testIntegrateWithRelativeTolerance() throws Exception {
        ExpandableStatefulODE equations = this.equations.clone();
        DoubleField y0 = new DoubleField("y0", new double[]{0.1, 0.1});
        DoubleField expected = new DoubleField("expected", new double[]{1.2345, 6.7890});
        equations.addState(y0);
        DoubleField result = new DoubleField("result", new double[y0.getDimension()]);
        integrator.initialize(equations, 0.0);
        integrator.integrate(equations, 1.0);
        double maxError = 1.0;
        for (int i = 0; i < result.getDimension(); i++) {
            double error = Precision.safeSubtract(expected.getValue()[i], result.getValue()[i], Precision.safeMax(Precision.safeAbs(expected.getValue()[i]), Precision.safeAbs(result.getValue()[i])) * RELATIVE_TOLERANCE);
            if (Math.abs(error) > maxError) {
                maxError = Math.abs(error);
            }
        }
        assertTrue(maxError <= RELATIVE_TOLERANCE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws Exception {
        ExpandableStatefulODE equations = this.equations.clone();
        equations.setParameter(0, 0.0);
        DoubleField y0 = new DoubleField("y0", new double[]{0.1, 0.1});
        equations.addState(y0);
        integrator.initialize(equations, 0.0);
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() throws Exception {
        ExpandableStatefulODE equations = this.equations.clone();
        equations.setParameter(0, new double[]{1.0, 2.0});
        DoubleField y0 = new DoubleField("y0", new double[]{0.1, 0.1});
        equations.addState(y0);
        integrator.initialize(equations, 0.0);
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        ExpandableStatefulODE equations = this.equations.clone();
        DoubleField y0 = new DoubleField("y0", new double[]{0.1, 0.1});
        equations.addState(y0);
        integrator.initialize(equations, 0.0);
        integrator.integrate(equations, 1.0, new double[]{1.0e10, 1.0e10});
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws Exception {
        ExpandableStatefulODE equations = this.equations.clone();
        DoubleField y0 = new DoubleField("y0", new double[]{0.1, 0.1});
        equations.addState(y0);
        integrator.initialize(equations, 1.0);
        integrator.integrate(equations, 0.0);
    }
}
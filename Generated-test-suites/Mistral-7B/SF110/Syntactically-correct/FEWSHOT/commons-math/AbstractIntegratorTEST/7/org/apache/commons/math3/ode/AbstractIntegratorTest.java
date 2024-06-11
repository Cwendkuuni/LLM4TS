package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] expectedY;

    @Parameterized.Parameters(name = "{index}: testIntegrate({0}, {1}, {2}, {3}, {4})")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new MyFirstOrderDEs(), 0.0, new double[]{1.0, 1.0}, 1.0, new double[]{1.2, 1.4}},
                {new MyFirstOrderDEs(), 0.0, new double[]{2.0, 2.0}, 2.0, new double[]{3.2, 3.4}},
                {new MyFirstOrderDEs(), 0.0, new double[]{3.0, 3.0}, 3.0, new double[]{4.2, 4.4}},
                {new MyFirstOrderDEs(), 1.0, new double[]{1.2, 1.4}, 1.1, new double[]{1.3, 1.5}},
                {new MyFirstOrderDEs(), 1.0, new double[]{2.3, 2.5}, 1.1, new double[]{2.5, 2.7}},
                {new MyFirstOrderDEs(), 1.0, new double[]{3.3, 3.5}, 1.1, new double[]{3.5, 3.7}},
                {new MyOtherFirstOrderDEs(), 0.0, new double[]{1.0, 1.0}, 1.0, new double[]{1.3, 1.6}},
                {new MyOtherFirstOrderDEs(), 0.0, new double[]{2.0, 2.0}, 2.0, new double[]{2.3, 2.6}},
                {new MyOtherFirstOrderDEs(), 0.0, new double[]{3.0, 3.0}, 3.0, new double[]{3.3, 3.6}},
                {new MyOtherFirstOrderDEs(), 1.0, new double[]{1.3, 1.6}, 1.1, new double[]{1.4, 1.8}},
                {new MyOtherFirstOrderDEs(), 1.0, new double[]{2.3, 2.6}, 1.1, new double[]{2.4, 2.9}},
                {new MyOtherFirstOrderDEs(), 1.0, new double[]{3.3, 3.6}, 1.1, new double[]{3.4, 3.9}},
        });
    }

    public AbstractIntegratorTest(ExpandableStatefulODE equations, double t0, double[] y0, double t, double[] expectedY) {
        this.equations = equations;
        this.t0 = t0;
        this.y0 = y0;
        this.t = t;
        this.expectedY = expectedY;
        this.integrator = new MyAbstractIntegrator("TestIntegrator");
        integrator.addStepHandler(new MyStepHandler());
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        integrator.initIntegration(t0, y0, t);
        integrator.setExpandable(equations);
        double y[] = new double[equations.getDimension()];
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, result, 1e-10);
        assertArrayEquals(expectedY, y, 1e-10);
    }

    static class MyAbstractIntegrator extends AbstractIntegrator {
        public MyAbstractIntegrator(String name) {
            super(name);
        }

        @Override
        protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // Override the abstract method with a stub
        }
    }

    static class MyStepHandler implements StepHandler {
        @Override
        public void handleStep(InterpolatedState interpolatedState, boolean lastStep) {
            // Stub
        }

        @Override
        public void init(double t, double[] y, double tStep) {
            // Stub
        }
    }

    static class MyFirstOrderDEs extends ExpandableStatefulODE {
        public MyFirstOrderDEs() {
            super(2, new FirstOrderDifferentialEquations() {
                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    yDot[0] = y[0] * 0.1;
                    yDot[1] = y[1] * 0.1 + 1.0;
                }
            });
        }
    }

    static class MyOtherFirstOrderDEs extends ExpandableStatefulODE {
        public MyOtherFirstOrderDEs() {
            super(2, new FirstOrderDifferentialEquations() {
                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    yDot[0] = y[0] * 0.2;
                    yDot[1] = y[1] * 0.2 + 2.0;
                }
            });
        }
    }

}
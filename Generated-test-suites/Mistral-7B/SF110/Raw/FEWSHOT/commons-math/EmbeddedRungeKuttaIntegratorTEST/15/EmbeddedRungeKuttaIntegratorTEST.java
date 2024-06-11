package org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator; // Make sure to change the package name according to the actual package in your project

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.EmbeddedRungeKuttaType;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.StepControl;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.StepStatus;
import org.apache.commons.math3.util.Precision;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

    private final EmbeddedRungeKuttaMethod method;
    private final double[] c;
    private final double[][] a;
    private final double[] b;
    private final RungeKuttaStepInterpolator prototype;
    private final double minStep;
    private final double maxStep;
    private final double scalAbsoluteTolerance;
    private final double scalRelativeTolerance;
    private final boolean fsal;
    private final double minReduction;
    private final double maxGrowth;

    public EmbeddedRungeKuttaIntegratorTest(EmbeddedRungeKuttaMethod method, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance, boolean fsal, double minReduction, double maxGrowth) {
        this.method = method;
        this.c = c;
        this.a = a;
        this.b = b;
        this.prototype = prototype;
        this.minStep = minStep;
        this.maxStep = maxStep;
        this.scalAbsoluteTolerance = scalAbsoluteTolerance;
        this.scalRelativeTolerance = scalRelativeTolerance;
        this.fsal = fsal;
        this.minReduction = minReduction;
        this.maxGrowth = maxGrowth;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {EmbeddedRungeKuttaMethod.RK4, new double[]{1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0}, new double[][]{
                        {1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0},
                        {1.0 / 6.0, 1.2 / 6.0, 1.2 / 6.0, 1.0 / 2.0},
                        {1.0 / 6.0, 1.2 / 6.0, 1.1 / 6.0, 1.2 / 6.0},
                        {1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0}},
                new double[]{1.0}, new double[]{1.0}, new double[]{1.0}, new RungeKuttaStepInterpolator(StepControl.ADAPTIVE), 1.0e-8, 1.0e-8, false, 0.2, 10.0},

                // Add more test cases for other methods, coefficients, and tolerances as needed
        };
        return Arrays.asList(data);
    }

    @Test
    public void testInstance() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(method.name(), fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        assertNotNull(integrator);
        assertEquals(method.name(), integrator.getName());
        assertEquals(fsal, integrator.getFullySelectiveAdaptive(false));
    }

    @Test
    public void testGetOrder() {
        assertEquals(method.getOrder(), integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 1e-6);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 1e-6);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 1e-6);
    }

    @Test
    public void testIntegrate1() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return a[0].length;
            }

            @Override
            public int getPrimaryDimension() {
                return a[0].length;
            }

            @Override
            public int getSecondaryDimension() {
                return 0;
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getCompleteState() {
                return new double[a[0].length];
            }

            @Override
            public void setTime(double time) {
                // No-op
            }

            @Override
            public void setCompleteState(double[] state) {
                // No-op
            }

            @Override
            public void setPrimaryMapper(double[] mapper) {
                // No-op
            }

            @Override
            public void setSecondaryMappers(double[][] maps) {
                // No-op
            }

            @Override
            public void initialize(double t, double[] y, double[] dy) {
                Arrays.fill(y, 0.0);
                Arrays.fill(dy, 1.0);
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                System.arraycopy(y, 0, dy, 0, a[0].length);
            }
        };

        integrator.integrate(equations, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullODE() throws Exception {
        integrator.integrate(null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNegativeTime() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            // Implement the ODE as needed
        };

        integrator.integrate(equations, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateZeroStepSize() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            // Implement the ODE as needed
        };

        integrator.integrate(equations, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNaNStepSize() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            // Implement the ODE as needed
        };

        integrator.integrate(equations, Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullInterpolator() throws Exception {
        integrator.integrate(new ExpandableStatefulODE() {
            // Implement the ODE as needed
        }, 0.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateTooSmallStepSize() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            // Implement the ODE as needed
        };

        integrator.integrate(equations, 0.0, 0.1, 0.01);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateMismatchedDimensions() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getPrimaryDimension() {
                return 1;
            }

            @Override
            public int getSecondaryDimension() {
                return 1;
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getCompleteState() {
                return new double[2];
            }

            @Override
            public void setTime(double time) {
                // No-op
            }

            @Override
            public void setCompleteState(double[] state) {
                // No-op
            }

            @Override
            public void initialize(double t, double[] y, double[] dy) {
                Arrays.fill(y, 0.0);
                Arrays.fill(dy, 1.0);
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        integrator.integrate(equations, 0.0, 2.0, 0.1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxCountExceeded() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            // Implement the ODE as needed
        };

        integrator.setMaxSteps(1);
        integrator.integrate(equations, 0.0, 2.0, 0.1);
    }

    @Test
    public void testIntegrate() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return a[0].length;
            }

            @Override
            public int getPrimaryDimension() {
                return a[0].length;
            }

            @Override
            public int getSecondaryDimension() {
                return 0;
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getCompleteState() {
                return new double[a[0].length];
            }

            @Override
            public void setTime(double time) {
                // No-op
            }

            @Override
            public void setCompleteState(double[] state) {
                // No-op
            }

            @Override
            public void initialize(double t, double[] y, double[] dy) {
                Arrays.fill(y, 0.0);
                Arrays.fill(dy, 1.0);
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                System.arraycopy(y, 0, dy, 0, a[0].length);
            }
        };

        double[] y0 = new double[a[0].length];
        double[] y1 = new double[a[0].length];
        double[] y2 = new double[a[0].length];
        double[] y3 = new double[a[0].length];
        double[] yDotK = new double[c.length + 1][a[0].length];

        integrator.integrate(equations, 0.0, 2.0, 0.1);

        StepStatus status = integrator.getStepStatus();
        assertEquals(StepStatus.SUCCESS, status);

        double[] state = equations.getCompleteState();
        assertArrayEquals(y1, state);

        double[] yTmp = new double[a[0].length];
        double[] yDotTmp = new double[a[0].length];

        StepInterpolator interpolator = integrator.getInterpolator();
        interpolator.reinitialize(integrator, yTmp, yDotK, true, equations.getPrimaryMapper(), equations.getSecondaryMappers());

        double h = integrator.getStepSize();
        double[] p0 = interpolator.getInterpolatedPoint(0.0);
        double[] p1 = interpolator.getInterpolatedPoint(h);
        double[] p2 = interpolator.getInterpolatedPoint(2.0);

        Arrays.sort(p0);
        Arrays.sort(p1);
        Arrays.sort(p2);

        for (int i = 0; i < a[0].length; i++) {
            double error = Precision.compareDouble(y1[i], p1[i], 1e-8);
            assertTrue(error <= 0);
            error = Precision.compareDouble(p1[i], p2[i], 1e-8);
            assertTrue(error <= 0);
            error = Precision.compareDouble(y0[i], p0[i], 1e-8);
            assertTrue(error <= 0);

            double yDotKi = yDotK[1][i];
            double yDotPi = p1[i] - y1[i];
            double yDotPj = p2[i] - y1[i];
            double y2i = y1[i] + h * yDotKi;
            double y2j = y1[i] + h * yDotPi;

            double error2 = Precision.Compare_relative_tolerance(y2i, y2j, 1e-8);
            assertTrue(error2 <= 0);
        }
    }
}
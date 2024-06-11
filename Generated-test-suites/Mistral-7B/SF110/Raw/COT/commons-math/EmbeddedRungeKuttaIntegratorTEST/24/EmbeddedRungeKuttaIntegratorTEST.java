import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.util.Precision.absTolerance;
import static org.apache.commons.math3.util.Precision.delta;
import static org.apache.commons.math3.util.Precision.epsilon;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        // Test the default constructor with given arguments
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        Assert.assertEquals("TestIntegrator", integrator.getName());
        Assert.assertFalse(integrator.isFSAL());
        Assert.assertArrayEquals(new double[]{0.3, 0.2, 0.1}, integrator.getC(), delta(0.0));
        Assert.assertArrayEquals(new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, integrator.getA(), delta(0.0));
        Assert.assertArrayEquals(new double[]{0.3, 0}, integrator.getB(), delta(0.0));
        Assert.assertNotNull(integrator.getPrototype());
        Assert.assertEquals(-1.0 / integrator.getOrder(), integrator.getExp(), epsilon(0.0));
        Assert.assertEquals(0.9, integrator.getSafety(), epsilon(0.0));
        Assert.assertEquals(0.2, integrator.getMinReduction(), epsilon(0.0));
        Assert.assertEquals(10.0, integrator.getMaxGrowth(), epsilon(0.0));
    }

    @Test
    public void testGetOrder() {
        // Test the getOrder() abstract method
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(1, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        int order = integrator.getOrder();
        Assert.assertEquals(1, order);
    }

    @Test
    public void testIntegrateSimpleCase() {
        // Test the integrate() method with a simple case (RK4)
        double[] initialValues = new double[]{1.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public String getName() {
                return "TestEquations";
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        integrator.integrate(equations, 1.0);

        double[] expectedValues = new double[]{3.0 / 2.0, 1.0 / 2.0};
        double[] actualValues = equations.getCompleteState();

        Assert.assertArrayEquals(expectedValues, actualValues, delta(0.0));
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateNumberIsTooSmallException() {
        // Test the NumberIsTooSmallException when the step size is too small
        double[] initialValues = new double[]{1.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public String getName() {
                return "TestEquations";
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        integrator.setMinStep(1e-10);
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatchException() {
        // Test the DimensionMismatchException when the equations have a different dimension
        double[] initialValues = new double[]{1.0, 0.0, 1.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialValues.length - 1;
            }

            @Override
            public String getName() {
                return "TestEquations";
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
                yPrime[2] = y[1] * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxCountExceededException() {
        // Test the MaxCountExceededException when the maximum number of steps is reached
        double[] initialValues = new double[]{1.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public String getName() {
                return "TestEquations";
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0)) {
            @Override
            public int getMaxSteps() {
                return 1;
            }
        };
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketingException() {
        // Test the NoBracketingException when the initial step size is too large
        double[] initialValues = new double[]{1.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public String getName() {
                return "TestEquations";
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0)) {
            @Override
            public double getMinStep() {
                return 10.0;
            }
        };
        integrator.integrate(equations, 1.0);
    }

    @Test
    public void testSetSafety() {
        // Test the setSafety() method
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(1, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        integrator.setSafety(0.8);
        Assert.assertEquals(0.8, integrator.getSafety(), epsilon(0.0));
    }

    @Test
    public void testSetMinReduction() {
        // Test the setMinReduction() method
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(1, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        integrator.setMinReduction(0.5);
        Assert.assertEquals(0.5, integrator.getMinReduction(), epsilon(0.0));
    }

    @Test
    public void testSetMaxGrowth() {
        // Test the setMaxGrowth() method
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator(1, new double[]{0.3, 0.2, 0.1}, new double[][]{{0.776745966192321, 0.3733729830961602}, {0, 0.9}, {0.658553692318638, 0.0, 0.31622776601683796}}, new double[]{0.3, 0}, new RungeKuttaStepInterpolator(1.0));
        integrator.setMaxGrowth(5.0);
        Assert.assertEquals(5.0, integrator.getMaxGrowth(), epsilon(0.0));
    }
}

class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    public TestEmbeddedRungeKuttaIntegrator(int order, double[] c, double[][] a, double[] b, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super("TestIntegrator", false, c, a, b, new RungeKuttaStepInterpolator(1.0), 0.01, 0.02, scalAbsoluteTolerance, scalRelativeTolerance);
        this.setOrder(order);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y, double[] yDot, double stepSize) {
        // Implement the estimateError() method for testing purposes
        double error = 0.0;
        for (int i = 0; i < y.length; i++) {
            error += absTolerance(y[i] - yDot[i], 1e-10);
        }
        return error;
    }
}
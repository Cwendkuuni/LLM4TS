import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractODEEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no-op
            }
        };
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no-op
            }
        });
        Assert.assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new AbstractODEEvent() {
            @Override
            public double getEventTime() {
                return 1.0;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                // no-op
            }

            @Override
            public boolean stop() {
                return false;
            }

            @Override
            public void reinitializeBegin(AbstractStepInterpolator interpolator) {
                // no-op
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }
        }, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        Assert.assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[1]);

        double[] result = integrator.getStepHandlers().get(0).getInterpolator().getInterpolatedState();
        Assert.assertEquals(0.0, result[0], 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithInvalidStepSize() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[1], 0.0, 1.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithInvalidInitialCondition() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.integrate(equations, 0.0, new double[]{2.0}, 1.0, new double[1]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidTargetCondition() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.integrate(equations, 0.0, new double[]{0.0}, null, new double[1]);
    }

    @Test
    public void testResetEvaluations() {
        integrator.resetEvaluations();
        Assert.assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());
    }

}
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Precision;

import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractIntegratorTest.AbstractIntegratorTestHelper.createDummyEventHandler;
import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractIntegratorTest.AbstractIntegratorTestHelper.createDummyStepHandler;
import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractIntegratorTest.AbstractIntegratorTestHelper.createEquations;
import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractIntegratorTest.AbstractIntegratorTestHelper.createInitialValues;
import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractIntegratorTest.AbstractIntegratorTestHelper.createIntegrator;
import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractIntegratorTest.AbstractIntegratorTestHelper.createInterpolator;
import static org.apache.commons.math3.ode.AbstractIntegrator.AbstractIntegratorTest.AbstractIntegratorTestHelper.createStatefulODE;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() throws Exception {
        final String name = "TestIntegrator";
        AbstractIntegrator integrator = createIntegrator(name);

        // Test adding and getting step handlers
        integrator.addStepHandler(createDummyStepHandler());
        Assert.assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());

        // Test adding and getting event handlers
        integrator.addEventHandler(createDummyEventHandler(), 0.1, 1e-9, 10);
        Assert.assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());

        // Test setting and getting max evaluations
        integrator.setMaxEvaluations(100);
        Assert.assertEquals(100, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        Assert.assertEquals(-1, integrator.getMaxEvaluations());

        // Test initialization
        ExpandableStatefulODE equations = createEquations(1, 1);
        integrator.initIntegration(0.0, createInitialValues(1), 1.0);
        Assert.assertEquals(0.0, integrator.getCurrentStepStart(), 1e-12);
        Assert.assertNull(integrator.getCurrentSignedStepsize());
        Assert.assertFalse(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred);

        // Test integration
        integrator.integrate(equations, 1.0);
        Assert.assertTrue(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred);
        Assert.assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        Assert.assertNotNull(integrator.getCurrentSignedStepsize());

        // Test evaluation of derivatives
        double[] y = new double[1];
        double[] yDot = new double[1];
        integrator.computeDerivatives(1.0, y, yDot);
        Assert.assertArrayEquals(yDot, new double[]{1.0}, 1e-12);

        // Test reset of integrator state
        integrator.reset();
        Assert.assertEquals(0.0, integrator.getCurrentStepStart(), 1e-12);
        Assert.assertNull(integrator.getCurrentSignedStepsize());
        Assert.assertFalse(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred);

        // Test integration with events
        integrator.addEventHandler(createDummyEventHandler(), 0.5, 1e-9, 10);
        integrator.integrate(equations, 1.0);
        Assert.assertTrue(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred);
        integrator.reset();
        integrator.addEventHandler(createDummyEventHandler(), 0.5, 1e-9, 10);
        integrator.integrate(equations, 2.0);
        Assert.assertTrue(integrator.isLastStep());
        Assert.assertTrue(integrator.resetOccurred);

        // Test integration with step handlers
        integrator.addStepHandler(createDummyStepHandler());
        integrator.integrate(equations, 1.0);
        integrator.reset();
        integrator.addStepHandler(createDummyStepHandler());
        integrator.integrate(equations, 2.0);
        integrator.reset();
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.integrate(equations, 3.0);
        integrator.reset();
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.integrate(equations, 4.0);
        integrator.reset();
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.integrate(equations, 5.0);
        integrator.reset();
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.addStepHandler(createDummyStepHandler());
        integrator.integrate(equations, 6.0);

        // Test integration with a too small step size
        integrator = createIntegrator(name);
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 1.0);
        integrator.reset();
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 1e-15);
        integrator.reset();
        integrator.integrate(equations, 1e-12);
        integrator.reset();
        integrator.integrate(equations, 1e-9);
        integrator.reset();
        integrator.integrate(equations, 1e-6);
        integrator.reset();
        integrator.integrate(equations, 1e-3);
        integrator.reset();
        integrator.integrate(equations, 1.0);
        integrator.reset();

        // Test integration with a too large step size
        integrator = createIntegrator(name);
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 1.0);
        integrator.reset();
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 1000.0);
        integrator.reset();

        // Test integration without equations set
        integrator = createIntegrator(name);
        integrator.integrate(null, 1.0);
        integrator.reset();

        // Test integration with non-matching dimensions
        integrator = createIntegrator(name);
        equations = createStatefulODE(2, 1);
        integrator.initIntegration(0.0, createInitialValues(2), 1.0);
        integrator.integrate(equations, 1.0);
        integrator.reset();
        integrator.integrate(equations, 1.0);
        integrator.reset();
        integrator.integrate(createEquations(1, 2), 1.0);
        integrator.reset();

        // Test integration with a too small time step
        integrator = createIntegrator(name);
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 1.0 / 1000.0);
        integrator.reset();

        // Test integration with a too large time step
        integrator = createIntegrator(name);
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 1000.0);
        integrator.reset();

        // Test integration with a max number of evaluations
        integrator = createIntegrator(name);
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 1.0);
        integrator.reset();

        // Test integration with a non-bracketing event
        integrator = createIntegrator(name);
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] dydt, double[] yPrime, double[] yDoublePrime) {
                throw new NoBracketingException("This event cannot be bracketed.");
            }

            @Override
            public double getEventTime() {
                return 1.0;
            }

            @Override
            public int getEventMultiplicity() {
                return 0;
            }
        }, 0.1, 1e-9, 10);
        integrator.integrate(equations, 1.0);
        integrator.reset();

    }

    static AbstractIntegrator createIntegrator(String name) {
        return new AbstractIntegrator(name) {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws DimensionMismatchException, NumberIsTooSmallException,
                           MaxCountExceededException, NoBracketingException {
                super.integrate(equations, t);
            }
        };
    }

    static double[] createInitialValues(int dimension) {
        return new double[]{0.0};
    }

    static ExpandableStatefulODE createEquations(int dimension, int stateSize) {
        return new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return dimension;
            }

            @Override
            public int getStateSize() {
                return stateSize;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1.0;
            }
        };
    }

    static AbstractStepInterpolator createInterpolator() {
        return new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return -1.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public void setSoftPreviousTime(double t) {
                // no-op
            }

            @Override
            public void setSoftCurrentTime(double t) {
                // no-op
            }

            @Override
            public void setInterpolatedTime(double t) {
                // no-op
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{0.0};
            }
        };
    }

    static EventHandler createDummyEventHandler() {
        return new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] dydt, double[] yPrime, double[] yDoublePrime) {
                // no-op
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public int getEventMultiplicity() {
                return 0;
            }
        };
    }

    static StepHandler createDummyStepHandler() {
        return new StepHandler() {
            @Override
            public void handleStep(StepHandler handler, boolean isLastStep) {
                // no-op
            }
        };
    }
}
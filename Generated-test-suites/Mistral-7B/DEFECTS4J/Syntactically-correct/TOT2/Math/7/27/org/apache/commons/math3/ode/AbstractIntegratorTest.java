import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        equations = new MockDifferentialEquations();
        integrator.addEventHandler(new MockEventHandler(), 0.5, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
    }

    @Test
    public void testBasicFunctionality() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.integrate(equations, 1.0);
        // check results
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        integrator.setEquations(new ExpandableStatefulODE(new MockDifferentialEquations()) {
            @Override
            public int getDimension() {
                return 3;
            }
        });
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        integrator.setMaxEvaluations(0);
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        integrator.setEquations(new ExpandableStatefulODE(new MockDifferentialEquationsWithEvent()) {
            @Override
            public int getDimension() {
                return 1;
            }
        });
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.integrate(equations, 1.0);
    }

    @Test
    public void testEventHandler() {
        integrator.addEventHandler(new MockEventHandlerWithReset(), 0.5, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.integrate(equations, 1.0);
        // check resetOccurred flag
    }

    @Test
    public void testStepHandlers() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.integrate(equations, 1.0);
        // check if step handler was called
    }

}

class MockDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] * 0.1;
    }

}

class MockDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

    private double eventTime = 0.5;

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] * 0.1;
    }

    public double getEventTime() {
        return eventTime;
    }

}

class MockEventHandler implements EventHandler {

    private double eventTime = 0.5;

    @Override
    public boolean isStateDefined(double t, double[] state) {
        return true;
    }

    @Override
    public boolean isEventActive(double t, double[] state) {
        return Precision.equals(t, eventTime);
    }

    @Override
    public void handleEvent(double t, double[] state, double[] stateTransition) {
        // empty implementation
    }

    @Override
    public void init(double t, double[] state, double tEnd) {
        eventTime = tEnd;
    }

}

class MockEventHandlerWithReset implements EventHandler {

    private boolean resetOccurred = false;

    @Override
    public boolean isStateDefined(double t, double[] state) {
        return true;
    }

    @Override
    public boolean isEventActive(double t, double[] state) {
        return Precision.equals(t, 0.5);
    }

    @Override
    public void handleEvent(double t, double[] state, double[] stateTransition) {
        resetOccurred = true;
    }

    @Override
    public void init(double t, double[] state, double tEnd) {
        // empty implementation
    }

    public boolean getResetOccurred() {
        return resetOccurred;
    }

}

class MockStepHandler implements StepHandler {

    private boolean called = false;

    @Override
    public void handleStep(StepHandler last, boolean lastStep) {
        called = true;
    }

    public boolean isCalled() {
        return called;
    }

}
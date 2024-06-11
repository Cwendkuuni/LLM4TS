import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EmptyEventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double initialTime;
    private double[] initialValues;
    private double targetTime;
    private double[] targetValues;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op integration
            }
        };
        equations = new AbstractStepDoublingIntegrator();
        initialTime = 0.0;
        initialValues = new double[] { 1.0, 2.0 };
        targetTime = 3.0;
        targetValues = new double[] { 3.0, 4.0 };
    }

    /**
     * Test basic functionality of the integrator.
     */
    @Test
    public void testBasicFunctionality() {
        integrator.initIntegration(initialTime, initialValues, targetTime);
        integrator.integrate(equations, initialTime, initialValues, targetTime, targetValues);
        assertArrayEquals(targetValues, integrator.getState(), 0.001);
    }

    /**
     * Test handling of step handlers.
     */
    @Test
    public void testStepHandlers() {
        StepHandler stepHandler = new StepHandler() {
            private int counter = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                counter++;
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.initIntegration(initialTime, initialValues, targetTime);
        integrator.integrate(equations, initialTime, initialValues, targetTime, targetValues);
        assertEquals(2, stepHandler.getCounter());
    }

    /**
     * Test handling of events.
     */
    @Test
    public void testEvents() {
        EmptyEventState eventState = new EmptyEventState();
        integrator.addEventHandler(eventState, 2.0, 1.0e-6, 5);
        integrator.initIntegration(initialTime, initialValues, targetTime);
        integrator.integrate(equations, initialTime, initialValues, targetTime, targetValues);
        assertTrue(eventState.isTriggered());
    }

    /**
     * Test handling of integration exceptions.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                throw new NumberIsTooSmallException(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, 0.0, 0.0);
            }
        };
        integrator.initIntegration(initialTime, initialValues, targetTime);
        integrator.integrate(equations, initialTime, initialValues, targetTime, targetValues);
    }

    /**
     * Test handling of dimension mismatch exception.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op integration
            }

            @Override
            public int getDimension() {
                return 3;
            }
        };
        integrator.initIntegration(initialTime, initialValues, targetTime);
        FirstOrderDifferentialEquations equations = new AbstractDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }
        };
        integrator.integrate(equations, initialTime, initialValues, targetTime, targetValues);
    }

}
package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.StepHandlerAdapter;
import org.apache.commons.math3.ode.event.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator_Constructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        assertEquals("TestIntegrator", integrator.getName());
        assertTrue(integrator.getMaxEvaluations() == -1);
        assertNull(integrator.getExpandable());
    }

    @Test
    public void testAbstractIntegrator_AddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepContext context, int index) {
                // implementation not needed for testing
            }
        };
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAbstractIntegrator_AddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        EventHandler handler = new EventHandlerAdapter() {
            @Override
            public int execute(double time, double[] values) {
                // implementation not needed for testing
                return 0;
            }
        };
        integrator.addEventHandler(handler, 0.1, 1e-4, 10);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractIntegrator_Integrate_DimensionMismatchException() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // implementation not needed for testing
            }
        };
        integrator.integrate(equations, 0.0, new double[]{0.0, 0.0}, 1.0, new double[]{0.0, 0.0});
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testAbstractIntegrator_Integrate_NumberIsTooSmallException() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // implementation not needed for testing
            }
        };
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0e-10, new double[]{0.0});
    }

    @Test(expected = MaxCountExceededException.class)
    public void testAbstractIntegrator_Integrate_MaxCountExceededException() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // implementation not needed for testing
            }
        };
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{0.0});
    }

    @Test(expected = NoBracketingException.class)
    public void testAbstractIntegrator_Integrate_NoBracketingException() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // implementation not needed for testing
            }

            @Override
            public double bracketRoot(double lowerBound, double upperBound, double tolerance) {
                throw new UnsupportedOperationException();
            }
        };
        integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{0.0});
    }

    @Test
    public void testAbstractIntegrator_Integrate() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        };
        double[] initialValues = new double[]{0.0};
        double[] finalValues = new double[]{1.0};
        double[] intermediateValues = new double[]{0.5};
        integrator.integrate(equations, 0.0, initialValues, 0.5, intermediateValues);
        assertArrayEquals(intermediateValues, finalValues);
    }

    @Test
    public void testAbstractIntegrator_AcceptStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };
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
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
        expandableODE.setTime(0.0);
        expandableODE.setPrimaryState(initialValues);
        integrator.initIntegration(0.0, initialValues, 1.0);
        integrator.setExpandable(expandableODE);

        AbstractStepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);
        double stepSize = 0.05;
        double[] y = initialValues.clone();
        double tEnd = 1.0;
        double currentStepStart = 0.0;

        integrator.acceptStep(interpolator, y, null, tEnd);
        double acceptedStep = interpolator.getGlobalCurrentTime();
        assertTrue(FastMath.abs(acceptedStep - currentStepStart) < 1e-6);

        currentStepStart = acceptedStep;
        stepSize *= 2.0;
        integrator.acceptStep(interpolator, y, null, tEnd);
        acceptedStep = interpolator.getGlobalCurrentTime();
        assertTrue(FastMath.abs(acceptedStep - currentStepStart) < stepSize);
    }
}
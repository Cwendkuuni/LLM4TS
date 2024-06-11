package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // no implementation needed
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no implementation needed
            }

            @Override
            public void init(double t, double[] y, double tStep) {
                // no implementation needed
            }
        };

        integrator.addStepHandler(stepHandler);
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertTrue(stepHandlers.contains(stepHandler));
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // no implementation needed
            }
        };

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t, double[] y, double tStep) {
                // no implementation needed
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // no implementation needed
            }
        };

        integrator.addEventHandler(eventHandler, 1.0, 1.0e-6, 10);
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public String getName() {
                return "IntegratorTest";
            }
        };
        assertEquals("IntegratorTest", integrator.getName());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDimensionMismatch() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // no implementation needed
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // no implementation needed
            }
        };

        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[2]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithNumberIsTooSmallException() throws Exception {
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

        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                throw new NumberIsTooSmallException("step size is too small");
            }
        };

        integrator.integrate(equations, 0.0, new double[1], 0.0, new double[1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceededException() throws Exception {
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

        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                throw new MaxCountExceededException("maximum number of evaluations reached");
            }
        };

        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketingException() throws Exception {
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

        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // no implementation needed
            }
        };

        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test
    public void testResetOccurred() throws Exception {
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

        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // no implementation needed
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                super.computeDerivatives(t, y, yDot);
                this.resetOccurred = true;
            }
        };

        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
        assertTrue(integrator.resetOccurred);
    }
}
package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        StepHandler handler1 = mock(StepHandler.class);
        integrator.addStepHandler(handler1);
        StepHandler handler2 = mock(StepHandler.class);
        integrator.addStepHandler(handler2);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegativeNumber() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        // assert initial state
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[]{3.0, 4.0};
        integrator.integrate(new DummyDifferentialEquations(y0.length), t0, y0, t, y);
        // assert integration result
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[]{3.0}; // wrong length
        integrator.integrate(new DummyDifferentialEquations(y0.length), t0, y0, t, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluationsReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        integrator.setMaxEvaluations(0);
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[]{3.0, 4.0};
        integrator.integrate(new DummyDifferentialEquations(y0.length), t0, y0, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        integrator.setBracketingFunction(null);
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[]{3.0, 4.0};
        integrator.integrate(new DummyDifferentialEquations(y0.length), t0, y0, t, y);
    }

    @Test(expected = NullPointerException.class)
    public void testIntegrateNullDifferentialEquations() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[]{3.0, 4.0};
        integrator.integrate(null, t0, y0, t, y);
    }

    @Test(expected = NullPointerException.class)
    public void testIntegrateNullY() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        integrator.integrate(new DummyDifferentialEquations(y0.length), t0, y0, t, null);
    }

    @Test(expected = NullPointerException.class)
    public void testIntegrateNullYDot() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[]{3.0, 4.0};
        integrator.integrate(new DummyDifferentialEquations(y0.length), t0, y0, t, y);
        integrator.computeDerivatives(t, y, null);
    }
}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    private final int dimension;

    public DummyDifferentialEquations(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public int getDimension() {
        return dimension;
    }
}
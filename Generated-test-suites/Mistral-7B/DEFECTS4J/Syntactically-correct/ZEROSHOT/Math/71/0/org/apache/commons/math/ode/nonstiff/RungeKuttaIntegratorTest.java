package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private double startTime;
    private double endTime;
    private double stepSize;

    @Before
    public void setUp() {
        equations = new BesselDifferentialEquations();
        initialValues = new double[]{1.0, 0.0};
        startTime = 0.0;
        endTime = Math.PI;
        stepSize = 0.01;
        integrator = createIntegrator();
    }

    @Test
    public void testIntegrate() throws Exception {
        double[] result = new double[initialValues.length];
        integrator.integrate(equations, startTime, initialValues, endTime, result);
        assertEquals(Math.sin(endTime), result[0], 0.001);
        assertEquals(Math.cos(endTime), result[1], 0.001);
    }

    @Test(expected = DeriveException.class)
    public void testInvalidDerivatives() {
        equations = new InvalidDerivativesDifferentialEquations();
        integrator.integrator(equations, startTime, initialValues, endTime, result);
    }

    @Test(expected = IntegratorException.class)
    public void testStepHandlerException() {
        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(org.apache.commons.math3.ode.sampling.StepContext context, int index) {
                throw new RuntimeException();
            }
        };
        integrator = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.25}, {0.25, 0.25}, {0.5, 0.5}, {0.5, 0.5}},
                new double[]{0.25, 0.25}, new RungeKuttaStepInterpolator(), stepSize) {
            @Override
            public void addStepHandler(StepHandler handler) {
                super.addStepHandler(handler);
            }
        };
        integrator.integrate(equations, startTime, initialValues, endTime, result);
    }

    @Test(expected = NoRootsException.class)
    public void testEvents() throws Exception {
        double[] events = {2.0, 3.0, 4.0};
        integrator = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.25}, {0.25, 0.25}, {0.5, 0.5}, {0.5, 0.5}},
                new double[]{0.25, 0.25}, new RungeKuttaStepInterpolator(), stepSize) {
            @Override
            public void addEventHandler(org.apache.commons.math3.ode.events.Event<?> event) {
                super.addEventHandler(event);
            }
        };
        integrator.addEventHandler(new RelativeEvent(new BesselRelativEventFunction(), 1.0));
        integrator.integrate(equations, startTime, initialValues, endTime, result);
        fail("An event should have been triggered before the end time");
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxSteps() throws Exception {
        double maxSteps = 1000;
        integrator = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.25}, {0.25, 0.25}, {0.5, 0.5}, {0.5, 0.5}},
                new double[]{0.25, 0.25}, new RungeKuttaStepInterpolator(), stepSize) {
            @Override
            public int getMaxSteps() {
                return (int) maxSteps;
            }
        };
        integrator.integrate(equations, startTime, initialValues, endTime, result);
    }

    @Test(expected = IntegratorException.class)
    public void testNullEquations() {
        integrator = createIntegrator();
        try {
            integrator.integrate(null, startTime, initialValues, endTime, result);
        } catch (Exception e) {
            assertInstanceOf(IntegratorException.class, e);
        }
    }

    @Test(expected = IntegratorException.class)
    public void testNullResult() {
        integrator = createIntegrator();
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, null);
        } catch (Exception e) {
            assertInstanceOf(IntegratorException.class, e);
        }
    }

    @Test
    public void testResetEvaluations() {
        integrator = createIntegrator();
        integrator.integrate(equations, startTime, initialValues, endTime, result);
        integrator.resetEvaluations();
        assertFalse(integrator.isEvaluationDone());
    }

    @Test
    public void testSetEquations() {
        integrator = createIntegrator();
        integrator.integrate(equations, startTime, initialValues, endTime, result);
        integrator.setEquations(new BesselDifferentialEquations());
        assertTrue(integrator.isEvaluationDone());
    }

    private RungeKuttaIntegrator createIntegrator() {
        return new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.25}, {0.25, 0.25}, {0.5, 0.5}, {0.5, 0.5}},
                new double[]{0.25, 0.25}, new RungeKuttaStepInterpolator(), stepSize) {
            @Override
            protected boolean requiresDenseOutput() {
                return false;
            }
        };
    }

}

class BesselDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = -y[1];
        yPrime[1] = y[0];
    }

}

class InvalidDerivativesDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new UnsupportedOperationException();
    }

}

class BesselRelativEventFunction implements org.apache.commons.math3.ode.events.Event<Double> {

    @Override
    public Double value(Double t) {
        return Math.sin(t);
    }

    @Override
    public int dimension() {
        return 1;
    }

}
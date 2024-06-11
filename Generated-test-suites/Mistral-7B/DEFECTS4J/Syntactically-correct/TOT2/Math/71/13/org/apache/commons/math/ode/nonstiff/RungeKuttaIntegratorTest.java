package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.testcharts.LorenzSystem;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;

/**
 * Test class for RungeKuttaIntegrator.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}.
     */
    @Test
    public void testConstructor() {
        new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(), 0.1);
        new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.5, 0.5 }, new double[][] { { 0.25, 0.25 }, { 0.25, 0 }, { 0.5, 0.5 }, { 0.5, 0.5 } }, new double[] { 0.25, 0.25, 0.25, 0.25 }, new RungeKuttaStepInterpolator(), 0.1);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() throws Exception {
        final double[] y0 = new double[] { 1.0, 1.0, 1.0 };
        final LorenzSystem equations = new LorenzSystem();
        final RungeKuttaIntegrator rk2 = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(), 0.1);
        final RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.5, 0.5 }, new double[][] { { 0.25, 0.25 }, { 0.25, 0 }, { 0.5, 0.5 }, { 0.5, 0.5 } }, new double[] { 0.25, 0.25, 0.25, 0.25 }, new RungeKuttaStepInterpolator(), 0.1);

        assertEquals(3.564662, rk2.integrate(equations, 0.0, y0, 10.0, new double[3]), 1e-5);
        assertEquals(27.05691, rk4.integrate(equations, 0.0, y0, 10.0, new double[3]), 1e-5);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with custom step handler
     */
    @Test
    public void testIntegrateWithStepHandler() throws Exception {
        final double[] y0 = new double[] { 1.0, 1.0, 1.0 };
        final LorenzSystem equations = new LorenzSystem();
        final RungeKuttaIntegrator rk2 = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(), 0.1);
        final RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.5, 0.5 }, new double[][] { { 0.25, 0.25 }, { 0.25, 0 }, { 0.5, 0.5 }, { 0.5, 0.5 } }, new double[] { 0.25, 0.25, 0.25, 0.25 }, new RungeKuttaStepInterpolator(), 0.1);

        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(int stage, double time, double[] y, double[] yDot, double[] yDotDot) {
                System.out.println("Stage: " + stage + ", Time: " + time + ", Y: " + Arrays.toString(y) + ", YDot: " + Arrays.toString(yDot) + ", YDotDot: " + Arrays.toString(yDotDot));
            }
        };

        rk2.addStepHandler(handler);
        rk4.addStepHandler(handler);

        assertEquals(3.564662, rk2.integrate(equations, 0.0, y0, 10.0, new double[3]), 1e-5);
        assertEquals(27.05691, rk4.integrate(equations, 0.0, y0, 10.0, new double[3]), 1e-5);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with events
     */
    @Test
    public void testIntegrateWithEvents() throws Exception {
        final double[] y0 = new double[] { 1.0, 1.0, 1.0 };
        final LorenzSystem equations = new LorenzSystem();
        final RungeKuttaIntegrator rk2 = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(), 0.1);
        final RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.5, 0.5 }, new double[][] { { 0.25, 0.25 }, { 0.25, 0 }, { 0.5, 0.5 }, { 0.5, 0.5 } }, new double[] { 0.25, 0.25, 0.25, 0.25 }, new RungeKuttaStepInterpolator(), 0.1);

        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new When(new When.Inclusive(10.0), new Event() {
            @Override
            public void handleEvent(double time, double[] state) {
                System.out.println("Event detected at time: " + time + ", State: " + Arrays.toString(state));
            }
        }));

        rk2.setEventsManager(manager);
        rk4.setEventsManager(manager);

        assertEquals(3.564662, rk2.integrate(equations, 0.0, y0, 10.0, new double[3]), 1e-5);
        assertEquals(27.05691, rk4.integrate(equations, 0.0, y0, 10.0, new double[3]), 1e-5);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with exception
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithException() {
        final RungeKuttaIntegrator rk2 = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(), 0.0);
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                throw new UnsupportedOperationException();
            }
        };

        rk2.integrate(equations, 0.0, new double[] { 1.0 }, 10.0, new double[1]);
    }

}
package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.initialvalues.InitialValueProblem;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethods;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.StepControllable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test for RK2 integrator.
     */
    @Test
    public void testRk2() {
        final double[] initialValues = { 1.0, 1.0 };
        final double   start          = 0.0;
        final double   end            = 1.0;

        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {

                @Override
                public int getDimension() {
                    return initialValues.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    yPrime[0] = y[1];
                    yPrime[1] = -y[0];
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RK2", false,
            new double[] { 1.0 / 2.0 },
            new double[][] { { 0.25 } },
            new double[] { 0.25, 0.5 },
            new RungeKuttaStepInterpolator(RungeKuttaMethods.RK2) ) {

            @Override
            public int getOrder() {
                return 2;
            }
        };

        // set up test step handler
        final StepHandler stepHandler = new StepHandler() {

            private int count = 0;

            @Override
            public void handleStep(StepHandler event, int stepNumber) {
                assertEquals(initialValues.length, event.getState().getLength());
                assertEquals(initialValues[0], event.getState()[0], 1e-14);
                assertEquals(initialValues[1], event.getState()[1], 1e-14);
                count++;
            }
        };

        final InitialValueProblem ivp = new InitialValueProblem(equations, initialValues, start);
        integrator.setStepHandlers(stepHandler);
        integrator.integrate(ivp, start, end);

        // check that all steps have been processed
        assertEquals(1 + (int) Math.ceil((end - start) / 0.5), count);
    }

    /**
     * Test for RK4 integrator with events.
     */
    @Test
    public void testRk4WithEvents() {
        final double[] initialValues = { 1.0, 1.0 };
        final double   start          = 0.0;
        final double   end            = 2.0;

        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {

                @Override
                public int getDimension() {
                    return initialValues.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    yPrime[0] = y[1];
                    yPrime[1] = -y[0];
                }
            };

        // set up event
        final Event event = new When(1.5) {

            @Override
            public boolean isSatisfied(double t, double[] y) {
                return y[0] > 2.0;
            }

            @Override
            public double handle(double t, double[] y) {
                // do nothing
                return 0.0;
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RK4", false,
            new double[] { 1.0 / 4.0, 1.0 / 4.0, 1.0 / 2.0, 1.0 / 2.0 },
            new double[][] { { 0.25, 0.25, 0.5, 0.5 }, { 0, 0.25, 0.5, 0.25 } },
            new double[] { 0.25, 0.25, 0.5, 0.5 },
            new double[] { 0.25, 0.5, 0.25, 0.5 },
            new RungeKuttaStepInterpolator(RungeKuttaMethods.RK4) ) {

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public void addEventsHandlerManager(CombinedEventsManager manager) {
                manager.addHandler(event);
            }

        };

        // set up test step handler
        final StepHandler stepHandler = new StepHandler() {

            private int count = 0;

            @Override
            public void handleStep(StepHandler event, int stepNumber) {
                assertEquals(initialValues.length, event.getState().getLength());
                assertEquals(initialValues[0], event.getState()[0], 1e-14);
                assertEquals(initialValues[1], event.getState()[1], 1e-14);
                count++;
            }
        };

        integrator.setStepHandlers(stepHandler);
        integrator.integrate(new InitialValueProblem(equations, initialValues, start), start, end);

        // check that all steps have been processed
        assertEquals(3 + (int) Math.ceil((end - start) / 0.5), count);

        // check event has been triggered
        final double[] eventState = event.getState();
        assertEquals(initialValues.length, eventState.length);
        assertEquals(2.0, eventState[0], 1e-14);
        assertEquals(Math.sqrt(2.0), eventState[1], 1e-14);
    }

    /**
     * Test for RKF45 integrator.
     * @throws MathIllegalArgumentException if order is not supported
     */
    @Test
    public void testRkF45() throws MathIllegalArgumentException {
        final double[] initialValues = { 1.0, 1.0 };
        final double   start          = 0.0;
        final double   end            = 1.0;

        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {

                @Override
                public int getDimension() {
                    return initialValues.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    yPrime[0] = Math.exp(-t) * y[1];
                    yPrime[1] = Math.exp(-t) * y[0];
                }
            };

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RKF45", true,
            new double[] { 0.23, 0.23, 0.23, 0.0474, 0.0474, 0.0474, 0.0474, 0.0474 },
            new double[][] { { 0.37890625, 0.0, 0.346596487, 0.0, 0.230258507411, 0.0, 0.073115242503, 0.0 },
                             { 0.0, 0.37890625, 0.346596487, 0.0, 0.230258507411, 0.0474, 0.073115242503, 0.0474 },
                             { 0.0, 0.0, 0.346596487, 0.37890625, 0.230258507411, 0.0474, 0.073115242503, 0.0474 },
                             { 0.0, 0.0, 0.0, 0.37890625, 0.230258507411, 0.0474, 0.073115242503, 0.0474 },
                             { 0.0, 0.0, 0.0, 0.0, 0.230258507411, 0.37890625, 0.073115242503, 0.0474 },
                             { 0.0, 0.0, 0.0, 0.0, 0.0, 0.37890625, 0.073115242503, 0.0474 },
                             { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.37890625, 0.0474 },
                             { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.37890625 } },
            new double[] { 0.0, 0.236926885056189, 0.259822110892141, 0.216963500494162, 0.167961749895758,
                           0.120224261118446, 0.075329463468141, 0.027739765755451, 1.0 / 23.0 },
            new RungeKuttaStepInterpolator(RungeKuttaMethods.RKF45) ) {

                @Override
                public int getOrder() {
                    return 5;
                }
            };

        integrator.setMinStep(1e-10);
        integrator.setMaxStep(1e10);

        // set up test step handler
        final StepHandler stepHandler = new StepHandler() {

            private int count = 0;

            @Override
            public void handleStep(StepHandler event, int stepNumber) {
                assertEquals(initialValues.length, event.getState().getLength());
                assertEquals(initialValues[0], event.getState()[0], 1e-14);
                assertEquals(initialValues[1], event.getState()[1], 1e-14);
                count++;
            }
        };

        integrator.setStepHandlers(stepHandler);
        integrator.integrate(new InitialValueProblem(equations, initialValues, start), start, end);

        // check that all steps have been processed
        assertEquals(7, count);

        // check that the final state is correct
        final double[] finalState = integrator.getLastStepState();
        assertEquals(initialValues.length, finalState.length);
        assertEquals(0.678579574325868, finalState[0], 1e-14);
        assertEquals(0.678579574325868, finalState[1], 1e-14);
    }

}
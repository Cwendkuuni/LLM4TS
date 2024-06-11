package org.apache.commons.math3.ode.integrator.test;

import junit.framework.TestCase;

import org.apache.commons.math3.analysis.SolverRegistry;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.events.UnstableEventState;
import org.apache.commons.math3.ode.events.constraints.AlwaysTrueConstraint;
import org.apache.commons.math3.ode.events.constraints.NeverTrueConstraint;
import org.apache.commons.math3.ode.events.constraints.TimeWindowConstraint;
import org.apache.commons.math3.ode.events.sampling.StepInterpolator;
import org.apache.commons.math3.ode.events.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapterFactory;
import org.apache.commons.math3.ode.sampling.StepHandlerList;
import org.apache.commons.math3.ode.sampling.StepperState;
import org.apache.commons.math3.ode.stepper.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaFehlberg78Integrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaMersonIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaNystromIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaRadauIIAIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaRadauIIBIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaRk4Integrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaRkFalb9Integrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaRkFehlberg45Integrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaSundialsIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaWalshShampineIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaYoshidaIntegrator;
import org.apache.commons.math3.ode.stepper.StepperState;
import org.apache.commons.math3.ode.stepper.StepperStatus;
import org.apache.commons.math3.util.Precision;

public class AbstractIntegratorTest extends TestCase {

    /**
     * Test the AbstractIntegrator class with various integrators.
     */
    public void testIntegrator() {
        // create an integrator and test it with various ODEs
        for (String integratorName : new String[]{"Runge-Kutta", "RK-Fehlberg", "Runge-Kutta-Fehlberg",
                                                  "Runge-Kutta-Merson", "Runge-Kutta-Nystrom",
                                                  "Runge-Kutta-Radau-I", "Runge-Kutta-Radau-II",
                                                  "Runge-Kutta-Rk4", "Runge-Kutta-RkFalb9",
                                                  "Runge-Kutta-RkFehlberg45", "Runge-Kutta-Sundials",
                                                  "Runge-Kutta-Walsh-Shampine", "Runge-Kutta-Yoshida"}) {
            System.out.println("Testing " + integratorName);
            try {
                AbstractIntegrator integrator = getIntegrator(integratorName);
                assertNotNull(integrator);
                testIntegrator(integrator);
            } catch (Exception e) {
                fail("Exception while testing " + integratorName + ": " + e.getMessage());
            }
        }
    }

    /**
     * Test the AbstractIntegrator class with a simple ODE.
     * @param integrator the integrator to test
     * @throws Exception if the test fails
     */
    private void testIntegrator(AbstractIntegrator integrator)
        throws Exception {
        // create a simple ODE
        class SimpleODE implements FirstOrderDifferentialEquations {

            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1 - y[0]);
            }

            public void initialize(double t0, double[] y0, double[] yp0) {
                // no initialization needed
            }
        }

        // create the ODE and integrator
        SimpleODE ode = new SimpleODE();
        integrator.setEquations(ode);

        // integrate from t0=0 to t=1
        double[] y0 = new double[]{0.1};
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[]{0.1};
        double[] yDot = new double[ode.getDimension()];

        integrator.initIntegration(t0, y0, t);
        integrator.integrate(ode, t);
        integrator.computeDerivatives(t, y, yDot);
        assertEquals(t, integrator.getCurrentStepStart());
        assertEquals(Precision.round(ode.computeValue(t, y), 15), yDot[0], 1e-11);

        // test with various step sizes
        double[] y1 = new double[]{0.1};
        for (double h = 0.001; h <= 0.1; h *= 2) {
            integrator.setStepSize(h);
            integrator.integrate(ode, t);
            integrator.computeDerivatives(t, y, yDot);
            double t1 = integrator.getCurrentStepStart();
            assertEquals(t, t1, h);
            assertEquals(Precision.round(ode.computeValue(t, y), 15), yDot[0], 1e-11);
        }

        // test with various max evaluations
        integrator.setMaxEvaluations(10);
        integrator.integrate(ode, t);
        integrator.computeDerivatives(t, y, yDot);
        assertEquals(10, integrator.getEvaluations());
        integrator.setMaxEvaluations(50);
        integrator.integrate(ode, t);
        integrator.computeDerivatives(t, y, yDot);
        assertEquals(50, integrator.getEvaluations());

        // test with various step handlers
        StepHandlerList stepHandlers = new StepHandlerList();
        stepHandlers.add(new StepHandlerAdapter() {
            public void handleStep(StepperState state) {
                assertEquals(integrator.getClass().getSimpleName(), state.getIntegratorName());
            }
        });
        integrator.clearStepHandlers();
        integrator.addStepHandler(stepHandlers);
        integrator.integrate(ode, t);
        integrator.computeDerivatives(t, y, yDot);

        // test with events
        class EventODE extends SimpleODE {

            public EventState[] getEventStates() {
                return new EventState[]{new MockEventHandler(),
                                        new UnstableEventState()};
            }

            public boolean hasEvents() {
                return true;
            }
        }

        EventODE eventODE = new EventODE();
        integrator.clearEventHandlers();
        integrator.addEventHandler(eventODE, 0.5, 1e-6, 10, SolverRegistry.getSolver("Brent"));
        integrator.addEventHandler(eventODE, 0.7, 1e-6, 10, SolverRegistry.getSolver("Brent"));
        integrator.integrate(eventODE, 1.0);
        integrator.computeDerivatives(t, y, yDot);
        assertTrue(integrator.getEventHandlers().contains(eventODE.getEventStates()[0]));
        assertTrue(integrator.getEventHandlers().contains(eventODE.getEventStates()[1]));

        // test with various interpolators
        StepperState state = new StepperState(ode.getPrimaryState(0.5), 0.5, StepperStatus.SUCCESS);
        StepInterpolator interpolator = new AbstractStepInterpolator() {

            public double[] interpolate(double t, double[] y, double[] yDot) {
                return new double[]{Precision.round(ode.computeValue(t, y), 15)};
            }

            public void reinitializeBegin(StepperState state) {
                // no initialization needed
            }
        };
        integrator.clearStepHandlers();
        integrator.setStepInterpolator(interpolator);
        integrator.integrate(ode, 1.0);
        integrator.computeDerivatives(t, y, yDot);
        assertEquals(ode.getPrimaryState(t), y);

        // test with various integrators
        AbstractIntegrator integrator2 = getIntegrator("Runge-Kutta-RkFehlberg45");
        integrator2.setEquations(ode);
        integrator2.integrate(ode, t);
        integrator2.computeDerivatives(t, y, yDot);
        assertEquals(ode.getPrimaryState(t), y);
        integrator2.setMaxEvaluations(5);
        try {
            integrator2.integrate(ode, t);
            fail("Should have thrown an exception");
        } catch (MaxCountExceededException e) {
            // expected
        }

    }

    /**
     * Get an instance of the integrator with the given name.
     * @param integratorName the name of the integrator to get
     * @return the integrator instance
     * @throws Exception if the integrator cannot be instantiated
     */
    private AbstractIntegrator getIntegrator(String integratorName)
        throws Exception {
        if (integratorName.equals("Runge-Kutta")) {
            return new ClassicalRungeKuttaIntegrator();
        } else if (integratorName.startsWith("RK-Fehlberg")) {
            String methodName = integratorName.substring(6);
            if (methodName.equals("45")) {
                return new RungeKuttaRkFehlberg45Integrator();
            } else if (methodName.equals("78")) {
                return new RungeKuttaFehlberg78Integrator();
            } else {
                throw new IllegalArgumentException("Unknown RK-Fehlberg method: " + methodName);
            }
        } else if (integratorName.equals("Runge-Kutta-Fehlberg")) {
            return new RungeKuttaRkFehlbergIntegrator();
        } else if (integratorName.equals("Runge-Kutta-Merson")) {
            return new RungeKuttaMersonIntegrator();
        } else if (integratorName.equals("Runge-Kutta-Nystrom")) {
            return new RungeKuttaNystromIntegrator();
        } else if (integratorName.equals("Runge-Kutta-Radau-I")) {
            return new RungeKuttaRadauIIAIntegrator();
        } else if (integratorName.equals("Runge-Kutta-Radau-II")) {
            return new RungeKuttaRadauIIBIntegrator();
        } else if (integratorName.equals("Runge-Kutta-Rk4")) {
            return new RungeKuttaRk4Integrator();
        } else if (integratorName.equals("Runge-Kutta-RkFalb9")) {
            return new RungeKuttaRkFalb9Integrator();
        } else if (integratorName.equals("Runge-Kutta-RkFehlberg45")) {
            return new RungeKuttaRkFehlberg45Integrator();
        } else if (integratorName.equals("Runge-Kutta-Sundials")) {
            return new RungeKuttaSundialsIntegrator();
        } else if (integratorName.equals("Runge-Kutta-Walsh-Shampine")) {
            return new RungeKuttaWalshShampineIntegrator();
        } else if (integratorName.equals("Runge-Kutta-Yoshida")) {
            return new RungeKuttaYoshidaIntegrator();
        } else {
            throw new IllegalArgumentException("Unknown integrator: " + integratorName);
        }
    }

}
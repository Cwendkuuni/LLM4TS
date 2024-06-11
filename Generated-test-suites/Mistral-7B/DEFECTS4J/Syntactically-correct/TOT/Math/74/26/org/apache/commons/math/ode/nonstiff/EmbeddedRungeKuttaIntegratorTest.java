package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootFinderException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethod;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolatorAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolatorFactory;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double TOL = 1e-14;
    private static final int MAX_ITER = 1000;
    private static final int MAX_STEP = 100;
    private static final int MIN_STEP = 1e-10;

    private EmbeddedRungeKuttaIntegrator rk45;
    private EmbeddedRungeKuttaIntegrator rkFehlberg78;

    @Before
    public void setUp() {
        RungeKuttaMethod rk45m = new RungeKuttaMethod("RK45", 4, 5, false);
        rk45 = new EmbeddedRungeKuttaIntegrator(rk45m.getName(), true, rk45m.getC(), rk45m.getA(), rk45m.getB(),
                new RungeKuttaStepInterpolator(rk45m), MIN_STEP, MAX_STEP, TOL, TOL);

        RungeKuttaMethod rkfehlberg78m = new RungeKuttaMethod("RKFehlberg78", 7, 8, true);
        rkFehlberg78 = new EmbeddedRungeKuttaIntegrator(rkfehlberg78m.getName(), true, rkfehlberg78m.getC(), rkfehlberg78m.getA(), rkfehlberg78m.getB(),
                new RungeKuttaStepInterpolator(rkfehlberg78m), MIN_STEP, MAX_STEP, TOL, TOL);
    }

    @Test
    public void testRK45() throws RootFinderException, MathIllegalArgumentException, MaxIterationsExceededException, NoBracketingException, IntegratorException {
        testRK(rk45, new RK45Problem());
    }

    @Test
    public void testRKFehlberg78() throws RootFinderException, MathIllegalArgumentException, MaxIterationsExceededException, NoBracketingException, IntegratorException {
        testRK(rkFehlberg78, new RKFehlberg78Problem());
    }

    private void testRK(EmbeddedRungeKuttaIntegrator integrator, FirstOrderDifferentialEquations problem)
            throws RootFinderException, MathIllegalArgumentException, MaxIterationsExceededException, NoBracketingException, IntegratorException {

        double[] y0 = new double[]{0, 1};
        double t0 = 0;
        double t1 = 1;

        // test integration using default values
        double[] y1 = integrator.integrate(problem, t0, y0, t1);
        double[] y2 = new double[]{1, 0};

        assertEquals("Integration failure: wrong solution", y1, y2, TOL);

        // test integration with custom step interpolator
        StepInterpolator stepInterpolator = new StepInterpolatorAdapter() {
            @Override
            public double interpolate(double time) {
                // custom step interpolator implementation
            }
        };
        integrator.setStepInterpolator(new StepInterpolatorFactory() {
            @Override
            public StepInterpolator createInterpolator() {
                return stepInterpolator;
            }
        });
        integrator.integrate(problem, t0, y0, t1);

        // test integration with custom step handler
        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                // custom step handler implementation
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.integrate(problem, t0, y0, t1);

        // test integration with custom events manager
        Event event = new When(){
            @Override
            public boolean occurs(double t, double[] y, double[] yPrime, int iStep) {
                // custom event handler implementation
                return false;
            }
        };
        integrator.addEvent(event);
        integrator.integrate(problem, t0, y0, t1);

        // test integration with custom events manager and custom step handler
        integrator.addEvent(event);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(problem, t0, y0, t1);

    }

    private static class RK45Problem implements FirstOrderDifferentialEquations {
        @Override
        public int getDimensions() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }
    }

    private static class RKFehlberg78Problem implements FirstOrderDifferentialEquations {
        @Override
        public int getDimensions() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = BesselFunctions.besselJ(0, t);
        }
    }

}
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Test;
import org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation of integrate method here
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2 * y[0];
            }
        };

        integrator.initIntegration(0, new double[]{0.0}, 1.0);
        integrator.setEquations(equations);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return integrator.getCurrentStepStart();
            }

            @Override
            public void setInterpolatedTime(double time) {
                integrator.setCurrentStepStart(time);
            }
        };

        double[] y = new double[1];
        double tEnd = 1.0;

        try {
            integrator.integrate(interpolator, y, tEnd);
            fail("Integration should have thrown an exception.");
        } catch (Exception e) {
            // expected
        }

        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double[] yDotPrime,
                                      double[] eventParameters) {
                // implementation of evaluateState method here
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }
        }, 0.0, 0.0, 1, null);

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // implementation of handleStep method here
            }
        });

        integrator.setMaxEvaluations(10);

        integrator.integrate(equations, 0.0, new double[]{0.0}, tEnd, y);
        assertEquals(1.0, integrator.getEvaluations(), 0.0);
        assertEquals(1.0, y[0], 0.0);

        integrator.integrate(equations, tEnd, new double[]{1.0}, 2.0, y);
        assertEquals(12, integrator.getEvaluations(), 0.0);
        assertEquals(2.718281828, Math.exp(1.0), y[0]);
    }

}
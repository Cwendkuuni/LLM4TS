package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethods.EmbeddedRk23F2;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRk23F2() throws NumberIsTooSmallException, DimensionMismatchException, NoBracketingException, MaxCountExceededException {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRk23F2();
        assertEquals(23, integrator.getOrder());

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(double currentTime, double[] currentState, double[] newState, double[] newDerivatives, double[] externalDerivatives) {
                // do something with the state and derivatives
            }
        };

        double[] initialState = new double[]{0.0, 0.1};
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                // compute derivatives based on state
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
                // do nothing
            }

            @Override
            public double[] getCompleteState() {
                return initialState;
            }

            @Override
            public void setCompleteState(double[] state) {
                // do nothing
            }

            @Override
            public ExpandableStatefulODE copy() {
                return this;
            }

            @Override
            public String getName() {
                return "TestEquations";
            }

            @Override
            public void setEquations(ExpandableStatefulODE equations) {
                // do nothing
            }

            @Override
            public void setPrimaryMapper(StateMapper mapper) {
                // do nothing
            }

            @Override
            public void setSecondaryMappers(StateMapper[] mappers) {
                // do nothing
            }

            @Override
            public StateMapper getPrimaryMapper() {
                return null;
            }

            @Override
            public StateMapper[] getSecondaryMappers() {
                return new StateMapper[0];
            }
        }, startTime, endTime, null, null, handler);
    }

    @Test
    public void testSettersAndGetters() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRk23F2();
        assertEquals(0.9, integrator.getSafety(), 0.001);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.001);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.001);
    }
}
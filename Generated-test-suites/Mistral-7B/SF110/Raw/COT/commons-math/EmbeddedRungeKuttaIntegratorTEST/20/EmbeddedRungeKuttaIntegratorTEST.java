package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;

    public EmbeddedRungeKuttaIntegratorTest(String integratorName, double[] c, double[][] a, double[] b, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        integrator = new EmbeddedRungeKuttaIntegrator(integratorName, true, c, a, b, new RungeKuttaStepInterpolator(new RungeKuttaMethod(5, 5, new RungeKuttaStepInterpolator.DefaultInterpolatorFactory())), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                // Implement your custom error estimation logic here
                return 0.0;
            }
        };
        equations = new SecondOrderRungeKutta255DOP853System();
    }

    @Parameters(name = "{0}, c={}, a={}, b={}")
    @SuppressWarnings("unchecked")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"RKF5(DOP853)", new double[]{0.25, 0.25, 0.5, 0.5}, new double[][]{{0.25, 0.25}, {0.25, 0.4375}, {0.5, 0.25}, {0.5, 0.25}, {0.5, 0.25}}, new double[]{0.25, 0.25}},
                {"RKF7(DOP853)", new double[]{0.1488574366713692, 0.3679732042572875, 0.4829687514516129, 0.4829687514516129, 0.1369429163731585}, new double[][]{{0.1488574366713692, 0.1488574366713692}, {0.1133728643417241, 0.3094732330228768}, {0.3679732042572875, 0.1488574366713692}, {0.3679732042572875, 0.3679732042572875}, {0.0862865145095358, 0.3274364128264467}, {0.1369429163731585, 0.0862865145095358}}, new double[]{0.001, 0.001}},
                {"RKF85(DOP853)", new double[]{0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3}, new double[][]{{0.3, 0.3}, {0.171875, 0.45}, {0.3, 0.3}, {0.3, 0.3}, {0.25982211264}, {0.25982211264}, {0.1815133232855}, {0.1815133232855}}, new double[]{0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001, 0.001}}
        });
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        integrator.integrate(equations, 10.0);
        double[] expected = {1.0, 2.5};
        assertArrayEquals(expected, equations.getCompleteState(), 0.0001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        integrator.setMaxStep(0.01);
        integrator.integrate(equations, 10.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimensions() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equationsWithMismatchedDimensions = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void calculateStateDerivatives(double t, double[] state, double[] derivatives) {
                // Intentionally left empty
            }
        };
        integrator.integrate(equationsWithMismatchedDimensions, 10.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceeded() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equationsWithMaxCountExceeded = new SecondOrderRungeKutta255DOP853System() {
            @Override
            public int getMaxSteps() {
                return 1;
            }
        };
        integrator.integrate(equationsWithMaxCountExceeded, 10.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equationsWithNoBracketing = new SecondOrderRungeKutta255DOP853System() {
            @Override
            public double getTimeEnd() {
                return -1.0;
            }
        };
        integrator.integrate(equationsWithNoBracketing, 10.0);
    }

}
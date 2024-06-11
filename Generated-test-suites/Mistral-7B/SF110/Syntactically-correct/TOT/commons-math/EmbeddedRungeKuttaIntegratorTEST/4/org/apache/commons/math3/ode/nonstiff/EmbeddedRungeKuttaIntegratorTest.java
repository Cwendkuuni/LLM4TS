package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.noexc.NoBracketingException;
import org.apache.commons.math3.ode.noexc.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        double[] c = {0.23692688505667133, 0.3254731222622104, 0.0473964663815639, 0.678724925152441};
        double[][] a = {{0.3568763013930374, 0, 0, 0}, {0.467913786272182, 0.3568763013930374, 0, 0}, {0.5689511111231356, 0.467913786272182, 0.3568763013930374, 0}, {0.6791884358841837, 0.5689511111231356, 0.467913786272182, 0.3568763013930374}};
        double[] b = {0.0, 0.0, 0.0, 0.6};

        EmbeddedRungeKuttaIntegrator integrator = new RK45Integrator("RK45Integrator", true, c, a, b, new RungeKuttaStepInterpolator(new RungeKuttaMethod("RK45")), 0.001, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{0.0};
            }

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                y[0] = Math.exp(-0.1 * t) * Math.sin(1.0 * t);
            }
        };

        integrator.integrate(equations, 10.0);

        double expectedResult = Math.exp(-0.1) * Math.sin(1.0) + 0.6 * (Math.exp(-0.1 * 10.0) - Math.exp(-0.1));
        assertEquals(expectedResult, equations.getCompleteState()[0], 1e-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testStepSizeTooSmall() {
        double[] c = {0.23692688505667133, 0.3254731222622104, 0.0473964663815639, 0.678724925152441};
        double[][] a = {{0.3568763013930374, 0, 0, 0}, {0.467913786272182, 0.3568763013930374, 0, 0}, {0.5689511111231356, 0.467913786272182, 0.3568763013930374, 0}, {0.6791884358841837, 0.5689511111231356, 0.467913786272182, 0.3568763013930374}};
        double[] b = {0.0, 0.0, 0.0, 0.6};

        EmbeddedRungeKuttaIntegrator integrator = new RK45Integrator("RK45Integrator", true, c, a, b, new RungeKuttaStepInterpolator(new RungeKuttaMethod("RK45")), 0.00001, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{0.0};
            }

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                y[0] = Math.exp(-0.1 * t) * Math.sin(1.0 * t);
            }
        };

        integrator.integrate(equations, 10.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testBracketingFailure() {
        double[] c = {0.23692688505667133, 0.3254731222622104, 0.0473964663815639, 0.678724925152441};
        double[][] a = {{0.3568763013930374, 0, 0, 0}, {0.467913786272182, 0.3568763013930374, 0, 0}, {0.5689511111231356, 0.467913786272182, 0.3568763013930374, 0}, {0.6791884358841837, 0.5689511111231356, 0.467913786272182, 0.3568763013930374}};
        double[] b = {0.0, 0.0, 0.0, 0.6};

        EmbeddedRungeKuttaIntegrator integrator = new RK45Integrator("RK45Integrator", true, c, a, b, new RungeKuttaStepInterpolator(new RungeKuttaMethod("RK45")), 0.001, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{0.0};
            }

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                throw new RuntimeException("Derivatives not implemented for this test case.");
            }
        };

        integrator.integrate(equations, 10.0);
    }
}
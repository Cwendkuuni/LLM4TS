package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.NonstiffIntegrator;
import org.apache.commons.math3.ode.SamplingPoints;
import org.apache.commons.math3.ode.util.DoubleIntegratorConstants;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorConstants;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator_getOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };
        int order = integrator.getOrder();
        assertEquals(5, order);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_getSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.001);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_integrate_forward() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(0.0, 10.0, 1);
        equations.addVariable("y", 0.0);
        equations.setFunction(x -> Math.sin(x));

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };

        double[] initialState = {0.0};
        integrator.integrate(equations, 0.0);
        double[] stateAt1 = new double[initialState.length];
        System.arraycopy(equations.getCompleteState(), 0, stateAt1, 0, stateAt1.length);

        SamplingPoints samplingPoints = new SamplingPoints(100);
        double[] yValues = new double[samplingPoints.getNumberOfPoints()];
        integrator.initialize(equations, 0.0);
        for (int i = 0; i < samplingPoints.getNumberOfPoints(); i++) {
            integrator.nextStep(samplingPoints.getPoint(i));
            yValues[i] = equations.getCompleteState()[0];
        }

        double[] expectedYValues = new double[yValues.length];
        for (int i = 0; i < expectedYValues.length; i++) {
            expectedYValues[i] = Math.sin(samplingPoints.getPoint(i));
        }

        ODETestUtils.compareArrays(yValues, expectedYValues, Precision.SAFE_DOUBLE);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_integrate_backward() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(10.0, 0.0, 1);
        equations.addVariable("y", 0.0);
        equations.setFunction(x -> Math.sin(x));

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };

        double[] initialState = {0.0};
        integrator.integrate(equations, 10.0);
        double[] stateAt1 = new double[initialState.length];
        System.arraycopy(equations.getCompleteState(), 0, stateAt1, 0, stateAt1.length);

        SamplingPoints samplingPoints = new SamplingPoints(100);
        double[] yValues = new double[samplingPoints.getNumberOfPoints()];
        integrator.initialize(equations, 10.0);
        for (int i = samplingPoints.size() - 1; i >= 0; i--) {
            integrator.nextStep(samplingPoints.getPoint(i));
            yValues[i] = equations.getCompleteState()[0];
        }

        double[] expectedYValues = new double[yValues.length];
        for (int i = 0; i < expectedYValues.length; i++) {
            expectedYValues[i] = Math.sin(samplingPoints.getPoint(samplingPoints.size() - 1 - i));
        }

        ODETestUtils.compareArrays(yValues, expectedYValues, Precision.SAFE_DOUBLE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEmbeddedRungeKuttaIntegrator_integrate_numberToSmall() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(0.0, 1.0e-20, 1);
        equations.addVariable("y", 0.0);
        equations.setFunction(x -> Math.sin(x));

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COEFFS_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };

        integrator.integrate(equations, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEmbeddedRungeKuttaIntegrator_integrate_dimensionMismatch() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(0.0, 10.0, 1);
        equations.addVariable("y", new double[]{0.0, 0.0});
        equations.setFunction(x -> new double[]{Math.sin(x), Math.cos(x)});

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };

        integrator.integrate(equations, 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEmbeddedRungeKuttaIntegrator_integrate_maxCountExceeded() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(0.0, 10.0, 1);
        equations.addVariable("y", 0.0);
        equations.setFunction(x -> Math.sin(x));

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };

        integrator.setMaxSteps(1);
        integrator.integrate(equations, 0.0, 10.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testEmbeddedRungeKuttaIntegrator_integrate_noBracketing() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(0.0, 1.0, 1);
        equations.addVariable("y", 0.0);
        equations.setFunction(x -> Math.sin(x));

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs_A, EmbeddedRungeKuttaIntegratorConstants.RK45_COeffs_B, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                double error = 0.0;
                for (int i = 0; i < p0[0].length; i++) {
                    error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };

        integrator.integrate(equations, 0.0, 1.5);
    }

}
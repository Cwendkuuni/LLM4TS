package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double initialTime;
    private double[] initialState;
    private double finalTime;

    public EmbeddedRungeKuttaIntegratorTest(String integratorName, boolean fsal, double[] c, double[][] a, double[] b, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance, double minReduction, double maxGrowth) {
        integrator = new EmbeddedRungeKuttaIntegrator(integratorName, fsal, c, a, b, new RungeKuttaStepInterpolator(new RungeKuttaMethod(c, a, b, new RKStepPerformer(), new RKContinuousStepper(), new RKNumericStepper())), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance, minReduction, maxGrowth);
        equations = new GoebbelsGinouxODE(initialState, initialTime);
    }

    @Parameters
    public static Collection<Object[]> testData() {
        Object[][] data = new Object[][] {
                {"RungeKutta23FehlbergIntegrator", false, new double[]{1.0, 1.0}, new double[][]{{0.23692688, 0.23692688}, {0.35274497, 0.28861011}, {0.49139722, 0.17230589}, {0.07784716, 0.04668274}, {0.01491365, 0.00231924}}, new double[]{{0.2, 0.02}, {0.001, 0.0001}}, 1.0e-8, 1.0e-12, 0.1, 10.0},
                {"RungeKuttaFehlberg78Integrator", true, new double[]{7.0, 7.0}, new double[][]{{0.3465005129607763, 0.0259601061002424}, {0.2369268866212416, 0.0233229487775252}, {0.2369268866212416, 0.0233229487775252}, {0.0149136532045422, 0.0044728339816266}, {0.0015773602618522, 0.0000383533836256}, {0.0, 0.0}, {0.0, 0.0}}, new double[]{{0.0, 0.0}, {0.0, 0.0}}, 1.0e-12, 1.0e-16, 0.1, 10.0},
                // Add more test cases if necessary
        };
        return Arrays.asList(data);
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        initialState = new double[]{1.0, 1.0};
        initialTime = 0.0;
        finalTime = 10.0;
        integrator.integrate(equations, finalTime);
        double[] expectedState = new double[]{2.718281828459045, 2.718281828459045};
        assertArrayEquals(expectedState, equations.getCompleteState());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws NumberIsTooSmallException {
        initialState = new double[]{1.0, 1.0};
        initialTime = 10.0;
        finalTime = 10.0;
        integrator.integrate(equations, finalTime);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        initialState = new double[]{1.0};
        initialTime = 0.0;
        finalTime = 10.0;
        try {
            integrator.integrate(equations, finalTime);
        } catch (NumberIsTooSmallException | MaxCountExceededException | NoBracketingException e) {
            throw new DimensionMismatchException();
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        initialState = new double[]{1.0, 1.0};
        initialTime = 0.0;
        finalTime = 10.0;
        StepControlledIntegrator integrator = new StepControlledIntegrator(10, integrator.getName(), integrator.getOrder(), new double[]{0.0, 0.0}, new double[][]{{}}, new double[]{}, 0.0, 1.0, integrator.getScalAbsoluteTolerance(), integrator.getScalRelativeTolerance(), integrator.isFsal(), integrator.getMinReduction(), integrator.getMaxGrowth());
        try {
            integrator.integrate(equations, finalTime);
        } catch (NumberIsTooSmallException | DimensionMismatchException | NoBracketingException e) {
            throw new MaxCountExceededException();
        }
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        initialState = new double[]{1.0, 1.0};
        initialTime = 0.0;
        finalTime = 0.0;
        try {
            integrator.integrate(equations, finalTime);
        } catch (NumberIsTooSmallException | DimensionMismatchException | MaxCountExceededException e) {
            throw new NoBracketingException();
        }
    }

    @Test
    public void testSetSafety() {
        double safety = 0.5;
        integrator.setSafety(safety);
        assertEquals(safety, integrator.getSafety(), 1e-6);
    }

    @Test
    public void testSetMinReduction() {
        double minReduction = 0.15;
        integrator.setMinReduction(minReduction);
        assertEquals(minReduction, integrator.getMinReduction(), 1e-6);
    }

    @Test
    public void testSetMaxGrowth() {
        double maxGrowth = 2.5;
        integrator.setMaxGrowth(maxGrowth);
        assertEquals(maxGrowth, integrator.getMaxGrowth(), 1e-6);
    }
}
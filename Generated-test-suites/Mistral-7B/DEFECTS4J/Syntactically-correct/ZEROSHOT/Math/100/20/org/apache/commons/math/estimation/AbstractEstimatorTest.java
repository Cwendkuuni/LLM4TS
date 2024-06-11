package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.stat.descript.Statistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private double[][] data;
    private double[] expectedParameters;
    private double[] expectedErrors;
    private double[] expectedResiduals;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new MyAbstractEstimator();
        data = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        expectedParameters = new double[] {1.5, 2.5};
        expectedErrors = new double[] {0.3, 0.4};
        expectedResiduals = new double[] {-0.8284271633542762, -0.31622776601683796,
                0.5345094114453543, 0.1837325852619721};
        problem = new EstimationProblem() {

            @Override
            public int getNumberOfMeasurements() {
                return data.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int index) {
                return new WeightedMeasurement() {

                    @Override
                    public double getResidual() {
                        double[] values = data[index];
                        double x = values[0];
                        double y = values[1];
                        double weight = 1.0;
                        return y - estimator.evaluate(new double[] {x});
                    }

                    @Override
                    public double getWeight() {
                        return weight;
                    }

                    @Override
                    public double getObservedValue() {
                        return y;
                    }

                    @Override
                    public double[] getParameters() {
                        return new double[] {x};
                    }

                    @Override
                    public RealVector getPartialDerivatives() {
                        return new ArrayRealVector(new double[] {1.0});
                    }
                };
            }

            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {new EstimatedParameter(0), new EstimatedParameter(1)};
            }

            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {0.0, 0.0});
            }
        };
    }

    @Test
    public void testGetters() {
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());

        estimator.setMaxCostEval(10);

        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        double[] jacobianExpected = new double[] {
            -0.366025403784439, 0.366025403784439,
            -0.366025403784439, 0.366025403784439,
            -0.366025403784439, 0.366025403784439,
            -0.366025403784439, 0.366025403784439,
        };

        assertArrayEquals(jacobianExpected, estimator.jacobian);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();

        double[] expectedResidualsAndCost = new double[] {
            -0.8284271633542762, -0.31622776601683796, 0.5345094114453543, 0.1837325852619721,
            0.0,
        };

        assertArrayEquals(expectedResidualsAndCost, new double[] {estimator.costEvaluations, estimator.cost, estimator.residuals}, 0.0);
    }

    @Test
    public void testGetRMS() {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();

        double rmsExpected = 0.8305922230076282;
        assertEquals(rmsExpected, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();

        double chiSquareExpected = 1.6163866685209684;
        assertEquals(chiSquareExpected, estimator.getChiSquare(problem), 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        double[][] covariancesExpected = {
            {0.0025680587798578, 0.0025680587798578},
            {0.0025680587798578, 0.0025680587798578},
        };

        RealMatrix covariances = estimator.getCovariances(problem);
        RealMatrix covariancesExpectedMatrix = MatrixUtils.createRealMatrix(covariancesExpected);

        assertEquals(covariancesExpectedMatrix.getRowDimension(), covariances.getRowDimension());
        assertEquals(covariancesExpectedMatrix.getColumnDimension(), covariances.getColumnDimension());
        assertEquals(covariancesExpectedMatrix, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        double[] errorsExpected = {0.1647865960423622, 0.1647865960423622};
        assertEquals(Arrays.toString(errorsExpected), Arrays.toString(estimator.guessParametersErrors(problem)), 0.0);
    }

    @Test
    public void testEstimateThrowsExceptionWhenMaxCostEvalExceeded() {
        estimator = new MyAbstractEstimator();
        estimator.setMaxCostEval(1);
        problem.setNumberOfMeasurements(1000000);

        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    @Test
    public void testEstimateReturnsExpectedParameters() throws EstimationException {
        estimator = new MyAbstractEstimator();
        estimator.setMaxCostEval(100000);

        estimator.estimate(problem);

        SimpleRegression regression = new SimpleRegression();
        regression.addData(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{1.5});
        regression.addData(new double[][]{new double[]{3.0}, new double[]{4.0}}, new double[]{2.5});
        regression.addData(new double[][]{new double[]{5.0}, new double[]{6.0}}, new double[]{1.5});
        regression.addData(new double[][]{new double[]{7.0}, new double[]{8.0}}, new double[]{2.5});

        RealVector parametersEstimated = estimator.getEstimatedParameters(problem);

        assertEquals(Arrays.toString(expectedParameters), Arrays.toString(parametersEstimated.toArray()), 0.0);
    }

    @Test
    public void testEstimateReturnsExpectedResiduals() throws EstimationException {
        estimator = new MyAbstractEstimator();
        estimator.setMaxCostEval(100000);

        estimator.estimate(problem);

        assertArrayEquals(expectedResiduals, estimator.residuals, 0.0);
    }

    @Test
    public void testGetEstimatedParameters() throws EstimationException {
        estimator = new MyAbstractEstimator();
        estimator.setMaxCostEval(100000);

        estimator.estimate(problem);

        RealVector parametersEstimated = estimator.getEstimatedParameters(problem);

        assertEquals(Arrays.toString(expectedParameters), Arrays.toString(parametersEstimated.toArray()), 0.0);
    }

    @Test
    public void testGetErrors() throws EstimationException {
        estimator = new MyAbstractEstimator();
        estimator.setMaxCostEval(100000);

        estimator.estimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);

        assertEquals(Arrays.toString(expectedErrors), Arrays.toString(errors), 0.0);
    }

    @Test
    public void testEvaluate() {
        estimator = new MyAbstractEstimator();

        double[] parameters = new double[] {1.0};
        double[] input = new double[] {1.5};
        double expectedOutput = 2.0;

        assertEquals(expectedOutput, estimator.evaluate(parameters), 0.0);
    }

    private static class MyAbstractEstimator extends AbstractEstimator {

        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            // Do nothing
        }
    }
}
import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.AbstractEstimatorTest;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.estimation.EstimatedParameter;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new FakeEstimator();
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new FakeEstimator();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new FakeEstimator();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxEvaluationReached() throws EstimationException {
        AbstractEstimator estimator = new FakeEstimator();
        estimator.setMaxCostEval(1);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2, 1), new WeightedMeasurement(2, 1, 1), new WeightedMeasurement(3, 3, 1) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter(1), new EstimatedParameter(2) };
            }
        };
        AbstractEstimator estimator = new FakeEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(Math.sqrt(2.5), estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2, 1), new WeightedMeasurement(2, 1, 1), new WeightedMeasurement(3, 3, 1) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter(1), new EstimatedParameter(2) };
            }
        };
        AbstractEstimator estimator = new FakeEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(6.5, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2, 1), new WeightedMeasurement(2, 1, 1), new WeightedMeasurement(3, 3, 1) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter(1), new EstimatedParameter(2) };
            }
        };
        AbstractEstimator estimator = new FakeEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        RealMatrix covarianceMatrix = estimator.getCovariances(problem);
        RealMatrix expectedCovarianceMatrix = MatrixUtils.identity(2);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(expectedCovarianceMatrix);
        RealMatrix inverse = eigenDecomposition.getB();
        RealArray2D covarianceMatrixData = covarianceMatrix.getData();
        RealArray2D inverseData = inverse.getData();

        for (int i = 0; i < covarianceMatrixData.getRowDimension(); i++) {
            for (int j = 0; j < covarianceMatrixData.getColumnDimension(); j++) {
                assertEquals(inverseData.getEntry(i, j), covarianceMatrixData.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2, 1), new WeightedMeasurement(2, 1, 1), new WeightedMeasurement(3, 3, 1) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter(1), new EstimatedParameter(2) };
            }
        };
        AbstractEstimator estimator = new FakeEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);
        RealVector expectedErrors = new ArrayRealVector(new double[] { 0.5, 0.5 });
        RealVector actualErrors = new ArrayRealVector(errors);

        assertEquals(expectedErrors, actualErrors);
    }

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {};
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {};
            }
        };
        AbstractEstimator estimator = new FakeEstimator();
        estimator.initializeEstimate(problem);
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertEquals(0, estimator.rows);
        assertEquals(0, estimator.cols);
        assertArrayEquals(new double[0], estimator.jacobian);
        assertArrayEquals(new double[0], estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }

    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithInvalidMeasurements() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                throw new RuntimeException();
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };
        AbstractEstimator estimator = new FakeEstimator();
        estimator.initializeEstimate(problem);
    }

    private static class FakeEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            // empty implementation
        }
    }

}
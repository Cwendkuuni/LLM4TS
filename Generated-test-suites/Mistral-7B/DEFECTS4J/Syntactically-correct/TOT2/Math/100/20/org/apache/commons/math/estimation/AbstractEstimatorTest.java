import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new MyAbstractEstimator(); // replace with the concrete estimator class
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(3.0, 4.0),
                    new WeightedMeasurement(5.0, 6.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter(0), new EstimatedParameter(1) };
            }
        };
        estimator.setMaxCostEval(10); // for example, let's test with a maximum of 10 cost evaluations
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(5);
        assertEquals(5, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.estimate(problem);
        assertEquals(3, estimator.getCostEvaluations()); // 3 calls to updateResidualsAndCost()
    }

    @Test
    public void testUpdateJacobian() {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        RealMatrix expectedJacobian = new RealMatrixImpl(new double[][] {
                {-1.414213625, -0.707106768},
                { 1.414213625,  0.707106768},
                { 1.414213625, -1.414213625},
                {-1.414213625,  1.414213625}
        });
        RealMatrix actualJacobian = new RealMatrixImpl(estimator.jacobian);
        assertEquals(expectedJacobian, actualJacobian);
    }

    @Test(expected = EstimationException.class)
    public void testExceedMaxCostEval() {
        estimator.initializeEstimate(problem);
        for (int i = 0; i < 11; i++) {
            estimator.updateJacobian();
            estimator.updateResidualsAndCost();
        }
    }

    @Test
    public void testGetRMS() {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        // replace with the expected RMS value
        assertEquals(2.5, rms);
    }

    @Test
    public void testGetChiSquare() {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        // replace with the expected chi-square value
        assertEquals(7.5, chiSquare);
    }

    @Test
    public void testGetCovariances() {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] covarianceMatrix = estimator.getCovariances(problem);
        // replace with the expected covariance matrix
        RealArray2D expectedCovarianceMatrix = new RealArray2D(new double[][] {
                { 1.2, 0.5 },
                { 0.5, 2.0 }
        });
        RealMatrix actualCovarianceMatrix = new RealMatrixImpl(covarianceMatrix);
        assertEquals(expectedCovarianceMatrix, actualCovarianceMatrix);
    }

    @Test
    public void testGuessParametersErrors() {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        // replace with the expected errors
        assertArrayEquals(new double[] { 0.5, 0.25 }, errors);
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        EstimatedParameter[] estimatedParameters = problem.getAllParameters();
        // replace with the expected values
        assertEquals(1.0, estimatedParameters[0].getValue(), 0.01);
        assertEquals(2.5, estimatedParameters[1].getValue(), 0.01);
    }

}
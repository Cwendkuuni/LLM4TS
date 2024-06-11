import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealArray2D;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        problem = new EstimationProblem() {
            @Override
            public double[] getAllParameters() {
                return new double[0];
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };
    }

    @Test
    public void testSetAndGetMaxCostEval() {
        int maxCostEval = 10;
        estimator.setMaxCostEval(maxCostEval);
        assertEquals(maxCostEval, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.costEvaluations = 5;
        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.jacobianEvaluations = 3;
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.rows = 2;
        estimator.cols = 3;
        estimator.jacobian = new double[6];
        estimator.measurements = new WeightedMeasurement[2];
        estimator.parameters = new EstimatedParameter[3];
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, new double[]{1.0, 2.0, 3.0}, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, new double[]{4.0, 5.0, 6.0}, 1.0);
        estimator.measurements[0] = wm1;
        estimator.measurements[1] = wm2;

        estimator.updateJacobian();

        RealArray2D expectedJacobian = new RealArray2D(3, 2);
        expectedJacobian.setEntry(0, 0, -Math.sqrt(wm1.getWeight()) * wm1.getPartial(0));
        expectedJacobian.setEntry(0, 1, -Math.sqrt(wm1.getWeight()) * wm1.getPartial(1));
        expectedJacobian.setEntry(0, 2, -Math.sqrt(wm1.getWeight()) * wm1.getPartial(2));
        expectedJacobian.setEntry(1, 0, -Math.sqrt(wm2.getWeight()) * wm2.getPartial(0));
        expectedJacobian.setEntry(1, 1, -Math.sqrt(wm2.getWeight()) * wm2.getPartial(1));
        expectedJacobian.setEntry(1, 2, -Math.sqrt(wm2.getWeight()) * wm2.getPartial(2));

        assertArrayEquals(expectedJacobian.getData(), estimator.jacobian, 0.0);
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.rows = 3;
        estimator.cols = 2;
        estimator.jacobian = new double[6];
        estimator.residuals = new double[3];
        estimator.measurements = new WeightedMeasurement[3];
        estimator.parameters = new EstimatedParameter[2];
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, new double[]{1.0, 2.0}, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, new double[]{2.0, 3.0}, 1.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, new double[]{3.0, 4.0}, 1.0);
        estimator.measurements[0] = wm1;
        estimator.measurements[1] = wm2;
        estimator.measurements[2] = wm3;

        estimator.updateResidualsAndCost();

        double expectedCost = Math.sqrt(wm1.getWeight() * wm1.getResidual() * wm1.getResidual()
                + wm2.getWeight() * wm2.getResidual() * wm2.getResidual()
                + wm3.getWeight() * wm3.getResidual() * wm3.getResidual()) / Math.sqrt(3.0);

        assertArrayEquals(new double[]{wm1.getResidual() * Math.sqrt(wm1.getWeight()),
                wm2.getResidual() * Math.sqrt(wm2.getWeight()),
                wm3.getResidual() * Math.sqrt(wm3.getWeight())}, estimator.residuals, 0.0);
        assertEquals(expectedCost, estimator.cost, 0.0);
        assertEquals(3, estimator.costEvaluations);
    }

    @Test
    public void testGetRMS() {
        estimator.rows = 3;
        estimator.cols = 2;
        estimator.jacobian = new double[6];
        estimator.residuals = new double[3];
        estimator.measurements = new WeightedMeasurement[3];
        estimator.parameters = new EstimatedParameter[2];
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, new double[]{1.0, 2.0}, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, new double[]{2.0, 3.0}, 1.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, new double[]{3.0, 4.0}, 1.0);
        estimator.measurements[0] = wm1;
        estimator.measurements[1] = wm2;
        estimator.measurements[2] = wm3;

        double rms = estimator.getRMS(problem);
        double expectedRMS = Math.sqrt(wm1.getWeight() * wm1.getResidual() * wm1.getResidual()
                + wm2.getWeight() * wm2.getResidual() * wm2.getResidual()
                + wm3.getWeight() * wm3.getResidual() * wm3.getResidual()) / Math.sqrt(3.0);

        assertEquals(expectedRMS, rms, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        estimator.rows = 3;
        estimator.cols = 2;
        estimator.jacobian = new double[6];
        estimator.residuals = new double[3];
        estimator.measurements = new WeightedMeasurement[3];
        estimator.parameters = new EstimatedParameter[2];
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, new double[]{1.0, 2.0}, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, new double[]{2.0, 3.0}, 1.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, new double[]{3.0, 4.0}, 1.0);
        estimator.measurements[0] = wm1;
        estimator.measurements[1] = wm2;
        estimator.measurements[2] = wm3;

        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = wm1.getResidual() * wm1.getResidual() / wm1.getWeight()
                + wm2.getResidual() * wm2.getResidual() / wm2.getWeight()
                + wm3.getResidual() * wm3.getResidual() / wm3.getWeight();

        assertEquals(expectedChiSquare, chiSquare, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesThrowsException() throws EstimationException {
        estimator.rows = 3;
        estimator.cols = 2;
        estimator.jacobian = new double[6];
        estimator.measurements = new WeightedMeasurement[3];
        estimator.parameters = new EstimatedParameter[2];
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, new double[]{1.0, 2.0}, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, new double[]{2.0, 3.0}, 1.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, new double[]{3.0, 4.0}, 1.0);
        estimator.measurements[0] = wm1;
        estimator.measurements[1] = wm2;
        estimator.measurements[2] = wm3;

        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.rows = 3;
        estimator.cols = 2;
        estimator.jacobian = new double[6];
        estimator.residuals = new double[3];
        estimator.measurements = new WeightedMeasurement[3];
        estimator.parameters = new EstimatedParameter[2];
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, new double[]{1.0, 2.0}, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, new double[]{2.0, 3.0}, 1.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, new double[]{3.0, 4.0}, 1.0);
        estimator.measurements[0] = wm1;
        estimator.measurements[1] = wm2;
        estimator.measurements[2] = wm3;

        double[] errors = estimator.guessParametersErrors(problem);

        double[] expectedErrors = new double[2];
        expectedErrors[0] = Math.sqrt(wm1.getWeight()) * Math.sqrt(wm1.getResidual() * wm1.getResidual() / (3.0 - 2.0));
        expectedErrors[1] = Math.sqrt(wm2.getWeight()) * Math.sqrt(wm2.getResidual() * wm2.getResidual() / (3.0 - 2.0));

        assertArrayEquals(expectedErrors, errors, 0.0);
    }

}
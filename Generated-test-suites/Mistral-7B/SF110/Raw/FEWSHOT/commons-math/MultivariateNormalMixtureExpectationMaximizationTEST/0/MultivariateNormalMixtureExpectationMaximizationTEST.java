package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(2, 2).identityMatrix().transpose().toArray(new RealMatrix[1]))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = emm.getFittedModel();

        assertEquals(5, fittedModel.getComponents().size());

        for (int i = 0; i < fittedModel.getComponents().size(); i++) {
            MultivariateNormalDistribution component = fittedModel.getComponents().get(i).getSecond();
            RealMatrix covMatrix = component.getCovarianceMatrix();
            assertEquals(2, covMatrix.getRowDimension());
            assertEquals(2, covMatrix.getColumnDimension());
            RealMatrix identity = new Array2DRowRealMatrix(2, 2).identityMatrix();
            double tolerance = 1e-5;
            for (int j = 0; j < covMatrix.getRowDimension(); j++) {
                for (int k = 0; k < covMatrix.getColumnDimension(); k++) {
                    double difference = Math.abs(identity.getEntry(j, k) - covMatrix.getEntry(j, k));
                    assertTrue("Expectedcovariance matrix to be close to identity, difference is " + difference, difference < tolerance);
                }
            }
        }

        assertEquals(-0.2662, emm.getLogLikelihood(), 1e-5);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() throws Exception {
        double[][] data = {
                {1.0, 0.0},
                {1.0, 0.0},
                {1.0, 0.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(2, 2).identityMatrix().transpose().toArray(new RealMatrix[1]))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture);

        fail("Expected SingularMatrixException to be thrown");
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() throws Exception {
        double[][] data = {
                {-1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(2, 2).identityMatrix().transpose().toArray(new RealMatrix[1]))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);

        try {
            emm.fit(initialMixture);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0, 5.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(2, 2).identityMatrix().transpose().toArray(new RealMatrix[1]))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);

        try {
            emm.fit(initialMixture);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0},
                {2.5, 3.5},
                {2.0, 3.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(2, 2).identityMatrix().transpose().toArray(new RealMatrix[1]))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);

        try {
            emm.fit(initialMixture);
            fail("Expected NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testEstimate() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);

        assertEquals(3, fittedModel.getComponents().size());

        for (int i = 0; i < fittedModel.getComponents().size(); i++) {
            MultivariateNormalDistribution component = fittedModel.getComponents().get(i).getSecond();
            RealMatrix covMatrix = component.getCovarianceMatrix();
            assertEquals(2, covMatrix.getRowDimension());
            assertEquals(2, covMatrix.getColumnDimension());
            RealMatrix identity = new Array2DRowRealMatrix(2, 2).identityMatrix();
            double tolerance = 1e-5;
            for (int j = 0; j < covMatrix.getRowDimension(); j++) {
                for (int k = 0; k < covMatrix.getColumnDimension(); k++) {
                    double difference = Math.abs(identity.getEntry(j, k) - covMatrix.getEntry(j, k));
                    assertTrue("Expectedcovariance matrix to be close to identity, difference is " + difference, difference < tolerance);
                }
            }
        }

        assertEquals(-0.3845, MultivariateNormalMixtureExpectationMaximization.getLogLikelihood(data, fittedModel), 1e-5);
    }

    @Test
    public void testGetLogLikelihood() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);

        double logLikelihood = MultivariateNormalMixtureExpectationMaximization.getLogLikelihood(data, fittedModel);

        assertEquals(-0.3845, logLikelihood, 1e-5);
    }

    private static double getLogLikelihood(double[][] data, MixtureMultivariateNormalDistribution fittedModel) {
        double logLikelihood = 0.0;
        List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();
        double[] weights = new double[components.size()];
        MultivariateNormalDistribution[] mvns = new MultivariateNormalDistribution[components.size()];

        for (int i = 0; i < components.size(); i++) {
            weights[i] = components.get(i).getFirst();
            mvns[i] = components.get(i).getSecond();
        }

        for (int i = 0; i < data.length; i++) {
            double[] dataRow = data[i];
            double rowDensity = 0.0;
            for (int j = 0; j < components.size(); j++) {
                double density = mvns[j].density(dataRow);
                rowDensity += weights[j] * density;
            }
            logLikelihood += Math.log(rowDensity);
        }

        logLikelihood /= data.length;

        return logLikelihood;
    }
}
package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import org.junit.Assert.*;

import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.covariance.Covariance;
import org.apache.commons.math.stat.covariance.PearsonProductMomentCorrelation;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        Assert.assertNull(correlationMatrix);
        Assert.assertEquals(0, pearsonCorr.nObs);
    }

    @Test
    public void testRectangularArray() {
        double[][] inputData = {{1.0, 1.5, 2.0}, {1.5, 2.0, 2.5}, {2.0, 2.5, 3.0}};
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputData);
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        TDistribution tDist = new TDistributionImpl(pearsonCorr.nObs - 2);

        // Test correlation matrix
        RealMatrix expectedCorrMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        Assert.assertEquals(expectedCorrMatrix, correlationMatrix);

        // Test correlation between variables
        Assert.assertEquals(0.8, pearsonCorr.correlation(new double[]{1.5, 1.5, 1.5}, new double[]{1.5, 1.5, 1.5}), 0.001);
        Assert.assertEquals(0.6, pearsonCorr.correlation(new double[]{1.5, 1.5, 1.5}, new double[]{2.0, 2.0, 2.0}), 0.001);
        Assert.assertEquals(0.8, pearsonCorr.correlation(new double[]{1.5, 1.5, 1.5}, new double[]{1.5, 2.0, 2.5}), 0.001);

        // Test correlation standard errors
        RealMatrix correlationStandardErrors = pearsonCorr.getCorrelationStandardErrors();
        Assert.assertEquals(new BlockRealMatrix(new double[][]{{0.4, 0.32, 0.28}, {0.32, 0.4, 0.36}, {0.28, 0.36, 0.36}}), correlationStandardErrors);

        // Test correlation p-values
        RealMatrix correlationPValues = null;
        try {
            correlationPValues = pearsonCorr.getCorrelationPValues();
        } catch (MathException e) {
            e.printStackTrace();
            fail("An error occurred during p-value computation");
        }

        RealMatrix expectedCorrPValues = new BlockRealMatrix(new double[][]{{0.2865, 0.0545, 0.0458}, {0.0545, 0.2865, 0.1326}, {0.0458, 0.1326, 0.1326}});
        Assert.assertEquals(expectedCorrPValues, correlationPValues);
    }

    @Test
    public void testRealMatrix() {
        double[][] inputData = {{1.0, 1.5, 2.0}, {1.5, 2.0, 2.5}, {2.0, 2.5, 3.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(inputData);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(inputMatrix);

        // Test correlation matrix
        RealMatrix expectedCorrMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        Assert.assertEquals(expectedCorrMatrix, pearsonCorr.getCorrelationMatrix());

        // Test correlation between variables
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < inputData[0].length; i++) {
            regression.addData(inputData[0][i], inputData[1][i]);
        }
        double correlation = regression.getR();
        Assert.assertEquals(correlation, pearsonCorr.correlation(inputData[0], inputData[1]), 0.001);

        // Test error when data is not rectangular
        try {
            new PearsonsCorrelation(new double[][]{1.0, 1.5}, new double[]{1.5, 2.0, 2.5});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCovariance() {
        double[][] inputData = {{1.0, 1.5, 2.0}, {1.5, 2.0, 2.5}, {2.0, 2.5, 3.0}};
        Covariance covariance = new Covariance(inputData);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(covariance);

        // Test correlation matrix
        RealMatrix expectedCorrMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        Assert.assertEquals(expectedCorrMatrix, pearsonCorr.getCorrelationMatrix());

        // Test correlation standard errors and p-values
        RealMatrix correlationStandardErrors = pearsonCorr.getCorrelationStandardErrors();
        RealMatrix correlationPValues = null;
        try {
            correlationPValues = pearsonCorr.getCorrelationPValues();
        } catch (MathException e) {
            e.printStackTrace();
            fail("An error occurred during p-value computation");
        }

        RealMatrix expectedCorrStandardErrors = new BlockRealMatrix(new double[][]{{0.4, 0.32, 0.28}, {0.32, 0.4, 0.36}, {0.28, 0.36, 0.36}});
        RealMatrix expectedCorrPValues = new BlockRealMatrix(new double[][]{{0.2865, 0.0545, 0.0458}, {0.0545, 0.2865, 0.1326}, {0.0458, 0.1326, 0.1326}});
        Assert.assertEquals(expectedCorrStandardErrors, correlationStandardErrors);
        Assert.assertEquals(expectedCorrPValues, correlationPValues);
    }

    @Test
    public void testCovarianceFromCorrelationMatrix() {
        double[][] inputData = {{1.0, 1.5, 2.0}, {1.5, 2.0, 2.5}, {2.0, 2.5, 3.0}};
        RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(correlationMatrix);
        Covariance covariance = new Covariance(pearsonCorr.getCorrelationMatrix());

        RealMatrix expectedCovarianceMatrix = new BlockRealMatrix(new double[][]{{1.5, 1.2, 1.0}, {1.2, 1.8, 1.4}, {1.0, 1.4, 1.5}});
        Assert.assertEquals(expectedCovarianceMatrix, covariance.getCovarianceMatrix());
    }
}
import org.apache.commons.math3.distribution.fitting.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.stat.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testInitialization() {
        try {
            double[][] data = new double[2][3];
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Should have thrown an exception for empty data.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            double[][] data = new double[1][3];
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Should have thrown an exception for data with less than 2 rows.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            double[][] data = new double[2][1];
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Should have thrown an exception for data with less than 2 columns.");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        double[][] data = new double[3][3];
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(mnmem.getFittedModel());
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitThrowsSingularMatrixException() {
        double[][] data = new double[3][3];
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(null, 1, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitThrowsNotStrictlyPositiveException() {
        double[][] data = new double[3][3];
        MultivariateNormalMixtureExpectationModel mnmem = new MultivariateNormalMixtureExpectationModel(data);
        mnmem.fit(null, -1, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitThrowsNumberIsTooSmallException() {
        double[][] data = new double[3][3];
        MultivariateNormalMixtureExpectationModel mnmem = new MultivariateNormalMixtureExpectationModel(data);
        mnmem.fit(null, 1, 0.0);
        mnmem.fit(null, 1, -0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitThrowsDimensionMismatchException() {
        double[][] data = new double[3][3];
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[2][2])))));
        MultivariateNormalMixtureExpectationModel mnmem = new MultivariateNormalMixtureExpectationModel(data);
        mnmem.fit(initialMixture, 1000, 1.0E-5);
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new double[2][2]);
        initialMixture.setComponentDistributions(initialMixture.getComponentDistributions().stream().map(cd -> new Pair<Double, MultivariateNormalDistribution>(cd.getFirst(), mvnd)).collect(Collectors.toList()));
        mnmem.fit(initialMixture);
    }

    @Test
    public void testFit() {
        double[][] data = new double[100][3];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = Math.random();
            }
        }

        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 5);
        MultivariateNormalMixtureExpectationModel mnmem = new MultivariateNormalMixtureExpectationModel(data);
        mnmem.fit(initialMixture);

        double logLikelihoodBefore = mnmem.getLogLikelihood();
        mnmem.fit();
        double logLikelihoodAfter = mnmem.getLogLikelihood();

        assertTrue("Log likelihood should have increased.", logLikelihoodAfter > logLikelihoodBefore);

        double[][] fittedData = mnmem.getFittedModel().getComponents().get(0).getSecond().getMeans();
        Arrays.sort(fittedData[0]);
        Arrays.sort(initialMixture.getComponents().get(0).getSecond().getMeans()[0]);
        for (int i = 0; i < 3; i++) {
            assertEquals("Mean for column " + i + " should be close.", initialMixture.getComponents().get(0).getSecond().getMeans()[0][i], fittedData[0][i], 0.01);
        }

        Covariance covariance = new Covariance(initialMixture.getComponents().get(0).getSecond().getData());
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        RealMatrix fittedCovarianceMatrix = mnmem.getFittedModel().getComponents().get(0).getSecond().getCovarianceMatrix();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals("Covariance for rows " + i + " and " + j + " should be close.", covarianceMatrix.getEntry(i, j), fittedCovarianceMatrix.getEntry(i, j), 0.01);
            }
        }
    }
}
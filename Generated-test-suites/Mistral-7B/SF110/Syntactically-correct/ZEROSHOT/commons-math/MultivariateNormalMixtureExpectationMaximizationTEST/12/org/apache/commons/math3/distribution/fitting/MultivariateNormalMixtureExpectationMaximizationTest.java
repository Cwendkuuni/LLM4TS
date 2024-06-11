import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 private final double[][] testData = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}, {2.0, 2.0}, {1.5, 1.5}, {2.5, 2.5}};

 @Test
 public void testConstructor() {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = null;
 try {
 multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
 } catch (Exception e) {
 e.printStackTrace();
 failure("Constructor should not throw exception");
 }
 assertTrue(multivariateNormalMixtureExpectationMaximization != null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithInvalidSizeData() {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = null;
 multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}});
 failure("Constructor should have thrown NotStrictlyPositiveException");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithInvalidDataShape() {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = null;
 multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}, {1.0, 2.0}});
 failure("Constructor should have thrown DimensionMismatchException");
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithInvalidDataShape2() {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = null;
 multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}, {1.0}});
 failure("Constructor should have thrown NumberIsTooSmallException");
 }

 @Test
 public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.3, new MultivariateNormalDistribution(new NormalDistribution(1.0), new DiagonalCovariance(2.0))), new Pair<>(0.7, new MultivariateNormalDistribution(new NormalDistribution(2.0), new DiagonalCovariance(3.0)))));
 multivariateNormalMixtureExpectationMaximization.fit(initialMixture, 100, 1.0E-5);
 MixtureMultivariateNormalDistribution fittedModel = multivariateNormalMixtureExpectationMaximization.getFittedModel();
 double logLikelihood = multivariateNormalMixtureExpectationMaximization.getLogLikelihood();
 assertTrue(fittedModel != null);
 assertEquals(2, fittedModel.getComponents().size());
 double[][] expectedMeans = new double[][]{{1.5, 1.5}, {2.5, 2.5}};
 for (int i = 0; i < 2; ++i) {
 MultivariateNormalDistribution component = fittedModel.getComponents().get(i).getSecond();
 double[] expectedMean = expectedMeans[i];
 double[] actualMean = component.getMeans();
 assertArrayEquals(expectedMean, actualMean, 0.001);
 }
 double[] expectedCovariances = new double[][]{new double[]{1.0, 1.0}, {1.0, 1.0}};
 for (int i = 0; i < 2; ++i) {
 RealMatrix covarianceMatrix = fittedModel.getComponents().get(i).getSecond().getCovarianceMatrix().getData();
 double[][] actualCovariance = covarianceMatrix.getData();
 assertArrayEquals(expectedCovariances[i], actualCovariance, 0.001);
 }
 assertEquals(0.3, fittedModel.getComponentProbability(0), 0.001);
 assertEquals(0.7, fittedModel.getComponentProbability(1), 0.001);
 assertTrue(Math.abs(logLikelihood) > 0.0);
 }

 @Test
 public void testFitWithDefaultInitialMixture() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
 multivariateNormalMixtureExpectationMaximization.fit(null);
 MixtureMultivariateNormalDistribution fittedModel = multivariateNormalMixtureExpectationMaximization.getFittedModel();
 double logLikelihood = multivariateNormalMixtureExpectationMaximization.getLogLikelihood();
 assertTrue(fittedModel != null);
 assertEquals(2, fittedModel.getComponents().size());
 // Check means
 double[][] expectedMeans = new double[][]{{1.5, 1.5}, {2.5, 2.5}};
 for (int i = 0; i < 2; ++i) {
 MultivariateNormalDistribution component = fittedModel.getComponents().get(i).getSecond();
 double[] expectedMean = expectedMeans[i];
 double[] actualMean = component.getMeans();
 assertArrayEquals(expectedMean, actualMean, 0.001);
 }
 // Check covariances
 double[] expectedCovariances = new double[][]{new double[]{1.0, 1.0}, {1.0, 1.0}};
 for (int i = 0; i < 2; ++i) {
 RealMatrix covarianceMatrix = fittedModel.getComponents().get(i).getSecond().getCovarianceMatrix().getData();
 double[][] actualCovariance = covarianceMatrix.getData();
 assertArrayEquals(expectedCovariances[i], actualCovariance, 0.001);
 }
 assertEquals(0.3, fittedModel.getComponentProbability(0), 0.001);
 assertEquals(0.7, fittedModel.getComponentProbability(1), 0.001);
 assertTrue(Math.abs(logLikelihood) > 0.0);
 }

 @Test
 public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = null;
 MixtureMultivariateNormalDistribution estimatedModel = null;
 try {
 estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(testData, 2);
 multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
 multivariateNormalMixtureExpectationMaximization.fit(estimatedModel);
 } catch (Exception e) {
 e.printStackTrace();
 failure("Methods should not throw exception");
 }
 assertTrue(multivariateNormalMixtureExpectationMaximization != null);
 assertTrue(estimatedModel != null);
 assertEquals(2, estimatedModel.getComponents().size());
 // Check means
 double[] expectedMeans = new double[][]{{1.5, 1.5}, {2.5, 2.5}};
 for (int i = 0; i < 2; ++i) {
 MultivariateNormalDistribution component = estimatedModel.getComponents().get(i).getSecond();
 double[] expectedMean = expectedMeans[i];
 double[] actualMean = component.getMeans();
 assertArrayEquals(expectedMean, actualMean, 0.001);
 }
 // Check covariances
 double[] expectedCovariances = new double[][]{new double[]{1.0, 1.0}, {1.0, 1.0}};
 for (int i = 0; i < 2; ++i) {
 RealMatrix covarianceMatrix = estimatedModel.getComponents().get(i).getSecond().getCovarianceMatrix().getData();
 double[][] actualCovariance = covarianceMatrix.getData();
 assertArrayEquals(expectedCovariances[i], actualCovariance, 0.001);
 }
 assertEquals(0.3, estimatedModel.getComponentProbability(0), 0.001);
 assertEquals(0.7, estimatedModel.getComponentProbability(1), 0.001);
 assertTrue(Math.abs(multivariateNormalMixtureExpectationMaximization.getLogLikelihood()) > 0.0);
 }

 @Test
 public void testGetLogLikelihood() {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.3, new MultivariateNormalDistribution(new NormalDistribution(1.0), new DiagonalCovariance(2.0))), new Pair<>(0.7, new MultivariateNormalDistribution(new NormalDistribution(2.0), new DiagonalCovariance(3.0)))));
 multivariateNormalMixtureExpectationMaximization.fit(initialMixture);
 double logLikelihood = multivariateNormalMixtureExpectationMaximization.getLogLikelihood();
 assertTrue(Math.abs(logLikelihood) > 0.0);
 }

 @Test
 public void testGetFittedModel() {
 MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.3, new MultivariateNormalDistribution(new NormalDistribution(1.0), new DiagonalCovariance(2.0))), new Pair<>(0.7, new MultivariateNormalDistribution(new NormalDistribution(2.0), new DiagonalCovariance(3.0)))));
 multivariateNormalMixtureExpectationMaximization.fit(initialMixture);
 MixtureMultivariateNormalDistribution fittedModel = multivariateNormalMixtureExpectationMaximization.getFittedModel();
 assertTrue(fittedModel != null);
 assertEquals(2, fittedModel.getComponents().size());
 }
}
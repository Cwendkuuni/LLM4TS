import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 @Test
 public void testFitWithValidDataAndInitialMixture() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.3, 0.3), 1.0, new IdentityMatrix(2))),
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.7, 0.7), 1.0, new IdentityMatrix(2))) ));
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, 10, 1e-5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception thrown during fit method call");
 }
 MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
 double logLikelihood = mmeme.getLogLikelihood();
 assertTrue(fittedModel.getComponents().size() == 2);
 assertArrayEquals(initialMixture.getComponents().get(0).getSecond().getMeans(), fittedModel.getComponents().get(0).getSecond().getMeans(), 0.001);
 assertArrayEquals(initialMixture.getComponents().get(1).getSecond().getMeans(), fittedModel.getComponents().get(1).getSecond().getMeans(), 0.001);
 assertEquals(logLikelihood, 1.154689287529243, 0.001);
 }

 @Test
 public void testFitWithValidDataAndEmptyInitialMixture() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.emptyList());
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, 10, 1e-5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception thrown during fit method call");
 }
 MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
 double logLikelihood = mmeme.getLogLikelihood();
 assertTrue(fittedModel.getComponents().size() > 0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitWithInvalidDataLength() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.3, 0.3), 1.0, new IdentityMatrix(2))),
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.7, 0.7), 1.0, new IdentityMatrix(2))) ));
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, 10, 1e-5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception thrown: " + e.getMessage());
 }
 fail("Exception not thrown when invalid data length is provided");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testFitWithInvalidDataRowLength() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0}, {1.5, 2.5, 3.0}, {2.0, 2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.3, 0.3), 1.0, new IdentityMatrix(2))),
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.7, 0.7), 1.0, new IdentityMatrix(2))) ));
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, 10, 1e-5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception thrown: " + e.getMessage());
 }
 fail("Exception not thrown when invalid data row length is provided");
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testFitWithInvalidDataPointLength() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.3, 0.3), 1.0, new IdentityMatrix(2))),
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.7, 0.7), 1.0, new IdentityMatrix(2))) ));
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, 10, 1e-5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception thrown: " + e.getMessage());
 }
 fail("Exception not thrown when invalid data point length is provided");
 }

 @Test(expected = SingularMatrixException.class)
 public void testFitWithSingularCovarianceMatrix() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.3, 0.3), 1.0, new SingularValueDecomposition(new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}}).getU()))),
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.7, 0.7), 1.0, new IdentityMatrix(2))) ));
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, 10, 1e-5);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitWithInvalidMaxIterations() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.3, 0.3), 1.0, new IdentityMatrix(2))),
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.7, 0.7), 1.0, new IdentityMatrix(2))) ));
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, -1, 1e-5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception thrown: " + e.getMessage());
 }
 fail("Exception not thrown when invalid max iterations is provided");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitWithInvalidThreshold() {
 MultivariateNormalMixtureExpectationMaximization mmeme;
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.3, 0.3), 1.0, new IdentityMatrix(2))),
 new MultivariateNormalDistribution(new RealVectorValue(new DoublePoint(0.7, 0.7), 1.0, new IdentityMatrix(2))) ));
 try {
 mmeme = new MultivariateNormalMixtureExpectationMaximization(testData);
 mmeme.fit(initialMixture, 10, -1);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception thrown: " + e.getMessage());
 }
 fail("Exception not thrown when invalid threshold is provided");
 }

 @Test
 public void testEstimateWithValidData() {
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 int numComponents = 2;
 MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(testData, numComponents);
 MixtureMultivariateNormalDistribution fittedModel = new MultivariateNormalMixtureExpectationMaximization(testData, numComponents).getFittedModel();
 assertEquals(expectedModel, fittedModel);
 }

 @Test
 public void testEstimateWithInvalidNumComponents() {
 double[][] testData = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 try {
 MultivariateNormalMixtureExpectationMaximization.estimate(testData, -1);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception not thrown");
 }
 fail("Exception not thrown when invalid numComponents is provided");
 }

 @Test
 public void testEstimateWithEmptyData() {
 double[][] testData = {};
 try {
 MultivariateNormalMixtureExpectationMaximization.estimate(testData, 2);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception not thrown");
 }
 fail("Exception not thrown when empty data is provided");
 }

 @Test
 public void testEstimateWithInvalidDataLength() {
 double[][] testData = { {1.0}, {1.5, 2.5}, {2.0, 2.0}, {2.5, 2.5} };
 try {
 MultivariateNormalMixtureExpectationMaximization.estimate(testData, 2);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception not thrown");
 }
 fail("Exception not thrown when invalid data length is provided");
 }
}
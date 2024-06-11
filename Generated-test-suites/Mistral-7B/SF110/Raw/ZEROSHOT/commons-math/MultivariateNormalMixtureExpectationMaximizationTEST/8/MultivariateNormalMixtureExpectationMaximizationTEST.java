import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 @Test
 public void testConstructor() {
 Double dataMatrix[][] = new Double[][]{{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}};
 try {
 MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(dataMatrix.clone());
 } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
 e.printStackTrace();
 fail("Exception should not be thrown when constructing MultivariateNormalMixtureExpectationMaximization object.");
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithEmptyDataMatrix() {
 new MultivariateNormalMixtureExpectationMaximization(new Double[][]{});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithDataMatrixOfDifferentColumnSize() {
 Double dataMatrix[][] = new Double[][]{{1.0, 2.0, 3.0}, {1.1, 2.1}, {1.2, 2.2, 3.2}};
 try {
 new MultivariateNormalMixtureExpectationMaximization(dataMatrix.clone());
 fail("DimensionMismatchException should be thrown when constructing MultivariateNormalMixtureExpectationMaximization object with a data matrix of different column size.");
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithDataMatrixOfRowsWithLessThanTwoColumns() {
 Double dataMatrix[][] = new Double[][]{{1.0}, {2.0, 3.0}, {1.2, 2.2, 3.2}};
 try {
 new MultivariateNormalMixtureExpectationMaximization(dataMatrix.clone());
 fail("NumberIsTooSmallException should be thrown when constructing MultivariateNormalMixtureExpectationMaximization object with a data matrix of rows with fewer than two columns.");
 } catch (NumberIsTooSmallException e) {
 // Expected exception
 }
 }

 @Test
 public void testFitWithInitialMixtureAndMaxIterationsAndThreshold() {
 // Prepare data
 Double dataMatrix[][] = new Double[][]{{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4}, {1.5, 2.5, 3.5}};
 MultivariateNormalDistribution component1 = new MultivariateNormalDistribution(new double[]{1.0, 1.0, 1.0}, new Array2DRowRealMatrix(3, 3).identityMatrix());
 MultivariateNormalDistribution component2 = new MultivariateNormalDistribution(new double[]{2.0, 2.0, 2.0}, new Array2DRowRealMatrix(3, 3).identityMatrix());
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<MultivariateNormalDistribution, Double>(component1, 0.3), new Pair<MultivariateNormalDistribution, Double>(component2, 0.7)));
 MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(dataMatrix.clone());

 // Test fit method with initial mixture, max iterations, and threshold
 mmeme.fit(initialMixture, 10, 1.0E-5);

 // Check fitted model
 MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
 List<Pair<MultivariateNormalDistribution, Double>> components = fittedModel.getComponents();
 assertEquals(2, components.size());
 assertEquals(component1, components.get(0).getSecond());
 assertEquals(component2, components.get(1).getSecond());
 }

 @Test
 public void testFitWithInitialMixture() {
 // Prepare data
 Double dataMatrix[][] = new Double[][]{{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4}, {1.5, 2.5, 3.5}};
 MultivariateNormalDistribution component1 = new MultivariateNormalDistribution(new double[]{1.0, 1.0, 1.0}, new Array2DRowRealMatrix(3, 3).identityMatrix());
 MultivariateNormalDistribution component2 = new MultivariateNormalDistribution(new double[]{2.0, 2.0, 2.0}, new Array2DRowRealMatrix(3, 3).identityMatrix());
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<MultivariateNormalDistribution, Double>(component1, 0.3), new Pair<MultivariateNormalDistribution, Double>(component2, 0.7)));
 MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(dataMatrix.clone());

 // Test fit method without specifying max iterations and threshold
 mmeme.fit(initialMixture);

 // Check fitted model
 MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
 List<Pair<MultivariateNormalDistribution, Double>> components = fittedModel.getComponents();
 assertEquals(2, components.size());
 assertEquals(component1, components.get(0).getSecond());
 assertEquals(component2, components.get(1).getSecond());
 }

 @Test
 public void testEstimate() {
 // Prepare data
 Double dataMatrix[][] = new Double[][]{{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4}, {1.5, 2.5, 3.5}};

 // Test estimate method
 MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(dataMatrix.clone(), 3);

 // Check estimated model
 assertEquals(3, estimatedModel.getComponents().size());
 }

 @Test
 public void testGetLogLikelihood() {
 // Prepare data and fitted model
 Double dataMatrix[][] = new Double[][]{{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4}, {1.5, 2.5, 3.5}};
 MultivariateNormalDistribution component1 = new MultivariateNormalDistribution(new double[]{1.0, 1.0, 1.0}, new Array2DRowRealMatrix(3, 3).identityMatrix());
 MultivariateNormalDistribution component2 = new MultivariateNormalDistribution(new double[]{2.0, 2.0, 2.0}, new Array2DRowRealMatrix(3, 3).identityMatrix());
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<MultivariateNormalDistribution, Double>(component1, 0.3), new Pair<MultivariateNormalDistribution, Double>(component2, 0.7)));
 MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(dataMatrix.clone());
 mmeme.fit(initialMixture);

 // Test getLogLikelihood method
 double logLikelihood = mmeme.getLogLikelihood();
 assertTrue(logLikelihood > -1000.0);
 }
}
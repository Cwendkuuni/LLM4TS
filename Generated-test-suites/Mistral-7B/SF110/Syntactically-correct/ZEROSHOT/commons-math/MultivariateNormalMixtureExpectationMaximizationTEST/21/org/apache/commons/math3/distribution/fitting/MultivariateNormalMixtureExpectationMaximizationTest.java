import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 private MultivariateNormalMixtureExpectationMaximization model;
 private double[][] testData;

 @Before
 public void setUp() {
 testData = new double[][]{{1.0, 2.0}, {1.5, 1.5}, {2.5, 3.0}, {2.0, 2.5}, {3.0, 3.5}};
 try {
 model = new MultivariateNormalMixtureExpectationMaximization(testData);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize model");
 }
 }

 @Test
 public void testConstructor() {
 assertEquals(5, model.getData().length);
 for (int i = 0; i < testData.length; i++) {
 assertEquals(testData[i].length, model.getData()[i].length);
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testEmptyData() {
 new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDataWithIncorrectLength() {
 new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}, {1.5, 1.5, 1.5}});
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testDataWithIncorrectNumberOfColumns() {
 new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}, {1.5, 1.5}, {3.0}});
 }

 @Test
 public void testFit() throws Exception {
 final int numComponents = 3;
 final MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(testData, numComponents);
 model.fit(initialMixture);

 final MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
 final List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();

 assertEquals(numComponents, components.size());
 for (Pair<Double, MultivariateNormalDistribution> component : components) {
 assertEquals(1.0 / numComponents, component.getFirst(), 0.01);
 }

 final Covariance covariance = new Covariance(testData);
 final RealMatrix expectedCovMat = covariance.getCovarianceMatrix().getData();
 for (int i = 0; i < numComponents; i++) {
 final MultivariateNormalDistribution mvn = components.get(i).getSecond();
 final RealMatrix covMat = mvn.getCovarianceMatrix().getData();
 assertArrayEquals(expectedCovMat[i], covMat.getData(), 0.01);
 }
 }

 @Test
 public void testFitWithDefaults() throws Exception {
 final MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(testData, 3);
 model.fit(initialMixture);

 final MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
 final List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();

 assertEquals(3, components.size());
 for (Pair<Double, MultivariateNormalDistribution> component : components) {
 assertEquals(1.0 / 3.0, component.getFirst(), 0.01);
 }

 final Covariance covariance = new Covariance(testData);
 final RealMatrix expectedCovMat = covariance.getCovarianceMatrix().getData();
 for (int i = 0; i < 3; i++) {
 final MultivariateNormalDistribution mvn = components.get(i).getSecond();
 final RealMatrix covMat = mvn.getCovarianceMatrix().getData();
 assertArrayEquals(expectedCovMat[i], covMat.getData(), 0.01);
 }
 }

 @Test
 public void testGetLogLikelihood() throws Exception {
 final double[][] data = {{-1.0, -1.0}, {1.0, 1.0}, {0.5, 0.5}, {2.0, 2.0}, {1.5, 1.5}};
 model = new MultivariateNormalMixtureExpectationMaximization(data);
 model.fit(MultivariateNormalMixtureExpectationMaximization.estimate(data, 3));
 double logLikelihood = model.getLogLikelihood();
 assertTrue(logLikelihood > -1000);
 }
}
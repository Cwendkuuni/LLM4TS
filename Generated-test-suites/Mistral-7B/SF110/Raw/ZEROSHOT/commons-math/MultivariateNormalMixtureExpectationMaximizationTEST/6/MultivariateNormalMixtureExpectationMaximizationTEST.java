import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 private MultivariateNormalMixtureExpectationMaximization model;
 private double[][] data;

 @Before
 public void setUp() {
 double[][] testData = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {1.5, 3.5}, {2.5, 3.5}, {2.0, 2.5}};
 try {
 model = new MultivariateNormalMixtureExpectationMaximization(testData);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize model.");
 }
 }

 @Test
 public void testInitializeModel() {
 assertEquals(6, model.data.length);
 for (int i = 0; i < model.data.length; i++) {
 assertEquals(2, model.data[i].length);
 }
 assertNotNull(model.fittedModel);
 assertEquals(0.0, model.logLikelihood, 0.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInitializeModelWithEmptyData() {
 double[][] emptyData = new double[0][];
 try {
 new MultivariateNormalMixtureExpectationMaximization(emptyData);
 } catch (Exception e) {
 assertEquals("The length of the data matrix must be strictly positive.", e.getMessage());
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testInitializeModelWithDataWithInvalidLength() {
 double[][] invalidData = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {1.5, 3.5}, {2.5, 3.5}, {2.0, 2.5}, {1.0, 2.0, 3.0}};
 try {
 new MultivariateNormalMixtureExpectationMaximization(invalidData);
 } catch (Exception e) {
 fail("Expected DimensionMismatchException but got " + e.getClass().getName());
 }
 }

 @Test
 public void testFitWithValidInitialMixtureAndParameters() {
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>(2));
 initialMixture.getComponents().get(0).setSecond(new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)));
 initialMixture.getComponents().get(1).setSecond(new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(2, 2)));

 try {
 model.fit(initialMixture, 1000, 1.0E-5);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to fit model.");
 }

 assertNotNull(model.fittedModel);
 assertEquals(2, model.fittedModel.getComponents().size());
 for (int i = 0; i < model.fittedModel.getComponents().size(); i++) {
 MultivariateNormalDistribution component = model.fittedModel.getComponents().get(i).getSecond();
 assertEquals(2, component.getMeans().length);
 assertEquals(2, component.getCovarianceMatrix().getRowDimension());
 assertEquals(2, component.getCovarianceMatrix().getColumnDimension());
 }
 }

 @Test(expected = SingularMatrixException.class)
 public void testFitWithSingularCovarianceMatrix() {
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>(2));
 initialMixture.getComponents().get(0).setSecond(new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2).scalarMultiply(0.0)));
 initialMixture.getComponents().get(1).setSecond(new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(2, 2)));

 try {
 model.fit(initialMixture, 1000, 1.0E-5);
 } catch (Exception e) {
 assertEquals("The covariance matrix of component " + 0 + " is singular.", e.getMessage());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitWithNegativeNumberOfIterations() {
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>(2));
 initialMixture.getComponents().get(0).setSecond(new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)));
 initialMixture.getComponents().get(1).setSecond(new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(2, 2)));

 try {
 model.fit(initialMixture, -1000, 1.0E-5);
 } catch (Exception e) {
 assertEquals("The number of iterations must be strictly positive.", e.getMessage());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitWithNegativeThreshold() {
 MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>(2));
 initialMixture.getComponents().get(0).setSecond(new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)));
 initialMixture.getComponents().get(1).setSecond(new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(2, 2)));

 try {
 model.fit(initialMixture, 1000, -1.0E-5);
 } catch (Exception e) {
 assertEquals("The threshold must be strictly positive.", e.getMessage());
 }
 }

 @Test
 public void testGetLogLikelihood() {
 double logLikelihood = model.getLogLikelihood();
 assertEquals(-3.141592653589793, logLikelihood, 0.0);
 }

 @Test
 public void testGetFittedModel() {
 MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
 assertEquals(2, fittedModel.getComponents().size());
 for (int i = 0; i < fittedModel.getComponents().size(); i++) {
 MultivariateNormalDistribution component = fittedModel.getComponents().get(i).getSecond();
 assertEquals(2, component.getMeans().length);
 assertEquals(2, component.getCovarianceMatrix().getRowDimension());
 assertEquals(2, component.getCovarianceMatrix().getColumnDimension());
 }
 }
}
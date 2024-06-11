import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 private final static double[][] TEST_DATA = {
  {1.0, 2.0, 3.0},
  {1.1, 2.1, 3.1},
  {1.2, 2.2, 3.2},
  {1.3, 2.3, 3.3},
  {1.4, 2.4, 3.4}
 };

 @Test
 public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
  MixtureMultivariateNormalDistribution expected = new MultivariateNormalMixtureDistribution(2, new double[]{0.5, 0.5}, new double[][]{{{0.1, 0.1, 0.1}, {0.2, 0.2, 0.2}}, {{0.3, 0.3, 0.3}, {0.4, 0.4, 0.4}}});
  MixtureMultivariateNormalDistribution actual = MultivariateNormalMixtureExpectationMaximization.estimate(TEST_DATA, 2);
  assertEquals(expected.getComponents().size(), actual.getComponents().size());
  assertEquals(expected.getComponents().get(0).getFirst(), actual.getComponents().get(0).getFirst(), 1e-6);
  assertEquals(expected.getComponents().get(0).getSecond().getMeans(), actual.getComponents().get(0).getSecond().getMeans(), 1e-6);
  assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), actual.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), 1e-6);
  assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 1), actual.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 1), 1e-6);
  assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 0), actual.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 0), 1e-6);
  assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), actual.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), 1e-6);
  assertEquals(expected.getComponents().get(1).getFirst(), actual.getComponents().get(1).getFirst(), 1e-6);
  assertEquals(expected.getComponents().get(1).getSecond().getMeans(), actual.getComponents().get(1).getSecond().getMeans(), 1e-6);
  assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), actual.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), 1e-6);
  assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 1), actual.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 1), 1e-6);
  assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 0), actual.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 0), 1e-6);
  assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), actual.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), 1e-6);
 }

 @Test
 public void testFit() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
  MixtureMultivariateNormalDistribution initialModel = new MultivariateNormalMixtureDistribution(2, new double[]{0.5, 0.5}, new double[][]{{{0.1, 0.1, 0.1}, {0.2, 0.2, 0.2}}, {{0.3, 0.3, 0.3}, {0.4, 0.4, 0.4}}});
  MultivariateNormalMixtureExpectationMaximization testee = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
  testee.fit(initialModel);
  MixtureMultivariateNormalDistribution actual = testee.getFittedModel();
  double expectedLogLikelihood = 1.552297365491584;
  assertEquals(expectedLogLikelihood, testee.getLogLikelihood(), 1e-6);
  assertEquals(initialModel.getComponents().size(), actual.getComponents().size());
  for (int i = 0; i < actual.getComponents().size(); i++) {
   MultivariateNormalDistribution actualComponent = actual.getComponents().get(i).getSecond();
   MultivariateNormalDistribution expectedComponent = initialModel.getComponents().get(i).getSecond();
   assertEquals(expectedComponent.getMeans(), actualComponent.getMeans(), 1e-6);
   RealMatrix expectedCovariance = expectedComponent.getCovarianceMatrix();
   RealMatrix actualCovariance = actualComponent.getCovarianceMatrix();
   assertEquals(expectedCovariance.getEntry(0, 0), actualCovariance.getEntry(0, 0), 1e-6);
   assertEquals(expectedCovariance.getEntry(0, 1), actualCovariance.getEntry(0, 1), 1e-6);
   assertEquals(expectedCovariance.getEntry(1, 0), actualCovariance.getEntry(1, 0), 1e-6);
   assertEquals(expectedCovariance.getEntry(1, 1), actualCovariance.getEntry(1, 1), 1e-6);
   assertEquals(expectedComponent.getProbabilityDensityFunction().value(new DoubleVector(TEST_DATA[0])), actualComponent.getProbabilityDensityFunction().value(new DoubleVector(TEST_DATA[0])), 1e-6);
  }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNewInstanceWithEmptyData() throws NotStrictlyPositiveException {
  new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testNewInstanceWithDataOfWrongSize() throws NotStrictlyPositiveException, DimensionMismatchException {
  new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {1.1, 3.0}});
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNewInstanceWithDataOfWrongDimensionality() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
  new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}, {1.1, 2.0}});
 }

 @Test(expected = ConvergenceException.class)
 public void testFitWithMaxIterationsAndThresholdNotReached() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
  MixtureMultivariateNormalDistribution initialModel = new MultivariateNormalMixtureDistribution(2, new double[]{0.5, 0.5}, new double[][]{{{0.1, 0.1, 0.1}, {0.2, 0.2, 0.2}}, {{0.3, 0.3, 0.3}, {0.4, 0.4, 0.4}}});
  MultivariateNormalMixtureExpectationMaximization testee = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
  testee.fit(initialModel, 1, 0.0);
 }
}
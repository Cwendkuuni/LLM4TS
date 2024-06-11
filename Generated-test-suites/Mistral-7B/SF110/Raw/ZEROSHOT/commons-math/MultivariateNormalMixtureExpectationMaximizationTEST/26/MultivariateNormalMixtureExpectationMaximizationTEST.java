import org.apache.commons.math3.distribution.fitting.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 private final static double[][] data1D = { { 1.0, 2.0, 3.0, 4.0, 5.0 }, { 1.1, 2.1, 3.1, 4.1, 5.1 } };
 private final static double[][] data2D = { { { 1.0, 2.0 }, { 1.1, 2.1 } }, { { 1.2, 2.2 }, { 1.3, 2.3 } } };
 private final static double[][] data3D = { { { { 1.0, 2.0 } }, { { 1.1, 2.1 } } }, { { { 1.2, 2.2 } }, { { 1.3, 2.3 } } } };

 @Test
 public void testInitWithInvalidData() {
     expectException(NotStrictlyPositiveException.class, () -> new MultivariateNormalMixtureExpectationMaximization(new double[0][0]));
     expectException(DimensionMismatchException.class, () -> new MultivariateNormalMixtureExpectationMaximization(new double[1][2][3]));
 }

 @Test
 public void testInitWithValidData() {
     MultivariateNormalMixtureExpectationMaximization mnm;
     mnm = new MultivariateNormalMixtureExpectationMaximization(data1D);
     assertEquals(2, mnm.data.length);
     assertEquals(5, mnm.data[0].length);
     mnm = new MultivariateNormalMixtureExpectationMaximization(data2D);
     assertEquals(2, mnm.data.length);
     assertEquals(2, mnm.data[0].length);
 }

 @Test
 public void testFitWithInvalidInitialMixture() {
     expectException(NotStrictlyPositiveException.class, () -> new MultivariateNormalMixtureDistribution(Collections.emptyList()));
     expectException(DimensionMismatchException.class, () -> new MultivariateNormalMixtureDistribution(Arrays.asList(new NormalDistribution(1.0), new NormalDistribution(2.0, new RealMatrix(new double[2][2])))));
 }

 @Test
 public void testFitWithValidInitialMixtureAndValidMaxIterationsAndThreshold() {
     MixtureMultivariateNormalDistribution initialMixture = new MultivariateNormalMixtureDistribution(Arrays.asList(
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2])))),
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2]))))));
     MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data2D);
     mnm.fit(initialMixture, 1000, 1.0E-5);
     assertEquals(2, mnm.getFittedModel().getComponents().size());
 }

 @Test
 public void testFitWithValidInitialMixtureAndInvalidMaxIterations() {
     MixtureMultivariateNormalDistribution initialMixture = new MultivariateNormalMixtureDistribution(Arrays.asList(
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2])))),
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2]))))));
     expectException(NotStrictlyPositiveException.class, () -> mnm.fit(initialMixture, -1));
 }

 @Test
 public void testFitWithValidInitialMixtureAndInvalidThreshold() {
     MixtureMultivariateNormalDistribution initialMixture = new MultivariateNormalMixtureDistribution(Arrays.asList(
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2])))),
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2]))))));
     expectException(NotStrictlyPositiveException.class, () -> mnm.fit(initialMixture, 1000, Double.NEGATIVE_INFINITY));
 }

 @Test
 public void testFitWithValidInitialMixture() {
     MixtureMultivariateNormalDistribution initialMixture = new MultivariateNormalMixtureDistribution(Arrays.asList(
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2])))),
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2]))))));
     MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data2D);
     mnm.fit(initialMixture);
     assertEquals(2, mnm.getFittedModel().getComponents().size());
 }

 @Test
 public void testEstimate() {
     MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data2D, 2);
     assertEquals(2, result.getComponents().size());
 }

 @Test
 public void testEstimateWithInvalidNumberOfComponents() {
     expectException(NumberIsTooSmallException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(data2D, 1));
     expectException(NumberIsTooLargeException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(data2D, 3));
 }

 @Test
 public void testEstimateWithEmptyData() {
     expectException(NotStrictlyPositiveException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(new double[0][], 2));
 }

 @Test
 public void testEstimateWithDataSmallerThanNumberOfComponents() {
     expectException(NotStrictlyPositiveException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(new double[1][], 2));
 }

 @Test
 public void testGetLogLikelihood() {
     MixtureMultivariateNormalDistribution initialMixture = new MultivariateNormalMixtureDistribution(Arrays.asList(
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2])))),
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2]))))));
     MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data2D);
     mnm.fit(initialMixture);
     assertEquals(-1.0, mnm.getLogLikelihood(), 0.001);
 }

 @Test
 public void testGetFittedModel() {
     MixtureMultivariateNormalDistribution initialMixture = new MultivariateNormalMixtureDistribution(Arrays.asList(
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2])))),
             new MultivariateNormalDistribution(new Pair<>(0.5, new MultivariateNormalDistribution(new double[2], new Array2DRowRealMatrix(new double[2][2]))))));
     MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data2D);
     mnm.fit(initialMixture);
     MixtureMultivariateNormalDistribution result = mnm.getFittedModel();
     assertEquals(2, result.getComponents().size());
 }
}
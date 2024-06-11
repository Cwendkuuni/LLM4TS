package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 private MultivariateNormalMixtureExpectationMaximization model;
 private double[][] testData;

 @Before
 public void setUp() throws NotStrictlyPositiveException, DimensionMismatchException {
 testData = new double[][]{{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4}};
 model = new MultivariateNormalMixtureExpectationMaximization(testData);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithEmptyData() {
 testData = new double[][]{{}};
 assertThrows(NotStrictlyPositiveException.class, () -> new MultivariateNormalMixtureExpectationMaximization(testData));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithMismatchedData() {
 testData = new double[][]{{1.0, 2.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3}, {1.4, 2.4, 3.4}};
 assertThrows(DimensionMismatchException.class, () -> new MultivariateNormalMixtureExpectationMaximization(testData));
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithDataOfWrongSize() {
 testData = new double[][]{{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}};
 assertThrows(NumberIsTooSmallException.class, () -> new MultivariateNormalMixtureExpectationMaximization(testData));
 }

 @Test
 public void testFitWithDefaults() {
 MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2);
 model.fit(initialModel);
 assertEquals(initialModel.getComponents().size(), model.getFittedModel().getComponents().size());
 for (int i = 0; i < initialModel.getComponents().size(); ++i) {
 MultivariateNormalDistribution component = initialModel.getComponents().get(i).getSecond();
 MultivariateNormalDistribution fittedComponent = model.getFittedModel().getComponents().get(i).getSecond();
 assertEquals(component.getMeans().length, fittedComponent.getMeans().length);
 assertArrayEquals(component.getMeans(), fittedComponent.getMeans());
 assertArrayEquals(component.getCovarianceMatrix().getData(), fittedComponent.getCovarianceMatrix().getData());
 }
 }

 @Test
 public void testFitWithCustomParameters() throws SingularMatrixException, NotStrictlyPositiveException {
 MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(3);
 model.fit(initialModel, 500, 1.0E-6);
 assertEquals(initialModel.getComponents().size(), model.getFittedModel().getComponents().size());
 for (int i = 0; i < initialModel.getComponents().size(); ++i) {
 MultivariateNormalDistribution component = initialModel.getComponents().get(i).getSecond();
 MultivariateNormalDistribution fittedComponent = model.getFittedModel().getComponents().get(i).getSecond();
 assertEquals(component.getMeans().length, fittedComponent.getMeans().length);
 assertArrayEquals(component.getMeans(), fittedComponent.getMeans());
 assertArrayEquals(component.getCovarianceMatrix().getData(), fittedComponent.getCovarianceMatrix().getData());
 }
 }

 @Test
 public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
 double[][] testData = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}, {1.3, 2.3}, {1.4, 2.4}, {1.5, 2.5}, {1.6, 2.6}, {1.7, 2.7}, {1.8, 2.8}, {1.9, 2.9}};
 MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(testData, 3);
 assertEquals(3, estimatedModel.getComponents().size());
 for (int i = 0; i < 3; ++i) {
 MultivariateNormalDistribution component = estimatedModel.getComponents().get(i).getSecond();
 assertNotNull(component);
 assertArrayEquals(new double[]{0.1, 0.1}, component.getMeans());
 }
 }

 @Test
 public void testGetLogLikelihood() {
 double expectedLogLikelihood = 0.0;
 for (int i = 0; i < testData.length; ++i) {
 expectedLogLikelihood += Math.log(model.density(testData[i]));
 }
 expectedLogLikelihood /= testData.length;
 assertEquals(expectedLogLikelihood, model.getLogLikelihood(), 1e-5);
 }

}
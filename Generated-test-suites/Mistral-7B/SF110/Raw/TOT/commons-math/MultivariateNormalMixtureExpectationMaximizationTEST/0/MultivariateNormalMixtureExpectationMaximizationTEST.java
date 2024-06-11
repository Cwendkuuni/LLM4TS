package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.*;
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
 testData = new double[][] {
 {1.0, 2.0},
 {1.5, 1.8},
 {2.0, 2.5},
 {2.5, 3.0},
 {3.0, 3.5}
 };
 model = new MultivariateNormalMixtureExpectationMaximization(testData);
 }

 @Test
 public void testFitWithInitialModelAndDefaultParameters() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
 MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new double[] {0.5, 0.5}), new CovarianceMatrix(new double[][] {
 {1.0, 0.5},
 {0.5, 1.0}
 }))));
 model.fit(initialModel);
 MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
 assertFalse(fittedModel.isIdentifiable());
 double logLikelihood = model.getLogLikelihood();
 assertEquals("Log likelihood should be positive.", logLikelihood, model.getLogLikelihood(), 1e-5);
 }

 @Test(expected = SingularMatrixException.class)
 public void testFitWithSingularCovarianceMatrix() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
 MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new MultivariateNormalDistribution(new RealVectorValue(new double[] {0.5, 0.5}), new CovarianceMatrix(new double[][] {
 {0.0, 0.5},
 {0.5, 0.0}
 }))));
 model.fit(initialModel);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitWithNegativeWeights() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
 MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(Arrays.asList(
 new Pair<Double, MultivariateNormalDistribution>(-1.0, new MultivariateNormalDistribution(new RealVectorValue(new double[] {0.5, 0.5}), new CovarianceMatrix(new double[][] {
 {1.0, 0.5},
 {0.5, 1.0}
 })))));
 model.fit(initialModel);
 }

 @Test
 public void testFitWithDefaultParameters() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
 MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution();
 model.fit(initialModel);
 MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
 assertFalse(fittedModel.isIdentifiable());
 double logLikelihood = model.getLogLikelihood();
 assertEquals("Log likelihood should be positive.", logLikelihood, model.getLogLikelihood(), 1e-5);
 }

 @Test
 public void testEstimateWithDefaultParameters() throws NotStrictlyPositiveException, DimensionMismatchException {
 double[][] data = {
 {1.0, 2.0},
 {1.5, 1.8},
 {2.0, 2.5},
 {2.5, 3.0},
 {3.0, 3.5}
 };
 MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
 assertFalse(fittedModel.isIdentifiable());
 }

 @Test
 public void testEstimateWithSpecifiedNumberOfComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
 double[][] data = {
 {1.0, 2.0},
 {1.5, 1.8},
 {2.0, 2.5},
 {2.5, 3.0},
 {3.0, 3.5},
 {4.0, 4.0},
 {5.0, 5.0}
 };
 MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
 assertFalse(fittedModel.isIdentifiable());
 }

 @After
 public void tearDown() {
 model = null;
 testData = null;
 }

}
import org.apache.commons.math3.distribution.fitting.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

 @Test
 public void testInitialization() {
 MultivariateNormalMixtureExpectationMaximization mmme;
 try {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
 mmme = new MultivariateNormalMixtureExpectationMaximization(data);
 } catch (Exception e) {
 fail("Initialization failed with exception: " + e.getMessage());
 }
 assertNotNull(mmme.getData());
 assertNotNull(mmme.getFittedModel());
 assertNotNull(mmme.getLogLikelihood());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInitializationFailsWithInvalidDataLength() {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {0.0}};
 try {
 new MultivariateNormalMixtureExpectationMaximization(data);
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testInitializationFailsWithInvalidDataDimension() {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {3.0, 2.0, 1.0}};
 try {
 new MultivariateNormalMixtureExpectationMaximization(data);
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testInitializationFailsWithInvalidDataRowLength() {
 double[][] data = new double[][]{{1.0}, {2.0, 3.0}, {3.0, 1.0}};
 try {
 new MultivariateNormalMixtureExpectationMaximization(data);
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testFitWithValidInitialMixture() {
 MultivariateNormalMixtureExpectationMaximization mmme;
 try {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {2.5, 2.5}};
 MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
 mmme = new MultivariateNormalMixtureExpectationMaximization(data);
 mmme.fit(initialMixture);
 } catch (Exception e) {
 fail("Fitting failed with exception: " + e.getMessage());
 }
 assertNotNull(mmme.getFittedModel());
 }

 @Test(expected = SingularMatrixException.class)
 public void testFitFailsWithSingularMatrixException() {
 MultivariateNormalMixtureExpectationMaximization mmme;
 try {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {2.5, 2.5}, {2.5, 2.5}};
 MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
 mmme = new MultivariateNormalMixtureExpectationMaximization(data);
 mmme.fit(initialMixture, 100, 1.0E-5);
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitFailsWithInvalidMaxIterations() {
 MultivariateNormalMixtureExpectationMaximization mmme;
 try {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {2.5, 2.5}};
 MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
 mmme = new MultivariateNormalMixtureExpectationMaximization(data);
 mmme.fit(initialMixture, -1, 1.0E-5);
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFitFailsWithInvalidThreshold() {
 MultivariateNormalMixtureExpectationMaximization mmme;
 try {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {2.5, 2.5}};
 MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
 mmme = new MultivariateNormalMixtureExpectationMaximization(data);
 mmme.fit(initialMixture, 1000, -1.0);
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testFitWithValidInitialMixtureAndParameters() {
 MultivariateNormalMixtureExpectationMaximization mmme;
 try {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {2.5, 2.5}};
 MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
 mmme = new MultivariateNormalMixtureExpectationMaximization(data);
 mmme.fit(initialMixture, 1000, 1.0E-5);
 } catch (Exception e) {
 fail("Fitting failed with exception: " + e.getMessage());
 }
 MixtureMultivariateNormalDistribution fittedModel = mmme.getFittedModel();
 assertNotNull(fittedModel);
 assertEquals(2, fittedModel.getComponents().size());
 }

 @Test
 public void testGetLogLikelihood() {
 MultivariateNormalMixtureExpectationMaximization mmme;
 try {
 double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {2.5, 2.5}};
 MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
 mmme = new MultivariateNormalMixtureExpectationMaximization(data);
 mmme.fit(initialMixture, 1000, 1.0E-5);
 double logLikelihood = mmme.getLogLikelihood();
 assertNotNull(logLikelihood);
 } catch (Exception e) {
 fail("Getting log likelihood failed with exception: " + e.getMessage());
 }
 }

}
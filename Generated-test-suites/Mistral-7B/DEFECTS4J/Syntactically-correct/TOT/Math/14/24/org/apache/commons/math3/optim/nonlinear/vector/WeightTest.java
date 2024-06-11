package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class WeightTest {

 // Test constructor with null weight
 @Test(expected = IllegalArgumentException.class)
 public void testWeightConstructorNullWeight() {
 Weight weight = new Weight(null);
 }

 // Test constructor with empty weight
 @Test(expected = IllegalArgumentException.class)
 public void testWeightConstructorEmptyWeight() {
 double[] emptyWeight = {};
 Weight weight = new Weight(emptyWeight);
 }

 // Test constructor with valid diagonal weight
 @Test
 public void testWeightConstructorDiagonalWeight() {
 double[] weight = {1.0, 2.0, 3.0};
 Weight weight = new Weight(weight);
 RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 // Test constructor with non-diagonal weight matrix
 @Test(expected = NonSquareMatrixException.class)
 public void testWeightConstructorNonSquareWeight() {
 double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
 RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
 Weight weight = new Weight(weightMatrix);
 }

 // Test constructor with valid square weight matrix
 @Test
 public void testWeightConstructorSquareWeightMatrix() {
 double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
 RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
 Weight weight = new Weight(weightMatrix);
 RealMatrix expectedWeight = weightMatrix.copy();
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 // Test getWeight method with diagonal weight
 @Test
 public void testGetWeightDiagonalWeight() {
 double[] weight = {1.0, 2.0, 3.0};
 Weight weight = new Weight(weight);
 RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 // Test getWeight method with non-diagonal weight matrix
 @Test
 public void testGetWeightNonDiagonalWeight() {
 double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
 RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
 Weight weight = new Weight(weightMatrix);
 RealMatrix expectedWeight = weightMatrix.copy();
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 // Test getWeight method with singular weight matrix
 @Test(expected = SingularValueDecomposition.SingularMatrixException.class)
 public void testGetWeightSingularWeight() {
 double[][] weight = {{1.0, 1.0}, {1.0, 1.0}};
 RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
 Weight weight = new Weight(weightMatrix);
 weight.getWeight();
 }

 // Test setWeight method (not applicable since the class is immutable)
 // @Test(expected = UnsupportedOperationException.class)
 // public void testSetWeight() {
 // Weight weight = new Weight(new Array2DRowRealMatrix(3, 3));
 // weight.setWeight(new Array2DRowRealMatrix(3, 3));
 // }
}
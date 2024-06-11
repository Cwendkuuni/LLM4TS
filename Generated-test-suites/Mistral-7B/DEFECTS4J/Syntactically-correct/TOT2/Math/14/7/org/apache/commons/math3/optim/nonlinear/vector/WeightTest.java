package org.apache.commons.math3.optimization;

import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

@Test
public void testWeightWithDoubleArray() {
double[] weight = {1.0, 2.0, 3.0};
Weight weightObj = new Weight(weight);
RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
expectedWeightMatrix.setEntry(0, 0, 1.0);
expectedWeightMatrix.setEntry(1, 1, 2.0);
expectedWeightMatrix.setEntry(2, 2, 3.0);
RealMatrix actualWeightMatrix = weightObj.getWeight();
assertNotNull(actualWeightMatrix);
assertEquals(expectedWeightMatrix, actualWeightMatrix);
}

@Test
public void testWeightWithRealMatrix() {
double[][] weight = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3, weight);
Weight weightObj = new Weight(weightMatrix);
RealMatrix expectedWeightMatrix = weightMatrix.copy();
RealMatrix actualWeightMatrix = weightObj.getWeight();
assertNotNull(actualWeightMatrix);
assertEquals(expectedWeightMatrix, actualWeightMatrix);
}

@Test
public void testWeightWithNull() {
double[] weight = null;
IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Weight(weight));
assertEquals("The array must not be null", exception.getMessage());
}

@Test
public void testWeightWithEmptyDoubleArray() {
double[] weight = {};
Weight weightObj = new Weight(weight);
assertThrows(IllegalArgumentException.class, () -> weightObj.getWeight());
}

@Test
public void testWeightWithEmptyRealMatrix() {
RealMatrix weightMatrix = new Array2DRowRealMatrix(0, 0);
IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Weight(weightMatrix));
assertEquals("The weight matrix must not be empty", exception.getMessage());
}

@Test
public void testWeightWithNonSquareRealMatrix() {
double[][] weight = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 1.0}};
RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3, weight);
NonSquareMatrixException exception = assertThrows(NonSquareMatrixException.class, () -> new Weight(weightMatrix));
assertEquals("Argument weight.getColumnDimension() != weight.getRowDimension()", exception.getMessage());
}
}
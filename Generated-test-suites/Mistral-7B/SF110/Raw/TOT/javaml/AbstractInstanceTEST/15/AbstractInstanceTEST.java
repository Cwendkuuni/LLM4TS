package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

@Test
public void testGetID() {
AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
int expectedID = 1;
int actualID = instance.getID();
assertEquals(expectedID, actualID);
}

@Test
public void testSubtractInstance() {
AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5});
AbstractInstance result = instance1.minus(instance2);

Double[] expectedResult = new Double[]{0.5, 0.5, 0.5};
for (int i = 0; i < result.noAttributes(); ++i) {
assertEquals(expectedResult[i], result.get(i), 0.001);
}
}

@Test
public void testDivideByValue() {
AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
Double expectedResult;

// Test division by a positive number
expectedResult = new Double[]{0.5, 1.0, 1.5};
AbstractInstance result = instance.divide(2.0);
for (int i = 0; i < result.noAttributes(); ++i) {
assertEquals(expectedResult[i], result.get(i), 0.001);
}

// Test division by zero
try {
instance.divide(0.0);
fail("Expected an ArithmeticException");
} catch (ArithmeticException e) {
// Expected
}
}
}
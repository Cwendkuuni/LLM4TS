package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class AbstractSimplexTest {

 @Test
 public void testAbstractSimplexWithZeroDimension() {
 try {
 new AbstractSimplex(0);
 fail("Should not be able to create a AbstractSimplex with a negative dimension");
 } catch (NegativeArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithZeroLengthStep() {
 try {
 new AbstractSimplex(new double[0]);
 fail("Should not be able to create a AbstractSimplex with zero length steps");
 } catch (ZeroException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithEqualVertices() {
 try {
 new AbstractSimplex(new double[] { 0.1, 0.1, 0.1 });
 fail("Should not be able to create a AbstractSimplex with equal vertices");
 } catch (MathIllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithInvalidReferenceSimplex() {
 try {
 new AbstractSimplex(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } });
 fail("Should not be able to create a AbstractSimplex with an invalid reference simplex");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithValidParameters() {
 final int dimension = 3;
 final double sideLength = 1.0;
 final AbstractSimplex simplex = new AbstractSimplex(dimension, sideLength);
 assertEquals(dimension, simplex.getDimension());
 assertEquals(dimension + 1, simplex.getSize());
 }

 @Test
 public void testAbstractSimplexWithInvalidStartConfiguration() {
 try {
 new AbstractSimplex(new double[] { 1.0, 1.0, 0.0 });
 fail("Should not be able to create a AbstractSimplex with an invalid start configuration");
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithValidStartConfiguration() {
 final int dimension = 3;
 final double sideLength = 1.0;
 final double[] steps = new double[] { sideLength, sideLength, sideLength };
 final AbstractSimplex simplex = new AbstractSimplex(steps);
 assertEquals(dimension, simplex.getDimension());
 assertEquals(dimension + 1, simplex.getSize());
 for (int i = 0; i < dimension; ++i) {
 final double[] vertexI = simplex.getStartConfiguration()[i];
 Arrays.equals(steps, Arrays.copyOfRange(vertexI, 0, steps.length));
 }
 }

 @Test
 public void testAbstractSimplexWithReferenceSimplexFailure() {
 try {
 new AbstractSimplex(new double[][] { { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 } });
 fail("Should not be able to create a AbstractSimplex with an invalid reference simplex");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithValidReferenceSimplex() {
 final int dimension = 3;
 final AbstractSimplex simplex = new AbstractSimplex(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } });
 assertEquals(dimension, simplex.getDimension());
 assertEquals(dimension + 1, simplex.getSize());
 for (int i = 0; i < dimension; ++i) {
 final double[] confI = simplex.getStartConfiguration()[i];
 Arrays.equals(new double[] { -confI[i][0], -confI[i][1], -confI[i][2] }, new double[] { 1.0, 0.0, 0.0 });
 }
 }

 @Test
 public void testGetDimension() {
 final int dimension = 3;
 final AbstractSimplex simplex = new AbstractSimplex(dimension);
 assertEquals(dimension, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 final int dimension = 3;
 final AbstractSimplex simplex = new AbstractSimplex(dimension);
 assertEquals(dimension + 1, simplex.getSize());
 }
}
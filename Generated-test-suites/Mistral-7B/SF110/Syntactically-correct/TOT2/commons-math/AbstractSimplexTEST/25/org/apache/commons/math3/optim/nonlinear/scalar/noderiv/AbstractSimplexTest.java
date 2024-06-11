package org.example;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.OptimizationFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithValidArguments() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexWithValidArgumentsAndSideLength() {
        AbstractSimplex simplex = new AbstractSimplex(3, 5.0);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexWithValidStepArray() {
        double[] steps = new double[] {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexWithValidReferenceSimplex() {
        double[][] referenceSimplex = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        Assert.assertEquals(2, simplex.getDimension());
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithZeroSteps() {
        double[] steps = new double[0];
        AbstractSimplex simplex = new AbstractSimplex(steps);
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexWithNullSteps() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexWithEqualVerticesInSimplex() {
        double[][] referenceSimplex = new double[][] {{1.0, 2.0}, {1.0, 2.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testAbstractSimplexWithNegativeSteps() {
        double[] steps = new double[] {-1.0, 1.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithInvalidReferenceSimplexLength() {
        double[][] referenceSimplex = new double[0][];
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        Assert.assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterateMethod() {
        OptimizationFunction function = new MockMultivariateFunction();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(function, comparator);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testIterateMethodWithEmptySimplex() {
        OptimizationFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        AbstractSimplex simplex = new AbstractSimplex(0);
        simplex.iterate(function, comparator);
    }

    @Test(expected = NullArgumentException.class)
    public void testIterateMethodWithNullComparator() {
        OptimizationFunction function = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(function, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testIterateMethodWithNullMultivariateFunction() {
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(null, comparator);
    }

    @Test
    public void testBuildMethod() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[] {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        Assert.assertEquals(4, points.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildMethodWithInvalidStartPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[] {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testBuildMethodWithNullStartPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(null);
    }

    @Test
    public void testEvaluateMethod() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        simplex.evaluate(function, comparator);
    }

    @Test(expected = NullArgumentException.class)
    public void testEvaluateMethodWithNullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = mock(MultivariateFunction.class);
        simplex.evaluate(function, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testEvaluateMethodWithNullMultivariateFunction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        simplex.evaluate(null, comparator);
    }

}
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValuesRealVectorFunction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Comparator;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private double[][] testPoints;
    private double[] testFunctionValues;
    private Comparator<PointValuePair> comparator;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        testPoints = new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}};
        testFunctionValues = new double[]{1.0, 0.5, 1.2, 0.8, 0.6};
        comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
    }

    @After
    public void tearDown() {
        optimizer = null;
        testPoints = null;
        testFunctionValues = null;
        comparator = null;
    }

    @Test
    public void testDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertEquals(2.0, md.khi, 0.001);
        assertEquals(0.5, md.gamma, 0.001);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, md.khi, 0.001);
        assertEquals(0.7, md.gamma, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructorNegativeKhi() {
        new MultiDirectional(-1.0, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructorNegativeGamma() {
        new MultiDirectional(2.0, -0.5);
    }

    @Test
    public void testIterateSimplex_noImprovement() throws Exception {
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleValuesRealVectorFunction(testPoints[0], testFunctionValues[0]), comparator, new RealConvergenceChecker(1e-6));
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(testPoints[0], best.getPointRef(), 0.001);
        assertEquals(testFunctionValues[0], best.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplex_reflection_improvement() throws Exception {
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleValuesRealVectorFunction(testPoints[1], testFunctionValues[1]), comparator, new RealConvergenceChecker(1e-6));
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(testPoints[1], best.getPointRef(), 0.001);
        assertEquals(testFunctionValues[1], best.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplex_reflection_noImprovement() throws Exception {
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleValuesRealVectorFunction(testPoints[2], testFunctionValues[2]), comparator, new RealConvergenceChecker(1e-6));
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(testPoints[2], best.getPointRef(), 0.001);
        assertEquals(testFunctionValues[2], best.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplex_contraction() throws Exception {
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleValuesRealVectorFunction(testPoints[3], testFunctionValues[3]), comparator, new RealConvergenceChecker(1e-6));
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(testPoints[3], best.getPointRef(), 0.001);
        assertEquals(testFunctionValues[3], best.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplex_expansion() throws Exception {
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleValuesRealVectorFunction(testPoints[4], testFunctionValues[4]), comparator, new RealConvergenceChecker(1e-6));
        PointValuePair best = optimizer.getBestPoint();
        assertEquals(testPoints[4], best.getPointRef(), 0.001);
        assertEquals(testFunctionValues[4], best.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplex_functionEvaluationException() throws Exception {
        optimizer.setMaxIterations(10);
        SimpleValuesRealVectorFunction function = new SimpleValuesRealVectorFunction(testPoints[0], Double.NaN);
        optimizer.optimize(function, comparator, new RealConvergenceChecker(1e-6));
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplex_optimizationException() throws Exception {
        optimizer.setMaxIterations(0);
        optimizer.optimize(new SimpleValuesRealVectorFunction(testPoints[0], testFunctionValues[0]), comparator, new RealConvergenceChecker(1e-6));
    }
}
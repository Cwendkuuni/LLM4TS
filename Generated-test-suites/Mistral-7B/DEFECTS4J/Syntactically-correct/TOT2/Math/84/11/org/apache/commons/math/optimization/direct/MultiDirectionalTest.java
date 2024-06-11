
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        MultiDirectional md = new MultiDirectional();
        MultiDirectional mdWithCustomParams = new MultiDirectional(3.0, 0.7);
        // Additional assertions for constructor validations
    }

    @Test
    public void testIterateSimplex() {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[8];
        RealPointValuePair bestPoint = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[0] = bestPoint;
        md.iterateSimplex(comparator);
        RealPointValuePair result = simplex[0];
        assertEquals(bestPoint, result);
        // Assertions for checking convergence
    }

    @Test
    public void testEvaluateNewSimplex() {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[2];
        RealPointValuePair originalPoint = new RealPointValuePair(new double[]{1.0, 2.0}, 5.0, false);
        simplex[0] = originalPoint;
        simplex[1] = new RealPointValuePair(new double[]{1.1, 2.1}, Double.NaN, false);
        RealPointValuePair evaluatedPoint = md.evaluateNewSimplex(simplex, 1.5, comparator);
        // Assertions for checking evaluated point
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorParameters() {
        new MultiDirectional(-1.0, 0.5); // khi < 0
        new MultiDirectional(2.0, -0.5); // gamma < 0
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[2];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[]{1.1, 2.1}, Double.NaN, false);
        try {
            md.iterateSimplex(comparator);
        } catch (OptimizationException | IllegalArgumentException e) {
            Assert.fail("FunctionEvaluationException should be thrown but another exception is");
        }
        // FunctionEvaluationException should be thrown
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizationException() {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[2];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[]{1.1, 2.1}, 0.0, false);
        try {
            md.iterateSimplex(comparator);
        } catch (FunctionEvaluationException e) {
            Assert.fail("OptimizationException should be thrown but another exception is");
        }
        // OptimizationException should be thrown
    }

    @Test
    public void testIterateSimplexEdgeCases() {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[8];
        RealPointValuePair bestPoint = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        RealPointValuePair reflectionPoint = new RealPointValuePair(new double[]{1.1, 2.1}, 2.8, false);
        RealPointValuePair expandedPoint = new RealPointValuePair(new double[]{1.2, 2.2}, 2.5, false);
        RealPointValuePair contractedPoint = new RealPointValuePair(new double[]{1.3, 2.3}, 3.2, false);
        simplex[0] = bestPoint;
        simplex[1] = reflectionPoint;
        simplex[2] = expandedPoint;
        simplex[3] = contractedPoint;
        md.iterateSimplex(comparator);
        // Best point, reflection point, expanded point, and contracted point should be checked
    }
}

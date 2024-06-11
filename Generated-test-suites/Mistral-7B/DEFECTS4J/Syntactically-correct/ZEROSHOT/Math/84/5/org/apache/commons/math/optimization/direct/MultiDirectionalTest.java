import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MultiDirectionalTest extends SimpleOptimizationTest {

    private MultiDirectional optimizer;
    private PointValuePairCollection pvpCollection;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional(2.0, 0.5);
        pvpCollection = new PointValuePairCollection();
    }

    @Test
    public void testIterateSimplex() throws Exception {
        // Test with a simple function
        optimizer.setInitialPoint(new AveragedStartingPointInitializer(1).getInitialPoint(2));
        optimizer.setFunction(this::testFunction);
        optimizer.setMaximumIterations(10);
        optimizer.optimize();

        PointValuePair best = optimizer.getSolution();
        double expected = Math.sqrt(2.0) / 2.0;
        assertEquals(expected, best.getPoint()[0], 1e-6);
        assertEquals(expected * Math.sqrt(2.0), best.getValue(), 1e-6);

        // Test with a function that has multiple minima
        optimizer.setInitialPoint(new double[]{1.0, 1.0});
        optimizer.setFunction(this::testFunction2);
        optimizer.setMaximumIterations(10);
        optimizer.optimize();

        PointValuePair best2 = optimizer.getSolution();
        double expected2 = Math.sqrt(2.0) / 2.0;
        assertEquals(expected2, best2.getPoint()[0], 1e-6);
        assertEquals(expected2 * Math.sqrt(2.0), best2.getValue(), 1e-6);

        // Test with a function that has a large gradient in one direction
        optimizer.setInitialPoint(new double[]{1.0, 1.0e10});
        optimizer.setFunction(this::testFunction3);
        optimizer.setMaximumIterations(10);
        optimizer.optimize();

        PointValuePair best3 = optimizer.getSolution();
        double expected3 = 1.0;
        assertEquals(expected3, best3.getPoint()[0], 1e-6);
        assertEquals(0.0, best3.getValue(), 1e-6);
    }

    private double testFunction(double[] x) {
        return x[0] * x[0] + x[1] * x[1];
    }

    private double testFunction2(double[] x) {
        return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
    }

    private double testFunction3(double[] x) {
        return x[0] * Math.exp(-x[0] * x[0]) + x[1] * Math.exp(-x[1] * x[1]);
    }

}
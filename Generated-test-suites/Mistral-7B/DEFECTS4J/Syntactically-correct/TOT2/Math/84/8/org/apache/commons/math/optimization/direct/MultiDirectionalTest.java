import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testConstructorDefault() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertEquals(2.0, multiDirectional.khi, 0.01);
        assertEquals(0.5, multiDirectional.gamma, 0.01);
    }

    @Test
    public void testConstructorCustom() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, multiDirectional.khi, 0.01);
        assertEquals(0.7, multiDirectional.gamma, 0.01);
    }

    @Test
    public void testIterateSimplexBasic() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[4];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 5.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 7.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 2.0}, 4.0);
        original[3] = new RealPointValuePair(new double[]{4.0, 1.0}, 6.0);

        multiDirectional.iterateSimplex(Comparator.comparingDouble(p -> p.getValue()));

        RealPointValuePair best = multiDirectional.simplex[0];
        assertEquals(5.0, best.getValue(), 0.01);
        assertEquals(1.0, best.getPointRef()[0], 0.01);
        assertEquals(2.0, best.getPointRef()[1], 0.01);
    }

    // Add test cases for other methods and edge cases here.
}
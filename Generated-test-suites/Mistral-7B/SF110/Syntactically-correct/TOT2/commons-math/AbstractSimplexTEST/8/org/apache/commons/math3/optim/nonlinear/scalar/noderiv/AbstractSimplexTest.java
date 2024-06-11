import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;

public class AbstractSimplexTest {

    @Test
    public void testConstructorWithIntAndDouble() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        Assert.assertEquals(2, simplex.getDimension());
        simplex = new AbstractSimplex(2, 1.0);
        Assert.assertEquals(2, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNull() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorWithZeroDimension() {
        new AbstractSimplex(0);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorWithZeroLengthSteps() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedDimension() {
        new AbstractSimplex(new double[]{1.0, 2.0, 3.0, 4.0});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithEqualVertices() {
        new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
    }
}
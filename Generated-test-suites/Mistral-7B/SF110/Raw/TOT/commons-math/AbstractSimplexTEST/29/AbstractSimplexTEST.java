package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

    private int dimension;
    private double[] steps;

    public AbstractSimplexTest(int dimension, double[] steps) {
        this.dimension = dimension;
        this.steps = steps;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1, new double[]{1.0}},
                {2, new double[]{1.0, 1.0}},
                {3, new double[]{1.0, 1.0, 1.0}}
        });
    }

    @Test
    public void testAbstractSimplexConstruction() {
        AbstractSimplex simplex;
        if (this.dimension > 0) {
            simplex = new AbstractSimplex(this.dimension, this.steps);
        } else {
            simplex = new AbstractSimplex(this.dimension);
        }
        assertEquals(this.dimension, simplex.getDimension());
        assertEquals(this.dimension + 1, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullArguments() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroArguments() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidStartConfiguration() {
        new AbstractSimplex(new double[]{1.0, 1.0, 2.0});
    }

    @Test(expected = EqualVerticesException.class)
    public void testAbstractSimplexEqualVertices() {
        new AbstractSimplex(new double[]{1.0, 1.0, 0.0});
    }
}

class EqualVerticesException extends RuntimeException {
    public EqualVerticesException(String message) {
        super(message);
    }
}
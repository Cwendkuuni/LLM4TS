import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    private static final double DELTA = 1e-9;
    private DiscreteDistribution<Double> distribution;

    @Before
    public void setUp() throws NotPositiveException, NotStrictlyPositiveException {
        List<Pair<Double, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.1),
                new Pair<>(2.0, 0.3),
                new Pair<>(3.0, 0.2),
                new Pair<>(4.0, 0.4),
                new Pair<>(5.0, 0.05)
        );
        distribution = new DiscreteDistribution<>(new Well19937c(), samples.stream().map(p -> new Pair<>(p.getKey().doubleValue(), p.getValue())).toList());
    }

    @Test
    public void testProbabilityMethod() {
        Assert.assertEquals(0.1, distribution.probability(1.0), DELTA);
        Assert.assertEquals(0.3, distribution.probability(2.0), DELTA);
        Assert.assertEquals(0.2, distribution.probability(3.0), DELTA);
        Assert.assertEquals(0.4, distribution.probability(4.0), DELTA);
        Assert.assertEquals(0.05, distribution.probability(5.0), DELTA);
        Assert.assertEquals(0.0, distribution.probability(6.0), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNegativeValue() {
        distribution.probability(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNullValue() {
        distribution.probability(null);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Double, Double>> samples = distribution.getSamples();
        Assert.assertEquals(5, samples.size());
        Assert.assertEquals(1.0, samples.get(0).getKey(), DELTA);
        Assert.assertEquals(0.1, samples.get(0).getValue(), DELTA);
        Assert.assertEquals(2.0, samples.get(1).getKey(), DELTA);
        Assert.assertEquals(0.3, samples.get(1).getValue(), DELTA);
        Assert.assertEquals(3.0, samples.get(2).getKey(), DELTA);
        Assert.assertEquals(0.2, samples.get(2).getValue(), DELTA);
        Assert.assertEquals(4.0, samples.get(3).getKey(), DELTA);
        Assert.assertEquals(0.4, samples.get(3).getValue(), DELTA);
        Assert.assertEquals(5.0, samples.get(4).getKey(), DELTA);
        Assert.assertEquals(0.05, samples.get(4).getValue(), DELTA);
    }

    @Test
    public void testSampleMethod() {
        Double sample = distribution.sample();
        List<Double> samplesList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            samplesList.add(distribution.sample());
        }
        double sum = 0.0;
        for (Double value : samplesList) {
            sum += distribution.probability(value);
        }
        Assert.assertEquals(1.0, sum, 1e-6);
        Assert.assertNotEquals(sample, distribution.sample());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSampleSize() {
        distribution.sample(-1);
    }

}
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        population = new ElitisticListPopulation(chromosomes, 20, 0.9);
    }

    @Test
    public void testConstructorWithListAndLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(this.population.getChromosomes(), 20, 0.9);
        Assert.assertEquals(this.population.size(), population.size());
    }

    @Test
    public void testConstructorWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(20, 0.9);
        Assert.assertEquals(this.population.size(), population.size());
    }

    @Test
    public void testNextGeneration_ElitismRateGreaterThanZeroAndLessThanOne() {
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(this.population.size(), nextGeneration.size());
        Assert.assertTrue(Precision.isClose(this.population.getElitismRate(), nextGeneration.getElitismRate(), 0.01));
    }

    @Test(expected = OutOfRangeException.class)
    public void testNextGeneration_ElitismRateLessThanZero() {
        ElitisticListPopulation population = new ElitisticListPopulation(this.population.getChromosomes(), 20, -0.1);
        population.nextGeneration();
    }

    @Test(expected = OutOfRangeException.class)
    public void testNextGeneration_ElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(this.population.getChromosomes(), 20, 1.1);
        population.nextGeneration();
    }

    @Test
    public void testSetElitismRate_ElitismRateGreaterThanZeroAndLessThanOne() {
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRate_ElitismRateLessThanZero() {
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRate_ElitismRateGreaterThanOne() {
        population.setElitismRate(1.1);
    }

    static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        public double[] getGenotype() {
            return new double[0];
        }

        public double[] getPhenotype() {
            return new double[]{id};
        }

        public double[] getFitnesses() {
            return new double[]{id};
        }

        public int getSize() {
            return 0;
        }

        public void mutate(double mutationProbability) {}

        public String toString() {
            return "DummyChromosome_" + id;
        }
    }

}

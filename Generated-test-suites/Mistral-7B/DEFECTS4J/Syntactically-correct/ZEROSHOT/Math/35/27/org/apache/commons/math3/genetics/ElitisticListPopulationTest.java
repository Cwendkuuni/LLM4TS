import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial chromosomes list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithInitialChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        List<Chromosome> initialChromosomes = List.of(chromosome1, chromosome2, chromosome3);

        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, populationLimit, elitismRate);

        Assert.assertEquals(populationLimit, population.size());
        Assert.assertTrue(population.containsAll(initialChromosomes));
    }

    /**
     * Test constructor with population limit.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        Assert.assertEquals(populationLimit, population.size());
    }

    /**
     * Test nextGeneration method with initial list of chromosomes.
     */
    @Test
    public void testNextGenerationWithInitialChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        List<Chromosome> initialChromosomes = List.of(chromosome1, chromosome2, chromosome3);

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 10, 0.5);

        Population nextGeneration = population.nextGeneration();

        Assert.assertEquals(10, nextGeneration.size());
        int boundIndex = (int) Math.ceil((1.0 - 0.5) * initialChromosomes.size());
        List<Chromosome> expectedNextGeneration = initialChromosomes.subList(boundIndex, initialChromosomes.size());
        Assert.assertTrue(nextGeneration.containsAll(expectedNextGeneration));
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        population.nextGeneration();
    }

    /**
     * Test setElitismRate method.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);

        double newElitismRate = 0.7;
        population.setElitismRate(newElitismRate);

        Assert.assertEquals(newElitismRate, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with out-of-range value.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithOutOfRangeValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);

        double newElitismRate = 1.2;
        population.setElitismRate(newElitismRate);
    }

}

class DummyChromosome implements Chromosome {
    private int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double getFitness() {
        return id;
    }

    @Override
    public void evolve() {
        // Not used in this test
    }

    @Override
    public int getSize() {
        return 0;
    }
}
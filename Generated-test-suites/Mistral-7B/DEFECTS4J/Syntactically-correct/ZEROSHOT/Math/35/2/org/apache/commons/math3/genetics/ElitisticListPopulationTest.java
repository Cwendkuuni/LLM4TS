import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreation() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = -0.1;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationGreaterElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 1.1;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testElitisticListPopulationAddChromosome() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        Chromosome chromosome = new MockChromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.size());
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Chromosome bestChromosome = new MockChromosome();
        Chromosome secondBestChromosome = new MockChromosome();
        chromosomes.add(bestChromosome);
        chromosomes.add(secondBestChromosome);
        Collections.sort(chromosomes);

        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(populationLimit, nextGeneration.size());
        int boundIndex = (int) FastMath.ceil((1.0 - elitismRate) * chromosomes.size());
        for (int i=boundIndex; i<chromosomes.size(); i++) {
            Chromosome chromosome = nextGeneration.get(i);
            assertTrue(chromosome instanceof Chromosome);
            assertNotSame(bestChromosome, chromosome);
            assertNotSame(secondBestChromosome, chromosome);
        }
        for (int i=0; i<boundIndex; i++) {
            Chromosome chromosome = nextGeneration.get(i);
            assertTrue(chromosome instanceof Chromosome);
            assertEquals(chromosomes.get(i), chromosome);
        }
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, population.getElitismRate(), 0.01);
    }

}

class MockChromosome implements Chromosome {
    // empty implementation for testing purpose
}
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreation() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Population population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationNegativeElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, -0.2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, 1.2);
    }

    @Test
    public void testElitisticListPopulationCreationEmptyPopulation() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Population population = new ElitisticListPopulation(chromosomes, 0, 0.5);
        assertEquals(0, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.addChromosome(new DoubleChromosome(1.0));
        population.addChromosome(new DoubleChromosome(2.0));
        population.addChromosome(new DoubleChromosome(3.0));
        population.addChromosome(new DoubleChromosome(4.0));
        population.addChromosome(new DoubleChromosome(5.0));
        population.addChromosome(new DoubleChromosome(6.0));
        population.addChromosome(new DoubleChromosome(7.0));
        population.addChromosome(new DoubleChromosome(8.0));
        population.addChromosome(new DoubleChromosome(9.0));
        population.addChromosome(new DoubleChromosome(10.0));

        Collections.sort(population.getChromosomes());

        Population nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.01);

        Chromosome bestChromosome = population.getChromosomes().get(0);
        Chromosome copiedChromosome = nextGeneration.getChromosomes().get(0);
        assertEquals(bestChromosome, copiedChromosome);

        int boundIndex = (int) Math.ceil((1.0 - 0.5) * population.getChromosomes().size());
        for (int i = boundIndex; i < population.getChromosomes().size(); i++) {
            Chromosome chromosome = nextGeneration.getChromosomes().get(i);
            Chromosome originalChromosome = population.getChromosomes().get(i);
            assertNotEquals(bestChromosome, chromosome);
            assertTrue(originalChromosome.getFitness() > chromosome.getFitness());
        }
    }

    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(0, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.01);
        assertTrue(nextGeneration.isEmpty());
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(1.2);
    }
}
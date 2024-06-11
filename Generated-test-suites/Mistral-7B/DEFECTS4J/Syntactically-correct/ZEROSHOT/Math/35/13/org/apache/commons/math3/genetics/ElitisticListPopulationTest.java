import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new DummyChromosome(i));
        }

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        assertEquals(15, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testNegativeElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Collections.sort(population.getChromosomes());

        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(15, nextGeneration.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(population.getChromosome(i), nextGeneration.getChromosome(i));
        }

        for (int i = 6; i < nextGeneration.size(); i++) {
            assertNotEquals(population.getChromosome(i), nextGeneration.getChromosome(i));
        }
    }

    @Test
    public void testSetElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(1.1);
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyChromosome " + id;
        }
    }

}
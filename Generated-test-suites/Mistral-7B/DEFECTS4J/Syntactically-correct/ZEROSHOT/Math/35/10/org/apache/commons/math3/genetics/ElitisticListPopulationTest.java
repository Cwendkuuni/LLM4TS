import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testElitisticListPopulationCreationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new DummyChromosome(i));
        }

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.9);
        Population nextGeneration = population.nextGeneration();

        // check the size of the next generation
        assertEquals(15, nextGeneration.getPopulationLimit());
        assertEquals(12, nextGeneration.size());

        // check the elitism part
        ElitisticListPopulation elitisticNextGeneration = (ElitisticListPopulation) nextGeneration;
        for (int i = 0; i < 3; i++) {
            Chromosome elitisticChromosome = elitisticNextGeneration.getChromosome(i);
            Chromosome elitisticChromosomeInOldPopulation = population.findChromosome(elitisticChromosome);
            assertEquals(elitisticChromosomeInOldPopulation, elitisticChromosome);
        }

        // check the mutated part
        for (int i = 3; i < nextGeneration.size(); i++) {
            Chromosome mutatedChromosome = nextGeneration.getChromosome(i);
            Chromosome elitisticChromosomeInOldPopulation = population.findChromosome(mutatedChromosome);
            assertNotEquals(elitisticChromosomeInOldPopulation, mutatedChromosome);
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(1.1);
    }

}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double[] getGenotype() {
        return new double[0];
    }

    @Override
    public void setGenotype(double[] genotype) {

    }

    @Override
    public int getFitness() {
        return id;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public Chromosome mutate() {
        return new DummyChromosome(id + 1);
    }

    @Override
    public Chromosome clone() {
        return new DummyChromosome(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.id == ((DummyChromosome) obj).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
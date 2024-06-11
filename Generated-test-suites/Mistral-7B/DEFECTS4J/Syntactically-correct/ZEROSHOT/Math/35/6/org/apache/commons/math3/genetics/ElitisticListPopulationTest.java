import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithInitialChromosomesAndLimit() {
        List<Chromosome> chromosomes = createChromosomesList(10);
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 20, 0.9);
        assertEquals(20, elitisticListPopulation.size());
    }

    @Test
    public void testElitisticListPopulationWithInitialLimitAndElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, elitisticListPopulation.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithNegativeElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithElitismRateGreaterThanOne() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 1.1);
    }

    @Test
    public void testNextGenerationWithInitialChromosomes() {
        List<Chromosome> chromosomes = createChromosomesList(10);
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 20, 0.9);
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(20, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithInitialPopulationLimitAndElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(10, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithElitismRateLessThanOne() {
        List<Chromosome> chromosomes = createChromosomesList(15);
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 20, 0.8);
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(12, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = createChromosomesList(10);
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 1.1);
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(10, nextGeneration.size());
    }

    @Test
    public void testSetElitismRateWithValidElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        elitisticListPopulation.setElitismRate(0.8);
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        elitisticListPopulation.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithElitismRateGreaterThanOne() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        elitisticListPopulation.setElitismRate(1.1);
    }

    private List<Chromosome> createChromosomesList(int size) {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        return chromosomes;
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double[] getGenes() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public void mutate(double mutationProbability) throws OutOfRangeException {
            // Dummy implementation
        }

        @Override
        public int getFitness() {
            return id;
        }

        @Override
        public void setFitness(int fitness) {
            // Dummy implementation
        }

        @Override
        public String toString() {
            return "DummyChromosome{" +
                    "id=" + id +
                    '}';
        }
    }

}
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidInput() {
        int populationLimit = 5;
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new FakeChromosome(1);
        Chromosome chromosome2 = new FakeChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, populationLimit);

        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
        assertFalse(population.getChromosomes().remove(null));
        assertThrows(UnsupportedOperationException.class, () -> population.getChromosomes().remove(chromosome1));

        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1.getFitness(), fittestChromosome.getFitness());
        assertEquals(chromosome1, fittestChromosome);
    }

    @Test
    public void testListPopulationWithInvalidInput_NullChromosomesList() {
        int populationLimit = 5;
        List<Chromosome> chromosomes = null;

        assertThrows(NullArgumentException.class, () -> new ListPopulation(chromosomes, populationLimit));
    }

    @Test
    public void testListPopulationWithInvalidInput_NegativePopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new FakeChromosome(1);
        chromosomes.add(chromosome);

        assertThrows(NotPositiveException.class, () -> new ListPopulation(chromosomes, -1));
    }

    @Test
    public void testListPopulationWithInvalidInput_TooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new FakeChromosome(1);
        Chromosome chromosome2 = new FakeChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        Chromosome chromosome3 = new FakeChromosome(3);

        int populationLimit = 2;

        assertThrows(NumberIsTooLargeException.class, () -> new ListPopulation(chromosomes, populationLimit));
    }

    private static class FakeChromosome implements Chromosome {
        private int id;
        private double fitness;

        public FakeChromosome(int id) {
            this.id = id;
            this.fitness = id;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public double getFitness() {
            return fitness;
        }

        @Override
        public void evaluateFitness() {
            // no-op
        }

        @Override
        public int compareTo(Chromosome o) {
            return Double.compare(this.getFitness(), o.getFitness());
        }
    }
}
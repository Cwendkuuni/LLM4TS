import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidInput() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        List<Chromosome> actualChromosomes = population.getChromosomes();
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(chromosome1);
        expectedChromosomes.add(chromosome2);
        expectedChromosomes.add(chromosome3);

        assertEquals(expectedChromosomes, actualChromosomes);
        assertEquals(3, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithNullChromosomesList() {
        new ListPopulation(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        new ListPopulation(new ArrayList<>(), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithPopulationSizeBiggerThanLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);
        Chromosome chromosome4 = new Chromosome(4);

        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);

        try {
            new ListPopulation(chromosomes, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }
}
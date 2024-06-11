import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ListPopulationTest1 {

    @Test
    public void testListPopulationConstruction() {
        ListPopulation population = new ListPopulation(5); // creates an empty population with a limit of 5
        assertNotNull(population);
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testListPopulationWithInitialChromosomes() {
        Chromosome chromosome1 = new Chromosome(new double[10]);
        Chromosome chromosome2 = new Chromosome(new double[10]);
        ListPopulation population = new ListPopulation(Collections.singletonList(chromosome1), 10);
        population.addChromosome(chromosome2);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithInitialChromosomesExceedingLimit() {
        Chromosome[] initialChromosomes = new Chromosome[6];
        new ListPopulation(Arrays.asList(initialChromosomes), 5);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        Chromosome chromosome1 = new Chromosome(new double[10]);
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(Arrays.asList(chromosome1));
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithChromosomesExceedingLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(Arrays.asList(new Chromosome[6]));
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome1 = new Chromosome(new double[10]);
        population.addChromosomes(Collections.singletonList(chromosome1));
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithChromosomesExceedingLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(Arrays.asList(new Chromosome[6]));
    }
}
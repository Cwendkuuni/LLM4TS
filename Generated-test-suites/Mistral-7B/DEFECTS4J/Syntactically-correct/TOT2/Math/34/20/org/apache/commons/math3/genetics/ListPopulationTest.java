import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructionWithPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testListPopulationConstructionWithEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructionWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome(new int[4]);
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeBiggerThanLimit() {
        ListPopulation population = new ListPopulation(5);
        Collection<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(new int[4]));
        chromosomes.add(new Chromosome(new int[4]));
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        Chromosome bestChromosome = new Chromosome(new int[4]);
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(bestChromosome);
        population.addChromosome(new Chromosome(new int[4]));
        Chromosome actual = population.getFittestChromosome();
        assertEquals(bestChromosome, actual);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        assertEquals(5, population.getPopulationSize());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitSmallerThanCurrentSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        population.setPopulationLimit(3);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        population.addChromosome(new Chromosome(new int[4]));
        String actual = population.toString();
        String expected = "[Chromosome[int], Chromosome[int], Chromosome[int], Chromosome[int], Chromosome[int]]";
        assertTrue(actual.startsWith(expected));
    }
}
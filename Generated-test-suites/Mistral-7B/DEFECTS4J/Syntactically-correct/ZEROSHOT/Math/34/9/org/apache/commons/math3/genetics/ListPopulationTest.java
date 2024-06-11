import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationCreationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationCreationWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationCreationWithEmptyChromosomeList() {
        List<Chromosome> emptyChromosomeList = Collections.emptyList();
        ListPopulation population = new ListPopulation(emptyChromosomeList, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationCreationWithNullChromosomeList() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCreationWithChromosomeListBiggerThanPopulationLimit() {
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(new Chromosome());
        chromosomeList.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomeList, 1);
    }

    @Test
    public void testListPopulationSetChromosomesWithValidChromosomeList() {
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(new Chromosome());
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(chromosomeList);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullChromosomeList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithChromosomeListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(new Chromosome());
        chromosomeList.add(new Chromosome());
        population.setChromosomes(chromosomeList);
    }

    @Test
    public void testListPopulationAddChromosomesWithValidChromosomeCollection() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddChromosomesWithNullChromosomeCollection() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithChromosomeCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = population.getChromosomes();
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, result);
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = population.getChromosomeList();
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, result);
    }

    @Test
    public void testListPopulationAddChromosomeWithValidChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitReached() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome fittestChromosome = new Chromosome();
        chromosomes.add(new Chromosome());
        chromosomes.add(fittestChromosome);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Chromosome result = population.getFittestChromosome();
        assertEquals(fittestChromosome, result);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimitWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitWithCurrentSizeBiggerThanNewLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        population.setPopulationLimit(1);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals("[Chromosome@", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosomes.get(0), iterator.next());
        assertFalse(iterator.hasNext());
    }
}
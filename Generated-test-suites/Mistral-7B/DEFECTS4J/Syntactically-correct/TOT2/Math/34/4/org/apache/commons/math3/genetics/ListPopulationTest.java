import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ListPopulationTest1 {

    @Test
    public void testListPopulationConstructorWithEmptyList() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationConstructorWithList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        assertNotNull(listPopulation);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(chromosomes.get(0), listPopulation.getChromosomes().get(0));
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithZeroPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(0);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(chromosomes.get(0), listPopulation.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullList() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithListBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 1);
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        Chromosome chromosome = new DummyChromosome();
        listPopulation.addChromosomes(Collections.singletonList(chromosome));
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(chromosome, listPopulation.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullCollection() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithCollectionBiggerThanPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.addChromosomes(Collections.singletonList(new DummyChromosome()));
        List<Chromosome> chromosomes = listPopulation.getChromosomes();
        assertEquals(1, chromosomes.size());
        assertEquals(listPopulation.getChromosomes().get(0), chromosomes.get(0));
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.addChromosomes(Collections.singletonList(new DummyChromosome()));
        List<Chromosome> chromosomes = listPopulation.getChromosomeList();
        assertEquals(1, chromosomes.size());
        assertEquals(listPopulation.getChromosomes().get(0), chromosomes.get(0));
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        Chromosome chromosome = new DummyChromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(chromosome, listPopulation.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeNullChromosome() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.addChromosome(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitReached() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 1);
        Chromosome chromosome = new DummyChromosome();
        listPopulation.addChromosome(chromosome);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        Chromosome fittestChromosome = new DummyChromosome();
        listPopulation.addChromosomes(Collections.singletonList(fittestChromosome));
        assertEquals(fittestChromosome, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.setPopulationLimit(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZeroPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.setPopulationLimit(0);
    }

    @Test
    public void testSetPopulationLimitWithBiggerPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.setPopulationLimit(11);
        assertEquals(10, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetPopulationLimitWithSmallerPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.setPopulationLimit(9);
        assertEquals(10, listPopulation.getPopulationSize());
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.addChromosomes(Collections.singletonList(new DummyChromosome()));
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.addChromosomes(Collections.singletonList(new DummyChromosome()));
        assertEquals("[Lorg.apache.commons.math3.genetics.DummyChromosome;@" + hashCode() + "]", listPopulation.toString());
    }

    @Test
    public void testIterator() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        listPopulation.addChromosomes(Collections.singletonList(new DummyChromosome()));
        Iterator<Chromosome> iterator = listPopulation.iterator();
        iterator.next();
        assertFalse(iterator.hasNext());
    }
}
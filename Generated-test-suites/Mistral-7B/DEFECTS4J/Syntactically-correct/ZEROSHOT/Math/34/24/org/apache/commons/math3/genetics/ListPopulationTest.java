import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ListPopulationTest {

    @Test
    public void testListPopulationInitializationWithEmptyList() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationInitializationWithNonEmptyList() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(chromosome1);

        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationInitializationWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationInitializationWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationInitializationWithTooManyChromosomes() {
        Chromosome[] chromosomes = new Chromosome[6];
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new DummyChromosome("chromosome" + i);
        }

        ListPopulation population = new ListPopulation(Arrays.asList(chromosomes), 5);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);

        population.setChromosomes(Arrays.asList(chromosome1));

        assertEquals(1, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testListPopulationSetChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(Arrays.asList(new Chromosome[6]));
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        population.addChromosomes(Arrays.asList(chromosome1));

        assertEquals(1, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testListPopulationAddChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(Arrays.asList(new Chromosome[6]));
    }

    @Test
    public void testListPopulationGetChromosomes() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1), 5);

        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(1, chromosomes.size());
        assertSame(chromosome1, chromosomes.get(0));
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1), 5);

        List<Chromosome> chromosomeList = population.getChromosomeList();
        assertEquals(1, chromosomeList.size());
        assertSame(chromosome1, chromosomeList.get(0));
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome1 = new DummyChromosome("chromosome1");

        population.addChromosome(chromosome1);

        assertEquals(1, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().iterator().next());
    }

    @Test(expected = IllegalStateException.class)
    public void testListPopulationAddChromosomeWithFullPopulation() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome[5]), 5);
        Chromosome chromosome1 = new DummyChromosome("chromosome1");

        population.addChromosome(chromosome1);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1), 1);

        Chromosome fittestChromosome = population.getFittestChromosome();
        assertSame(chromosome1, fittestChromosome);
    }

    @Test
    public void testListPopulationGetFittestChromosomeWithMultipleFittestChromosomes() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        Chromosome chromosome2 = new DummyChromosome("chromosome2");
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 2);

        Chromosome fittestChromosome = population.getFittestChromosome();
        assertSame(chromosome1, fittestChromosome);
        assertSame(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);

        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);

        population.setPopulationLimit(10);

        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationSetPopulationLimitWithNegativeValue() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);

        population.setPopulationLimit(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void testListPopulationSetPopulationLimitWithSmallerValue() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome[5]), 5);

        population.setPopulationLimit(4);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);

        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        Chromosome chromosome2 = new DummyChromosome("chromosome2");
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        assertEquals("[chromosome1, chromosome2]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        Chromosome chromosome1 = new DummyChromosome("chromosome1");
        Chromosome chromosome2 = new DummyChromosome("chromosome2");
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertSame(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}

class DummyChromosome implements Chromosome {

    private final String name;

    public DummyChromosome(String name) {
        this.name = name;
    }

    @Override
    public double getFitness() {
        return 1.0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Chromosome o) {
        if (o instanceof DummyChromosome) {
            return this.name.compareTo(((DummyChromosome) o).name);
        }
        throw new UnsupportedOperationException();
    }
}
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationCreationWithPositiveLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationCreationWithNegativeLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationCreationWithChromosomesAndPositiveLimit() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }}, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosome(0));
        assertEquals(chromosome2, population.getChromosome(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationCreationWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCreationWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>() {{
            add(new DummyChromosome(1));
            add(new DummyChromosome(2));
            add(new DummyChromosome(3));
        }};
        new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosome(0));
        assertEquals(chromosome2, population.getChromosome(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>() {{
            add(new DummyChromosome(1));
            add(new DummyChromosome(2));
            add(new DummyChromosome(3));
        }};
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosome(0));
        assertEquals(chromosome2, population.getChromosome(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>() {{
            add(new DummyChromosome(1));
            add(new DummyChromosome(2));
            add(new DummyChromosome(3));
        }};
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.setChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        List<Chromosome> result = population.getChromosomes();
        assertEquals(2, result.size());
        assertEquals(chromosome1, result.get(0));
        assertEquals(chromosome2, result.get(1));
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.setChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        List<Chromosome> result = population.getChromosomeList();
        assertEquals(2, result.size());
        assertEquals(chromosome1, result.get(0));
        assertEquals(chromosome2, result.get(1));
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        population.addChromosome(chromosome1);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosome(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        population.addChromosome(chromosome1);
        population.addChromosome(new DummyChromosome(2));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(10);
        Chromosome chromosome2 = new DummyChromosome(20);
        population.setChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitWithNegativeLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitWithTooSmallLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
    }
}

class DummyChromosome implements Chromosome {
    private int value;

    public DummyChromosome(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Chromosome o) {
        DummyChromosome other = (DummyChromosome) o;
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return "DummyChromosome{" + "value=" + value + '}';
    }
}
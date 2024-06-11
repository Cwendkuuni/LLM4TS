import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructionWithListAndLimit() {
        List<Chromosome> initialChromosomes = new java.util.ArrayList<>();
        Chromosome chromosome1 = new FooChromosome(1.0);
        Chromosome chromosome2 = new FooChromosome(2.0);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructionWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(5, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, 1.1);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> initialChromosomes = new java.util.ArrayList<>();
        Chromosome chromosome1 = new FooChromosome(1.0);
        Chromosome chromosome2 = new FooChromosome(2.0);
        Chromosome chromosome3 = new FooChromosome(3.0);
        Chromosome chromosome4 = new FooChromosome(4.0);
        Chromosome chromosome5 = new FooChromosome(5.0);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);
        initialChromosomes.add(chromosome3);
        initialChromosomes.add(chromosome4);
        initialChromosomes.add(chromosome5);

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.5);
        Collections.sort(population.getChromosomes());
        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        assertEquals(chromosome3, nextGeneration.getChromosome(0));
        assertEquals(chromosome4, nextGeneration.getChromosome(1));
        assertEquals(chromosome5, nextGeneration.getChromosome(2));
        for (int i = 3; i < nextGeneration.size(); i++) {
            Chromosome randomChromosome = population.getChromosome(i - 2);
            assertEquals(randomChromosome, nextGeneration.getChromosome(i));
        }
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate());
    }

    @Test
    public void testElitisticListPopulationGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, 0.5);
        assertEquals(0.5, population.getElitismRate());
    }
}

class FooChromosome implements Chromosome {
    private double value;

    public FooChromosome(double value) {
        this.value = value;
    }

    @Override
    public double getFitness() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }
}
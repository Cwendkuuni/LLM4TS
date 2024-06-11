import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        Chromosome[] chromosomes = new Chromosome[5];
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new DummyChromosome("chromosome " + i);
        }

        ElitisticListPopulation population = new ElitisticListPopulation(List.of(chromosomes), 10, 0.9);
        assertEquals(10, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitismRateOverOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 1.1);
    }

    @Test
    public void testNextGeneration() {
        Chromosome[] chromosomes = new Chromosome[5];
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new DummyChromosome("chromosome " + i);
        }

        ElitisticListPopulation population = new ElitisticListPopulation(List.of(chromosomes), 10, 0.9);
        Collections.sort(population.getChromosomes());
        Population nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.size());
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = population.getChromosome(i);
            Chromosome nextGenChromosome = nextGeneration.getChromosome(i);
            if (i < 5 - (int) Math.ceil(0.9 * 5.0)) {
                assertNotSame(chromosome, nextGenChromosome);
            } else {
                assertEquals(chromosome, nextGenChromosome);
            }
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOverOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(1.1);
    }

}

class DummyChromosome implements Chromosome {

    String name;

    public DummyChromosome(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
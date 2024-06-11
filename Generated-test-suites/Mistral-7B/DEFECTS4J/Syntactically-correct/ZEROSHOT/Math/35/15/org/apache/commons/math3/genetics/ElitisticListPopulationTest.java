import org.apache.commons.math3.GeneticAlgorithm;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreation() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = -0.1;
        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationGreaterThanOneElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 1.1;
        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() throws OutOfRangeException {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 5;
        double elitismRate = 0.3;
        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        Chromosome chromosome1 = new FitnessProportionateSelectionTest.DummyChromosome(1);
        Chromosome chromosome2 = new FitnessProportionateSelectionTest.DummyChromosome(2);
        Chromosome chromosome3 = new FitnessProportionateSelectionTest.DummyChromosome(3);
        Chromosome chromosome4 = new FitnessProportionateSelectionTest.DummyChromosome(4);
        Chromosome chromosome5 = new FitnessProportionateSelectionTest.DummyChromosome(5);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);
        chromosomes.add(chromosome5);

        Collections.sort(chromosomes);
        Population nextGeneration = parentPopulation.nextGeneration();

        assertEquals(populationLimit, nextGeneration.size());
        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        ElitisticListPopulation childPopulation = (ElitisticListPopulation) nextGeneration;

        assertEquals(elitismRate, childPopulation.getElitismRate(), 0.001);
        for (int i = 0; i < chromosomes.size() - (int) Math.ceil(parentPopulation.size() * elitismRate); i++) {
            Chromosome child = childPopulation.getChromosome(i);
            Chromosome parent = parentPopulation.getChromosome(i);
            assertNotSame(child, parent);
        }
        for (int i = chromosomes.size() - (int) Math.ceil(parentPopulation.size() * elitismRate); i < chromosomes.size(); i++) {
            Chromosome child = childPopulation.getChromosome(i);
            Chromosome parent = parentPopulation.getChromosome(i);
            assertSame(child, parent);
        }
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() throws OutOfRangeException {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, 0.3);
        double newElitismRate = 0.7;
        population.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateNegativeRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, 0.3);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateGreaterThanOneRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, 0.3);
        population.setElitismRate(1.1);
    }
}
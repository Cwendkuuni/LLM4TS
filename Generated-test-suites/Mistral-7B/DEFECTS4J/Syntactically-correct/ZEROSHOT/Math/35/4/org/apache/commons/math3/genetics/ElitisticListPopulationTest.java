import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    private static final int POPULATION_LIMIT = 10;
    private static final double ELITISM_RATE = 0.5;
    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < POPULATION_LIMIT; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return i;
                }
            });
        }
        population = new ElitisticListPopulation(chromosomes, POPULATION_LIMIT, ELITISM_RATE);
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(population, POPULATION_LIMIT, ELITISM_RATE);
        Assert.assertEquals(POPULATION_LIMIT, population.size());
        Assert.assertEquals(ELITISM_RATE, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(POPULATION_LIMIT, ELITISM_RATE);
        Assert.assertEquals(POPULATION_LIMIT, population.size());
        Assert.assertEquals(ELITISM_RATE, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithNegativeElitismRate() {
        new ElitisticListPopulation(population, POPULATION_LIMIT, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(population, POPULATION_LIMIT, 1.1);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(POPULATION_LIMIT, nextGeneration.size());
        ElitisticListPopulation nextElitisticPopulation = (ElitisticListPopulation) nextGeneration;
        Assert.assertEquals(ELITISM_RATE, nextElitisticPopulation.getElitismRate(), 0.001);
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        List<Chromosome> newChromosomes = nextElitisticPopulation.getChromosomes();
        for (int i = 0; i < (int) FastMath.ceil(oldChromosomes.size() * (1.0 - ELITISM_RATE)); i++) {
            Assert.assertNotSame(oldChromosomes.get(i), newChromosomes.get(i));
        }
        for (int i = (int) FastMath.ceil(oldChromosomes.size() * ELITISM_RATE); i < oldChromosomes.size(); i++) {
            Assert.assertSame(oldChromosomes.get(i), newChromosomes.get(i));
        }
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        population.setElitismRate(1.1);
    }
}
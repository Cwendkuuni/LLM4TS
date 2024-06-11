package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.ElitisticListPopulationBuilder;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomeList;

    /**
     * Initialize the test population with a list of mocked chromosomes.
     */
    @Before
    public void setUp() {
        Chromosome chromosome1 = new ChromosomeMock(1);
        Chromosome chromosome2 = new ChromosomeMock(2);
        Chromosome chromosome3 = new ChromosomeMock(3);
        Chromosome chromosome4 = new ChromosomeMock(4);
        Chromosome chromosome5 = new ChromosomeMock(5);
        Chromosome chromosome6 = new ChromosomeMock(6);

        chromosomeList = new ArrayList<Chromosome>();
        chromosomeList.add(chromosome1);
        chromosomeList.add(chromosome2);
        chromosomeList.add(chromosome3);
        chromosomeList.add(chromosome4);
        chromosomeList.add(chromosome5);
        chromosomeList.add(chromosome6);

        population = ElitisticListPopulationBuilder.aElitisticListPopulation()
                .withChromosomes(chromosomeList)
                .withPopulationLimit(6)
                .withElitismRate(0.5)
                .build();
    }

    /**
     * Test the constructor with valid parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        assertNotNull(population);
        assertEquals(6, population.size());
    }

    /**
     * Test the constructor with an invalid elitism rate.
     */
    @Test
    public void testConstructorWithInvalidElitismRate() {
        assertThrows(OutOfRangeException.class, () -> new ElitisticListPopulation(chromosomeList, 6, 1.5));
    }

    /**
     * Test the population size after the creation of the next generation.
     */
    @Test
    public void testNextGenerationSize() {
        Population nextGeneration = population.nextGeneration();
        assertEquals(6, nextGeneration.size());
    }

    /**
     * Test that the best chromosomes are copied to the next generation.
     */
    @Test
    public void testNextGenerationWithElitism() {
        Population nextGeneration = population.nextGeneration();
        Collections.sort(nextGeneration.getChromosomes());
        Chromosome bestChromosome = ((ElitisticListPopulation) population).getChromosomes().get(0);
        Chromosome copiedChromosome = nextGeneration.getChromosomes().get(0);
        assertEquals(bestChromosome.hashCode(), copiedChromosome.hashCode());
    }

    /**
     * Test that the worst chromosomes are not copied to the next generation.
     */
    @Test
    public void testNextGenerationWithoutElitism() {
        ElitisticListPopulation populationWithNoElitism = new ElitisticListPopulation(6, 0.0);
        Population nextGeneration = populationWithNoElitism.nextGeneration();
        Collections.sort(nextGeneration.getChromosomes());
        Chromosome worstChromosome = ((ElitisticListPopulation) population).getChromosomes().get(5);
        assertNotSame(worstChromosome, nextGeneration.getChromosomes().get(0));
    }

    /**
     * Test that setting the elitism rate updates the population.
     */
    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate());
    }

    /**
     * Test that setting an invalid elitism rate throws an exception.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalid() {
        population.setElitismRate(1.1);
    }
}

/**
 * Mock chromosome class for testing purposes.
 */
class ChromosomeMock implements Chromosome {

    private final int id;

    public ChromosomeMock(final int id) {
        this.id = id;
    }

    @Override
    public double[] getGenotype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] getPhenotype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getFitness() {
        return id;
    }

    @Override
    public String toString() {
        return "ChromosomeMock_" + id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ChromosomeMock) {
            ChromosomeMock other = (ChromosomeMock) obj;
            return this.id == other.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
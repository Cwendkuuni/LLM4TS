package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.OutOfRangeElitismRateException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testConstructorInvalidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 1.5;
        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    /**
     * Test nextGeneration method with valid arguments.
     */
    @Test
    public void testNextGenerationValidArguments() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5), new DummyChromosome(6), new DummyChromosome(7), new DummyChromosome(8), new DummyChromosome(9), new DummyChromosome(10));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
        assertFalse(nextGeneration.contains(chromosomes.get(0))); // worst chromosome should be removed
        assertFalse(nextGeneration.contains(chromosomes.get(9))); // best chromosome should be copied
        assertTrue(nextGeneration.containsAll(Arrays.asList(chromosomes.get(1), chromosomes.get(2), chromosomes.get(3), chromosomes.get(4), chromosomes.get(5), chromosomes.get(6), chromosomes.get(7), chromosomes.get(8)))); // other chromosomes should be copied based on their fitness
    }

    /**
     * Test setElitismRate method with valid arguments.
     */
    @Test
    public void testSetElitismRateValidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid arguments.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        population.setElitismRate(-0.1);
    }
}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double getFitness() {
        return Double.parseDouble(String.valueOf(id));
    }

    @Override
    public String toString() {
        return "DummyChromosome " + id;
    }
}
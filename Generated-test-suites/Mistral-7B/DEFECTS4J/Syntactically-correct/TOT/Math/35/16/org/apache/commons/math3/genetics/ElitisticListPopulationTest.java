package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        List<Chromosome> chromosomes = new LinkedList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(2.0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.7);
        Assert.assertEquals(5, population.size());
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithInvalidElitismRate() {
        List<Chromosome> chromosomes = new LinkedList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 2.0);
    }

    /**
     * Test nextGeneration method with valid population and elitism rate.
     */
    @Test
    public void testNextGenerationWithValidParameters() {
        List<Chromosome> chromosomes = new LinkedList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(1.5));
        chromosomes.add(new Chromosome(2.0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.7);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(5, nextGeneration.size());
        List<Chromosome> nextChromosomes = nextGeneration.getChromosomes();
        Assert.assertTrue(nextChromosomes.get(0) instanceof Chromosome);
        Assert.assertTrue(nextChromosomes.get(1) instanceof Chromosome);
        Assert.assertTrue(nextChromosomes.get(2) instanceof Chromosome);
        Assert.assertFalse(nextChromosomes.get(3) instanceof Chromosome);
        Assert.assertFalse(nextChromosomes.get(4) instanceof Chromosome);
        Collections.sort(nextChromosomes);
        Chromosome bestChromosome = nextChromosomes.get(nextChromosomes.size() - 1);
        Assert.assertEquals(1.5, bestChromosome.getValue(), 0.001);
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @Test
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new LinkedList<>(), 5, 0.7);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(0, nextGeneration.size());
    }

    /**
     * Test nextGeneration method with population limit smaller than the number of chromosomes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextGenerationWithPopulationLimitSmallerThanNumberOfChromosomes() {
        List<Chromosome> chromosomes = new LinkedList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(1.5));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 1, 0.7);
        population.nextGeneration();
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateWithValidParameters() {
        ElitisticListPopulation population = new ElitisticListPopulation(new LinkedList<>(), 5, 0.7);
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidParameters() {
        ElitisticListPopulation population = new ElitisticListPopulation(new LinkedList<>(), 5, 0.7);
        population.setElitismRate(2.0);
    }
}
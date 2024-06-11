package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Tests the constructor with valid parameters.
     */
    @Test
    public void testElitisticListPopulationValidConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertEquals(0, population.size());
    }

    /**
     * Tests the constructor with invalid elitismRate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationInvalidConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = -0.1;

        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    /**
     * Tests the nextGeneration method with empty population.
     */
    @Test
    public void testElitisticListPopulationEmptyNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);

        Population nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0.001);
        assertTrue(nextGeneration.isEmpty());
    }

    /**
     * Tests the nextGeneration method with valid population.
     */
    @Test
    public void testElitisticListPopulationValidNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        population.addAll(chromosomes);

        Collections.sort(population);

        Population nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0.001);
        assertEquals(2, nextGeneration.size());
        assertTrue(nextGeneration.get(0) instanceof DummyChromosome);
        assertTrue(nextGeneration.get(1) instanceof DummyChromosome);
        assertNotSame(population.get(0), nextGeneration.get(0));
        assertNotSame(population.get(1), nextGeneration.get(1));
    }

    /**
     * Tests the setElitismRate method with valid elitism rate.
     */
    @Test
    public void testElitisticListPopulationValidSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);

        population.setElitismRate(0.7);

        assertEquals(0.7, population.getElitismRate(), 0.001);
    }

    /**
     * Tests the setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationInvalidSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);

        population.setElitismRate(-0.1);
    }
}

class DummyChromosome implements Chromosome {
    private int fitness;

    public DummyChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "DummyChromosome{" +
                "fitness=" + fitness +
                '}';
    }
}
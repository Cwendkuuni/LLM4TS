package org.apache.commons.math3.genetics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @BeforeEach
    public void init() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        chromosomes.add(new DummyChromosome(4));
        chromosomes.add(new DummyChromosome(5));
        population = new ElitisticListPopulation(chromosomes, 5, 0.5);
    }

    @Test
    public void testElitisticListPopulationCreationWithListAndLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
        assertFalse(population.isFull());
    }

    @Test
    public void testElitisticListPopulationCreationWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
        assertFalse(population.isFull());
    }

    @Test
    public void testSettingElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    @Test
    public void testOutOfRangeElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        ElitisticListPopulation population = null;
        try {
            population = new ElitisticListPopulation(chromosomes, 5, -0.1);
        } catch (OutOfRangeException e) {
            assertEquals(OutOfRangeException.class, e.getClass());
        }
        assertNull(population);

        try {
            population = new ElitisticListPopulation(5, 1.2);
        } catch (OutOfRangeException e) {
            assertEquals(OutOfRangeException.class, e.getClass());
        }
        assertNull(population);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.01);
        assertEquals(2, nextGeneration.size());
        assertTrue(nextGeneration.containsAll(population.subList(2, population.size())));
    }

}

class DummyChromosome implements Chromosome {

    private int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public int getFitness() {
        return id;
    }

}
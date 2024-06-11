package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        Chromosome chromosome1 = Mockito.mock(Chromosome.class);
        Chromosome chromosome2 = Mockito.mock(Chromosome.class);
        Chromosome chromosome3 = Mockito.mock(Chromosome.class);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithListAndLimit() {
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
        assertEquals(3, population.size());
    }

    @Test
    public void testConstructorWithLimit() {
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
        assertEmpty(population);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithNegativeElitismRate() {
        new ElitisticListPopulation(Collections.emptyList(), 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(Collections.emptyList(), 10, 1.1);
    }

    @Test
    public void testNextGenerationSize() {
        assertEquals(5, population.nextGeneration().size());
    }

    @Test
    public void testNextGenerationContainsElites() {
        Chromosome bestChromosome = Mockito.mock(Chromosome.class);
        Mockito.when(chromosome1.getFitness()).thenReturn(1.0);
        Mockito.when(chromosome2.getFitness()).thenReturn(2.0);
        Mockito.when(chromosome3.getFitness()).thenReturn(3.0);
        Mockito.when(bestChromosome.getFitness()).thenReturn(0.5);
        chromosomes.set(0, bestChromosome);

        List<Chromosome> nextGeneration = population.nextGeneration().getChromosomes();
        assertTrue(nextGeneration.contains(bestChromosome));
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-0.1);
    }
}
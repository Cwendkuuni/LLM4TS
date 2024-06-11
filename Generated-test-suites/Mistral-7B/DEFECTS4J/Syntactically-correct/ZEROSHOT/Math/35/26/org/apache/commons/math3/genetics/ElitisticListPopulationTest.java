package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
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
    public void setup() {
        chromosomes = new ArrayList<>();
        Chromosome chromosome1 = Mockito.mock(Chromosome.class);
        Chromosome chromosome2 = Mockito.mock(Chromosome.class);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testElitisticListPopulationConstructorWithList() {
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testElitisticListPopulationConstructorWithPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithNegativeElitismRate() {
        new ElitisticListPopulation(Collections.emptyList(), 1, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(Collections.emptyList(), 1, 1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        ElitisticListPopulation nextGeneration = population.nextGeneration();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        int boundIndex = (int) FastMath.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());

        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0.01);

        for (int i = 0; i < boundIndex; i++) {
            assertTrue(oldChromosomes.get(i).equals(newChromosomes.get(i)));
        }

        assertFalse(oldChromosomes.get(0).equals(newChromosomes.get(0)));
        assertFalse(oldChromosomes.get(1).equals(newChromosomes.get(1)));
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
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
package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for ElitisticListPopulation.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.apache.commons.math3.exception.OutOfRangeException"})
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Test
    public void testElitisticListPopulation_ValidConstruction() {
        // Given
        List<Chromosome> chromosomes = Mockito.mock(List.class);
        int populationLimit = 10;
        double elitismRate = 0.9;

        // When
        population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Then
        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulation_InvalidElitismRate() {
        // Given
        List<Chromosome> chromosomes = Collections.emptyList();
        int populationLimit = 10;
        double elitismRate = 1.1;

        // When
        // Then an exception should be thrown
        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testElitisticListPopulation_NextGeneration_EmptyPopulation() {
        // Given
        List<Chromosome> chromosomes = Mockito.mock(List.class);
        int populationLimit = 10;
        double elitismRate = 0.9;
        PowerMockito.when(chromosomes.size()).thenReturn(0);

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        population.setChromosomes(chromosomes);

        // When
        Population nextGeneration = population.nextGeneration();

        // Then
        assertEquals(populationLimit, nextGeneration.size());
        PowerMockito.verifyNoMoreInteractions(chromosomes);
    }

    @Test
    public void testElitisticListPopulation_NextGeneration_ValidPopulation() {
        // Given
        List<Chromosome> chromosomes = Mockito.mock(List.class);
        int populationLimit = 10;
        double elitismRate = 0.9;
        PowerMockito.when(chromosomes.size()).thenReturn(5);

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        population.setChromosomes(chromosomes);

        // When
        Population nextGeneration = population.nextGeneration();

        // Then
        assertEquals(populationLimit, nextGeneration.size());

        PowerMockito.verify(chromosomes, ArgumentMatchers.atLeast(5)).get(ArgumentMatchers.anyInt());

        for (int i = 0; i < 5; i++) {
            Mockito.verify(chromosomes).get(i);
        }

        PowerMockito.verifyNoMoreInteractions(chromosomes);
    }

    @Test
    public void testElitisticListPopulation_SetElitismRate() {
        // Given
        List<Chromosome> chromosomes = Mockito.mock(List.class);
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit);

        // When
        population.setElitismRate(0.8);

        // Then
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }
}
package org.apache.commons.math3.genetics;

import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertNotNull;
import org.junit.Assert.assertThrows;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPopulationLimit() {
        // Given
        int populationLimit = 5;

        // When
        ListPopulation population = new ListPopulation(populationLimit);

        // Then
        assertEquals("Size of population should be equal to populationLimit.", populationLimit, population.getPopulationSize());
    }

    @Test
    public void testListPopulationConstructorWithEmptyListAndPopulationLimit() {
        // Given
        int populationLimit = 5;
        List<Chromosome> chromosomes = new ArrayList<>();

        // When
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);

        // Then
        assertEquals("Size of population should be equal to populationLimit.", populationLimit, population.getPopulationSize());
        assertEquals("List of chromosomes should be equal to the given list.", chromosomes, population.getChromosomes());
    }

    @Test
    public void testListPopulationConstructorWithNullListAndPopulationLimit() {
        // Given
        int populationLimit = 5;
        List<Chromosome> chromosomes = null;

        // Then
        assertThrows("Constructor should throw a NullArgumentException.", NullArgumentException.class,
                () -> new ListPopulation(chromosomes, populationLimit));
    }

    @Test
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        // Given
        int populationLimit = -1;

        // Then
        assertThrows("Constructor should throw a NotPositiveException.", NotPositiveException.class,
                () -> new ListPopulation(populationLimit));
    }

    @Test
    public void testListPopulationConstructorWithListOfChromosomesBiggerThanPopulationLimit() {
        // Given
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 1;

        // Then
        assertThrows("Constructor should throw a NumberIsTooLargeException.", NumberIsTooLargeException.class,
                () -> new ListPopulation(chromosomes, populationLimit));
    }

    @Test
    public void testListPopulationSetChromosomes() {
        // Given
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());

        // When
        population.setChromosomes(chromosomes);

        // Then
        assertEquals("Size of population should be equal to the size of the given list.", chromosomes.size(), population.getPopulationSize());
        assertEquals("List of chromosomes should be equal to the given list.", chromosomes, population.getChromosomes());
    }

    @Test
    public void testListPopulationSetChromosomesWithNullList() {
        // Given
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = null;

        // Then
        assertThrows("setChromosomes method should throw a NullArgumentException.", NullArgumentException.class,
                () -> population.setChromosomes(chromosomes));
    }

    @Test
    public void testListPopulationSetChromosomesWithListBiggerThanPopulationLimit() {
        // Given
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        // Then
        assertThrows("setChromosomes method should throw a NumberIsTooLargeException.", NumberIsTooLargeException.class,
                () -> population.setChromosomes(chromosomes));
    }

    // ... Add more test cases for other methods here
}
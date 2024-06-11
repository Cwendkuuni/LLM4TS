package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;

/**
 * Tests for the ListPopulation class.
 */
public class ListPopulationTest {

    /**
     * Test the creation of a ListPopulation instance with a positive population limit.
     */
    @Test
    public void testCreationWithPositivePopulationLimit() {
        Population population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test the creation of a ListPopulation instance with an empty list of chromosomes and a positive population limit.
     */
    @Test
    public void testCreationWithEmptyListAndPositivePopulationLimit() {
        Population population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test the creation of a ListPopulation instance with a list of chromosomes and a positive population limit,
     * and the list does not exceed the population limit.
     */
    @Test
    public void testCreationWithListLessThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        chromosomes.add(chromosome1);
        Population population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
    }

    /**
     * Test the creation of a ListPopulation instance with a list of chromosomes and a positive population limit,
     * and the list exceeds the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testCreationWithListBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        chromosomes.add(chromosome1);
        chromosomes.add(new DummyChromosome(2));
        new ListPopulation(chromosomes, 1);
    }

    /**
     * Test setting an empty list of chromosomes to a ListPopulation instance.
     */
    @Test
    public void testSettingEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setChromosomes(new ArrayList<>());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test setting a list of chromosomes to a ListPopulation instance, which does not exceed the population limit.
     */
    @Test
    public void testSettingListLessThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new DummyChromosome(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        population.setChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
    }

    /**
     * Test setting a list of chromosomes to a ListPopulation instance, which exceeds the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new DummyChromosome(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(new DummyChromosome(2));
        population.setChromosomes(chromosomes);
    }

    /**
     * Test adding a single chromosome to a ListPopulation instance.
     */
    @Test
    public void testAddingSingleChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new DummyChromosome(1);
        population.addChromosome(chromosome1);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
    }

    /**
     * Test adding multiple chromosomes to a ListPopulation instance, which does not exceed the population limit.
     */
    @Test
    public void testAddingMultipleChromosomesLessThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    /**
     * Test adding multiple chromosomes to a ListPopulation instance, which exceeds the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddingMultipleChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
            add(new DummyChromosome(3));
        }});
    }

    /**
     * Test getting the fittest chromosome from an empty population.
     */
    @Test
    public void testGettingFittestChromosomeFromEmptyPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertNull(population.getFittestChromosome());
    }

    /**
     * Test getting the fittest chromosome from a population with multiple chromosomes.
     */
    @Test
    public void testGettingFittestChromosomeFromPopulationWithMultipleChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new DummyChromosome(100);
        Chromosome chromosome2 = new DummyChromosome(50);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        assertEquals(chromosome1, population.getFittestChromosome());
    }

    /**
     * Test setting the population limit to a negative number.
     */
    @Test(expected = NotPositiveException.class)
    public void testSettingPopulationLimitToNegativeNumber() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test setting the population limit to a positive number smaller than the current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSettingPopulationLimitToNumberSmallerThanCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(new DummyChromosome(1));
            add(new DummyChromosome(2));
        }});
        population.setPopulationLimit(2);
    }

    /**
     * Test setting the population limit to a positive number equal to the current population size.
     */
    @Test
    public void testSettingPopulationLimitToNumberEqualToCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(new DummyChromosome(1));
            add(new DummyChromosome(2));
        }});
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
    }

    /**
     * Test setting the population limit to a positive number greater than the current population size.
     */
    @Test
    public void testSettingPopulationLimitToNumberGreaterThanCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(new DummyChromosome(1));
            add(new DummyChromosome(2));
        }});
        population.setPopulationLimit(6);
        assertEquals(5, population.getPopulationSize());
    }

    /**
     * Test getting the population size.
     */
    @Test
    public void testGettingPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(new DummyChromosome(1));
            add(new DummyChromosome(2));
        }});
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Test getting the population limit.
     */
    @Test
    public void testGettingPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});
        String expected = "[Chromosome[id=1], Chromosome[id=2]]";
        assertEquals(expected, population.toString());
    }
}

class DummyChromosome implements Chromosome {

    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double getFitness() {
        return id;
    }

    @Override
    public String toString() {
        return "Chromosome[id=" + id + "]";
    }
}
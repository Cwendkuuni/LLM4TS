package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid population limit.
     */
    @Test
    public void testValidConstruction() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstruction() {
        new ListPopulation(-1);
    }

    /**
     * Test setting chromosomes with valid list.
     */
    @Test
    public void testValidSettingChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        chromosomes.add(chromosome);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(1, population.getChromosomes().size());
        Assert.assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test setting chromosomes with null list.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullSettingChromosomes() {
        new ListPopulation(null, 5);
    }

    /**
     * Test setting chromosomes with list exceeding population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testLargeSettingChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        for (int i = 0; i < 10; i++) {
            chromosomes.add(chromosome);
        }
        new ListPopulation(chromosomes, 5);
    }

    /**
     * Test setting chromosomes with valid collection.
     */
    @Test
    public void testValidAddingChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new DummyChromosome(1);
        population.addChromosomes(Collections.singletonList(chromosome));
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test adding chromosomes with null collection.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullAddingChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(null);
    }

    /**
     * Test adding chromosomes with collection exceeding population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testLargeAddingChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        for (int i = 0; i < 10; i++) {
            chromosomes.add(chromosome);
        }
        population.addChromosomes(chromosomes);
    }

    /**
     * Test getting an unmodifiable list of chromosomes.
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new DummyChromosome(1);
        population.addChromosome(chromosome);
        List<Chromosome> actual = population.getChromosomes();
        Assert.assertNotNull(actual);
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(chromosome, actual.get(0));
    }

    /**
     * Test accessing the list of chromosomes.
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new DummyChromosome(1);
        population.addChromosome(chromosome);
        List<Chromosome> actual = population.getChromosomeList();
        Assert.assertNotNull(actual);
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(chromosome, actual.get(0));
    }

    /**
     * Test adding a chromosome with valid chromosome.
     */
    @Test
    public void testValidAddingChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new DummyChromosome(1);
        population.addChromosome(chromosome);
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test adding a chromosome with null chromosome.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullAddingChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosome(null);
    }

    /**
     * Test getting the fittest chromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome bestChromosome = new DummyChromosome(10);
        Chromosome chromosome1 = new DummyChromosome(5);
        Chromosome chromosome2 = new DummyChromosome(8);
        population.addChromosome(chromosome1);
        population.addChromosome(bestChromosome);
        population.addChromosome(chromosome2);
        Chromosome actual = population.getFittestChromosome();
        Assert.assertEquals(bestChromosome, actual);
    }

    /**
     * Test getting the population limit.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test setting the population limit.
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setting population limit with invalid value.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidSettingPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test setting population limit with smaller value than current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallSettingPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.setPopulationLimit(5);
    }
}

class DummyChromosome implements Chromosome {

    private final int fitness;

    public DummyChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Integer.compare(this.fitness, o.getFitnessValue());
    }

    @Override
    public int getFitnessValue() {
        return this.fitness;
    }
}
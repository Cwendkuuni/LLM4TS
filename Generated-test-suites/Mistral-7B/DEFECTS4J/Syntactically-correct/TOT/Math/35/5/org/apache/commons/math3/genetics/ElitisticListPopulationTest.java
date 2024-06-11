package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private Chromosome chromosome1, chromosome2, chromosome3;

    @Before
    public void initialize() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(1));
        this.population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        this.chromosome1 = new DummyChromosome(1);
        this.chromosome2 = new DummyChromosome(2);
        this.chromosome3 = new DummyChromosome(3);
    }

    @Test
    public void testConstructorWithListAndLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.singletonList(this.chromosome1), 10, 0.9);
        Assert.assertEquals(10, population.size());
        Assert.assertSame(this.chromosome1, population.getChromosome(0));
    }

    @Test
    public void testConstructorWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        Assert.assertEquals(10, population.size());
        population.addChromosome(this.chromosome1);
        Assert.assertSame(this.chromosome1, population.getChromosome(0));
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
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(1, nextGeneration.size());
    }

    @Test
    public void testNextGenerationSingleChromosome() {
        population.addChromosome(this.chromosome1);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(1, nextGeneration.size());
        Assert.assertSame(this.chromosome1, nextGeneration.getChromosome(0));
    }

    @Test
    public void testNextGenerationMultipleChromosomes() {
        population.addAll(List.of(this.chromosome1, this.chromosome2, this.chromosome3));
        Collections.sort(population);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(3, nextGeneration.size());
        Assert.assertNotSame(this.chromosome1, nextGeneration.getChromosome(0));
        Assert.assertNotSame(this.chromosome2, nextGeneration.getChromosome(0));
        Assert.assertNotSame(this.chromosome3, nextGeneration.getChromosome(0));
        Assert.assertTrue(nextGeneration.get(0).compareTo(nextGeneration.get(1)) > 0);
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
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

class DummyChromosome implements Chromosome {
    private int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public void evaluateFitness() {
        // empty
    }

    @Override
    public double getFitness() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DummyChromosome) {
            DummyChromosome other = (DummyChromosome) obj;
            return this.id == other.id;
        }
        return false;
    }

    @Override
    public String toString() {
        return "DummyChromosome{" + "id=" + id + '}';
    }
}
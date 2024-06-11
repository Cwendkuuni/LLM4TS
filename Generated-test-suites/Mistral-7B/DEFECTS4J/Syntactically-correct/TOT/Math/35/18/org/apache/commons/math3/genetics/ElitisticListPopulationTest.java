package org.apache.commons.math3.genetics;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreation() {
        // Test creating ElitisticListPopulation with a list and population limit
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.5, population.getElitismRate(), 0.01);

        // Test creating ElitisticListPopulation with only a population limit
        population = new ElitisticListPopulation(15, 0.8);
        Assert.assertEquals(15, population.getPopulationLimit());
        Assert.assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationOutOfRangeElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    @Test
    public void testElitisticListPopulationSorting() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FitnessEvaluatorTest.DummyChromosome(1));
        chromosomes.add(new FitnessEvaluatorTest.DummyChromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        Collections.sort(population.getChromosomes());
        Chromosome bestChromosome = population.getChromosomes().get(0);
        Assert.assertEquals(1, bestChromosome.getFitnessValue(), 0.01);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FitnessEvaluatorTest.DummyChromosome(1));
        chromosomes.add(new FitnessEvaluatorTest.DummyChromosome(2));
        chromosomes.add(new FitnessEvaluatorTest.DummyChromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        Collections.sort(nextGeneration.getChromosomes());
        Chromosome bestChromosome = population.getChromosomes().get(0);
        Chromosome copiedChromosome = nextGeneration.getChromosomes().get(0);
        Assert.assertEquals(bestChromosome, copiedChromosome);
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(0.6);
        Assert.assertEquals(0.6, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateOutOfRange() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(0.4);
    }
}
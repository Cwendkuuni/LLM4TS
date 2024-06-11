package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;

public class ElitisticListPopulationTest {

    @org.junit.Test
    public void testElitisticListPopulationConstructionWithListAndLimit() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(1.0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(5, population.size());
        assertFalse(population.isElitism());
    }

    @org.junit.Test
    public void testElitisticListPopulationConstructionWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(5, population.size());
        assertFalse(population.isElitism());
    }

    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithNegativeElitismRate() {
        new ElitisticListPopulation(10, -0.1);
    }

    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(10, 1.1);
    }

    @org.junit.Test
    public void testElitisticListPopulationNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.size());
    }

    @org.junit.Test
    public void testElitisticListPopulationNextGenerationWithPopulation() {
        List<Chromosome> oldChromosomes = Collections.nCopies(15, new DummyChromosome(1.0));
        ElitisticListPopulation population = new ElitisticListPopulation(oldChromosomes, 20, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
    }

    @org.junit.Test
    public void testElitisticListPopulationNextGenerationWithGoodChromosomes() {
        List<Chromosome> oldChromosomes = Collections.nCopies(15, new DummyChromosome(1.0));
        ElitisticListPopulation population = new ElitisticListPopulation(oldChromosomes, 20, 0.5);
        Population nextGeneration = population.nextGeneration();
        List<Chromosome> oldBestChromosomes = Collections.nCopies(5, new DummyChromosome(1.1));
        Collections.sort(oldChromosomes);
        List<Chromosome> newBestChromosomes = nextGeneration.getChromosomes().subList(0, 5);
        for (int i = 0; i < 5; i++) {
            assertEquals(oldBestChromosomes.get(i), newBestChromosomes.get(i));
        }
    }

    @org.junit.Test
    public void testElitisticListPopulationNextGenerationWithWorstChromosomes() {
        List<Chromosome> oldChromosomes = Collections.nCopies(15, new DummyChromosome(0.1));
        ElitisticListPopulation population = new ElitisticListPopulation(oldChromosomes, 20, 0.5);
        Population nextGeneration = population.nextGeneration();
        List<Chromosome> oldWorstChromosomes = oldChromosomes.subList(10, 15);
        List<Chromosome> newWorstChromosomes = nextGeneration.getChromosomes().subList(5, 10);
        for (int i = 0; i < 5; i++) {
            assertEquals(oldWorstChromosomes.get(i), newWorstChromosomes.get(i));
        }
    }

    @org.junit.Test
    public void testElitisticListPopulationSetElitismRateWithValidValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate());
    }

    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateWithInvalidValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }

    private static class DummyChromosome implements Chromosome {

        private final double value;

        public DummyChromosome(double value) {
            this.value = value;
        }

        @Override
        public double getFitness() {
            return this.value;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public Object getAllele(int index) {
            return Double.valueOf(this.value);
        }

        @Override
        public void setAllele(int index, Object allele) {
            this.value = ((Double) allele).doubleValue();
        }
    }
}
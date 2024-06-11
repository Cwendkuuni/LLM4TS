package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FakeChromosome(1));
        chromosomes.add(new FakeChromosome(2));
        chromosomes.add(new FakeChromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);

        assertNotNull(population);
        assertEquals(5, population.size());
    }

    /**
     * Test constructor with population limit.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        assertNotNull(population);
        assertEquals(3, population.size());
    }

    /**
     * Test nextGeneration method.
     */
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FakeChromosome(1));
        chromosomes.add(new FakeChromosome(2));
        chromosomes.add(new FakeChromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);

        List<Chromosome> nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        assertEquals(5, nextGeneration.size());
        for (int i = 0; i < 3; i++) {
            Chromosome chromosome = population.getChromosome(i);
            Chromosome nextGenerationChromosome = nextGeneration.get(i);
            assertEquals(chromosome, nextGenerationChromosome);
        }
    }

    /**
     * Test setElitismRate method.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-1));
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(2));
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate());
    }

    /**
     * Test getElitismRate method.
     */
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        assertEquals(0.5, population.getElitismRate());
    }

    /**
     * Test FakeChromosome class.
     */
    private static class FakeChromosome implements Chromosome {

        private int id;

        public FakeChromosome(int id) {
            this.id = id;
        }

        @Override
        public double[] getGenotype() {
            return new double[0];
        }

        @Override
        public void setGenotype(double[] genotype) {
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public void setFitness(double fitness) {
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof FakeChromosome)) {
                return false;
            }
            FakeChromosome other = (FakeChromosome) obj;
            if (id != other.id) {
                return false;
            }
            return true;
        }

    }

}
package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationPositiveInit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativeInit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        population.addChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.addAll(Collections.nCopies(10, new Chromosome() {}));
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {};
        population.addChromosome(chromosome);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeTooMany() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {};
        population.addChromosome(chromosome);
        population.addChromosome(chromosome);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome bestChromosome = new Chromosome() {
            @Override
            public double fitness() {
                return Double.POSITIVE_INFINITY;
            }
        };
        chromosomes.add(bestChromosome);
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
        assertEquals(bestChromosome, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test
    public void testListPopulationSetPopulationLimitSmaller() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }
}
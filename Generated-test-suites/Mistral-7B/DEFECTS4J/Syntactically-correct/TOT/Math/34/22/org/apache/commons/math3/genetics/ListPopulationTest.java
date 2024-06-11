package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link ListPopulation}.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid population limit.
     */
    @Test
    public void testListPopulationCreateWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationCreateWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    /**
     * Test adding chromosomes to the population.
     */
    @Test
    public void testAddChromosomesToListPopulation() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(2, chromosomes.size());
        assertEquals(chromosome1, chromosomes.get(0));
        assertEquals(chromosome2, chromosomes.get(1));
    }

    /**
     * Test adding list of chromosomes to the population.
     */
    @Test
    public void testAddListOfChromosomesToListPopulation() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.addChromosomes(chromosomes);
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(2, populationChromosomes.size());
        assertEquals(chromosome1, populationChromosomes.get(0));
        assertEquals(chromosome2, populationChromosomes.get(1));
    }

    /**
     * Test setting list of chromosomes to the population.
     */
    @Test
    public void testSetListOfChromosomesToListPopulation() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(2, populationChromosomes.size());
        assertEquals(chromosome1, populationChromosomes.get(0));
        assertEquals(chromosome2, populationChromosomes.get(1));
    }

    /**
     * Test setting list of chromosomes to the population with too many elements.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetListOfChromosomesToListPopulationWithTooManyElements() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        Chromosome chromosome3 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        population.setChromosomes(chromosomes);
    }

    /**
     * Test getting fittest chromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 10.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 5.0;
            }
        };
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1, fittestChromosome);
    }
}
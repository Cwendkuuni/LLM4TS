package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ListPopulation}.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid population limit.
     */
    @Test
    public void testListPopulationValidPopulationLimit() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
    }

    /**
     * Test constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test setting chromosomes with valid list.
     */
    @Test
    public void testSetChromosomesValidList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation pop = new ListPopulation(new ArrayList<>());
        pop.setChromosomes(chromosomes);
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes.get(0), pop.getFittestChromosome());
    }

    /**
     * Test setting chromosomes with invalid list.
     */
    @Test(expected = NullPointerException.class)
    public void testSetChromosomesNullList() {
        ListPopulation pop = new ListPopulation(new ArrayList<>());
        pop.setChromosomes(null);
    }

    /**
     * Test setting chromosomes with list larger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesListTooLarge() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation pop = new ListPopulation(5);
        pop.setChromosomes(chromosomes);
    }

    /**
     * Test adding chromosomes to population.
     */
    @Test
    public void testAddChromosomes() {
        ListPopulation pop = new ListPopulation(new ArrayList<>());
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.addChromosomes(chromosomes);
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes.get(0), pop.getFittestChromosome());
    }

    /**
     * Test adding chromosomes to population with limit reached.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesLimitReached() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.addChromosomes(chromosomes);
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        pop.addChromosomes(newChromosomes);
    }
}
package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ListPopulation.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
        assertFalse(pop.getChromosomes().isEmpty());
    }

    /**
     * Test constructor with invalid argument (population limit < 0).
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidArgument() {
        new ListPopulation(-1);
    }

    /**
     * Test setChromosomes method with valid argument.
     */
    @Test
    public void testSetChromosomesValidArguments() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.setChromosomes(chromosomes);
        assertEquals(chromosomes, pop.getChromosomes());
    }

    /**
     * Test setChromosomes method with invalid argument (null).
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesInvalidArgument() {
        ListPopulation pop = new ListPopulation(5);
        pop.setChromosomes(null);
    }

    /**
     * Test setChromosomes method with invalid argument (list size > population limit).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesInvalidArgumentsSize() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.setChromosomes(chromosomes);
    }

    /**
     * Test addChromosomes method with valid argument (list size <= population limit).
     */
    @Test
    public void testAddChromosomesValidArguments() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.addChromosomes(chromosomes);
        assertEquals(chromosomes, pop.getChromosomes());
    }

    /**
     * Test addChromosomes method with invalid argument (list size > population limit).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesInvalidArgumentsSize() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.addChromosomes(chromosomes);
    }

    /**
     * Test getFittestChromosome method with empty list.
     */
    @Test
    public void testGetFittestChromosomeEmptyList() {
        ListPopulation pop = new ListPopulation(5);
        assertNull(pop.getFittestChromosome());
    }

    /**
     * Test getFittestChromosome method with list containing one chromosome.
     */
    @Test
    public void testGetFittestChromosomeOneChromosome() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        Chromosome fittest = pop.getFittestChromosome();
        assertEquals(fittest, pop.getChromosome(0));
    }

    /**
     * Test getFittestChromosome method with list containing multiple chromosomes.
     */
    @Test
    public void testGetFittestChromosomeMultipleChromosomes() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        pop.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        Chromosome fittest = pop.getFittestChromosome();
        assertEquals(fittest, pop.getChromosome(1));
    }

    /**
     * Test getPopulationLimit method.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with valid argument.
     */
    @Test
    public void testSetPopulationLimitValidArguments() {
        ListPopulation pop = new ListPopulation(5);
        pop.setPopulationLimit(10);
        assertEquals(10, pop.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with invalid argument (population limit < current size).
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitInvalidArgumentsCurrentSize() {
        ListPopulation pop = new ListPopulation(5);
        pop.setPopulationLimit(3);
    }

    /**
     * Test setPopulationLimit method with invalid argument (population limit == current size).
     */
    @Test
    public void testSetPopulationLimitInvalidArgumentsSameSize() {
        ListPopulation pop = new ListPopulation(5);
        pop.setPopulationLimit(5);
        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test getPopulationSize method.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        assertEquals(1, pop.getPopulationSize());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        assertEquals("[Chromosome[fitness=1.0], Chromosome[fitness=2.0]]", pop.toString());
    }
}
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidParameters() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationWithValidChromosomesAndPopulationLimit() {
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
                return 2.0;
            }
        });

        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosomes.get(1), population.getFittestChromosome());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    @Test
    public void testListPopulationWithValidChromosomesAndPopulationSize() {
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
                return 2.0;
            }
        });

        ListPopulation population = new ListPopulation(chromosomes, 3);
        Assert.assertEquals(3, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        // since the population size is smaller than the initial list size, some chromosomes will be removed
        Assert.assertNotEquals(chromosomes.get(1), population.getFittestChromosome());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }
            });
        }
        ListPopulation population = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testListPopulationWithValidChromosomesAndPopulationLimitAndAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
        Assert.assertEquals(6, population.getPopulationSize());
        Assert.assertEquals(3.0, population.getFittestChromosome().getFitness());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithAddChromosomeExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
    }

    @Test
    public void testListPopulationWithValidChromosomesAndSetChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        initialChromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });

        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(initialChromosomes);

        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(initialChromosomes.get(1), population.getFittestChromosome());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullInitialChromosomes() {
        ListPopulation population = new ListPopulation(null, 5);
        population.setChromosomes(Collections.emptyList());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithInitialChromosomesExceedingPopulationLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            initialChromosomes.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }
            });
        }
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.setChromosomes(initialChromosomes);
    }
}
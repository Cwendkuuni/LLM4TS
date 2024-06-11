import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationCreation() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativeLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesTooMany() {
        ListPopulation population = new ListPopulation(2);
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
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesTooMany() {
        ListPopulation population = new ListPopulation(2);
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
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));
        population.addChromosome(chromosome3);
        Assert.assertEquals(3.0, population.getFittestChromosome().getFitness(), 0.0);
    }
}
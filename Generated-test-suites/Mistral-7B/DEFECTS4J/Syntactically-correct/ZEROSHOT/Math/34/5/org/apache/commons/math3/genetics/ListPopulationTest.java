import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationWithValidInitialChromosomes() {
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{1.0, 2.0};
            }

            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{2.0, 3.0};
            }

            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };

        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullInitialChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithInitialChromosomesBiggerThanPopulationLimit() {
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{1.0, 2.0};
            }

            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{2.0, 3.0};
            }

            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };

        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);
        initialChromosomes.add(chromosome1);

        new ListPopulation(initialChromosomes, 2);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        });
        Assert.assertEquals(6, population.getPopulationSize());
        Assert.assertEquals("Chromosome1", population.getChromosomes().get(5).toString());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        });
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        });
        population.addChromosomes(chromosomes);
        Assert.assertEquals(7, population.getPopulationSize());
        Assert.assertEquals("Chromosome1", population.getChromosomes().get(5).toString());
        Assert.assertEquals("Chromosome2", population.getChromosomes().get(6).toString());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome3";
            }
        });
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{4.0, 5.0};
            }

            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{3.0, 4.0};
            }

            @Override
            public double getFitness() {
                return 7.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        });
        Chromosome fittestChromosome = population.getFittestChromosome();
        Assert.assertEquals("Chromosome2", fittestChromosome.toString());
        Assert.assertEquals(7.0, fittestChromosome.getFitness(), 0.0);
    }
}
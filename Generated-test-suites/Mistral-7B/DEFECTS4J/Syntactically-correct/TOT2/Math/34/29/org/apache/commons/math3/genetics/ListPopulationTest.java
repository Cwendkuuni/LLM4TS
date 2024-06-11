package org.apache.commons.math3.genetics;

import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {
   @Test 
   public void testListPopulationConstructorWithPositiveParameter() {
      ListPopulation population = new ListPopulation(5);
      assertEquals(5, population.getPopulationLimit());
      assertEquals(0, population.getPopulationSize());
   }

   @Test (expected = NotPositiveException.class)
   public void testListPopulationConstructorWithNegativeParameter() {
      ListPopulation population = new ListPopulation(-2);
   }

   @Test
   public void testListPopulationAddChromosome() {
      ListPopulation population = new ListPopulation(2);
      Chromosome chromosome = new Chromosome(new double[]{1.0, 2.0});
      population.addChromosome(chromosome);
      assertEquals(1, population.getPopulationSize());
      Chromosome addedChromosome = population.getChromosomeList().get(0);
      assertEquals(chromosome, addedChromosome);
   }

   @Test (expected = NumberIsTooLargeException.class)
   public void testListPopulationAddChromosomeWithLimitExceeded() {
      ListPopulation population = new ListPopulation(3);
      List<Chromosome> chromosomes = new ArrayList<Chromosome>();
      chromosomes.add(new Chromosome(new double[]{1.0, 2.0}));
      population.setChromosomes(chromosomes);
   }

   @Test
   public void testListPopulationGetFittestChromosome() {
      ListPopulation population = new ListPopulation(3);
      Chromosome chromosome1 = new Chromosome(new double[]{1.0, 2.0});
      Chromosome chromosome2 = new Chromosome(new double[]{3.0, 4.0});
      Chromosome chromosome3 = new Chromosome(new double[]{5.0, 6.0});
      population.addChromosome(chromosome1);
      population.addChromosome(chromosome2);
      population.addChromosome(chromosome3);
      Chromosome expectedFittestChromosome = chromosome3;
      Chromosome fittestChromosome = population.getFittestChromosome();
      assertEquals(expectedFittestChromosome, fittestChromosome);
   }

   @Test
   public void testListPopulationSize() {
      ListPopulation population = new ListPopulation(3);
      population.addChromosome(new Chromosome(new double[]{1.0, 2.0}));
      population.addChromosome(new Chromosome(new double[]{3.0, 4.0}));
      population.addChromosome(new Chromosome(new double[]{5.0, 6.0}));
      assertEquals(3, population.getPopulationSize());
   }
}
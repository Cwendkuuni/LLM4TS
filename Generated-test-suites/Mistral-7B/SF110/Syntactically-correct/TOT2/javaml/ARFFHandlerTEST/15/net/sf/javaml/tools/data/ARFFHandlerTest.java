package com.example.junit;

import static org.junit.Assert.*;

import net.sf.javaml.core.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import org.junit.Test;

public class ARFFHandlerTest {

   @Test
   public void testLoadARFF() {
      File file = new File("testdata.arff");
      Dataset dataset = ARFFHandler.loadARFF(file);
      assertNotNull(dataset);
      assertTrue(dataset instanceof DefaultDataset);
   }

   @Test
   public void testLoadARFFWithClassIndex() {
      File file = new File("testdata.arff");
      Dataset dataset = ARFFHandler.loadARFF(file, 0); //Assuming class index is the first column
      assertNotNull(dataset);
      assertTrue(dataset instanceof DefaultDataset);
   }

   @Test(expected = FileNotFoundException.class)
   public void testLoadARFFWithNotFoundFile() {
      File file = new File("non-existent-file.arff");
      try {
         ARFFHandler.loadARFF(file, 0);
      } catch (FileNotFoundException e) {
         //Expected behavior
      }
   }

   @Test
   public void testLoadEmptyARFF() {
      File file = new File("empty-arff.arff");
      Dataset dataset = ARFFHandler.loadARFF(file);
      assertNotNull(dataset);
      assertTrue(dataset.size() == 0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testLoadARFFWithInvalidClassIndex() {
      File file = new File("testdata.arff");
      try {
         ARFFHandler.loadARFF(file, -1);
      } catch (IllegalArgumentException e) {
         //Expected behavior
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testLoadARFFWithInvalidFormat() {
      File file = new File("testdata-invalid.arff"); //Assuming an invalid ARFF format
      try {
         ARFFHandler.loadARFF(file, 0);
      } catch (IllegalArgumentException e) {
         //Expected behavior
      }
   }
}
package net.sf.javaml.core.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.*;
import net.sf.javaml.core.dataset.Fold;
import net.sf.javaml.distance.*;
import net.sf.javaml.util.random.RandomUtils;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

  private DefaultDataset dataset;
  private List<Instance> instances;
  private DistanceMeasure distanceMeasure;

  @Before
  public void setUp() {
    dataset = new DefaultDataset();
    instances = Arrays.asList(
        new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
        new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2),
        new DenseInstance(new double[] { 0.5, 0.5, 0.5 }, 1),
        new DenseInstance(new double[] { 3.5, 3.5, 3.5 }, 2)
    );
    distanceMeasure = new EuclideanDistance();
    dataset.addAll(instances);
  }

  @Test
  public void testDefaultDatasetConstructionWithInstances() {
    // Given

    // When

    // Then
  }

  @Test
  public void testAddAll() {
    // Given

    // When

    // Then
  }

  @Test
  public void testKNearest() {
    // Given

    // When

    // Then
  }

  @Test
  public void testFolds() {
    // Given

    // When

    // Then
  }

  @Test
  public void testNoAttributes() {
    // Given

    // When

    // Then
  }

  @Test
  public void testClassIndex() {
    // Given

    // When

    // Then
  }

  @Test
  public void testClassValue() {
    // Given

    // When

    // Then
  }

  @Test
  public void testCopy() {
    // Given

    // When

    // Then
  }
}
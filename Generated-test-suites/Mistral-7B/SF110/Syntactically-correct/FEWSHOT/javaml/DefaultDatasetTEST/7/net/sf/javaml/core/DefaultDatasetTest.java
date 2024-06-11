package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private Instance instance;
    private Instance anotherInstance;
    private DistanceMeasure mockDistanceMeasure;
    private SortedSet<Object> mockSortedSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        instance = mock(Instance.class);
        anotherInstance = mock(Instance.class);
        mockDistanceMeasure = mock(DistanceMeasure.class);
        mockSortedSet = mock(SortedSet.class);
        dataset = new DefaultDataset();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNull(dataset.classes);
    }

    @Test
    public void testCollectionConstructor() {
        Collection<Instance> instances = Arrays.asList(instance, anotherInstance);
        dataset = new DefaultDataset(instances);
        verify(instances).size();
        for (Instance i : instances) {
            dataset.check(i);
        }
        assertEquals(instances.size(), dataset.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllThrowsExceptionWhenAddingNullToEmptyDataset() {
        dataset.addAll(null);
    }

    @Test
    public void testAddAllAddsInstancesToDataset() {
        Collection<Instance> instances = Arrays.asList(instance, anotherInstance);
        dataset.addAll(instances);
        for (Instance i : instances) {
            verify(i).getClassValue();
            verify(i).noAttributes();
            dataset.check(i);
        }
        assertEquals(instances.size(), dataset.size());
    }

    @Test
    public void testAddAddsInstanceToDataset() {
        dataset.add(instance);
        verify(instance).getClassValue();
        verify(instance).noAttributes();
        dataset.check(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testClasses() {
        dataset.add(instance);
        dataset.add(anotherInstance);
        when(instance.getClassValue()).thenReturn("class1");
        when(anotherInstance.getClassValue()).thenReturn("class2");
        assertEquals(2, dataset.size());
        assertEquals("class1", dataset.classes().first());
        assertEquals("class2", dataset.classes().last());
    }

    @Test
    public void testKNearestThrowsNullPointerExceptionWhenNullInstancePassed() {
        try {
            dataset.kNearest(0, null, mockDistanceMeasure);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testKNearestReturnsCorrectNumberOfNearestInstances() {
        dataset.add(instance);
        dataset.add(anotherInstance);
        when(mockDistanceMeasure.compare(anyDouble(), anyDouble())).thenReturn(-1);
        when(mockDistanceMeasure.measure(instance, anotherInstance)).thenReturn(1.0);
        when(mockDistanceMeasure.measure(instance, instance)).thenReturn(0.0);
        assertEquals(2, dataset.size());
        SortedSet<Instance> result = dataset.kNearest(1, instance, mockDistanceMeasure);
        assertEquals(1, result.size());
        assertEquals(instance, result.first());
    }

    @Test
    public void testFoldsReturnsCorrectNumberOfFolds() {
        dataset.add(instance);
        dataset.add(anotherInstance);
        when(mockDistanceMeasure.compare(anyDouble(), anyDouble())).thenReturn(-1);
        when(mockDistanceMeasure.measure(instance, anotherInstance)).thenReturn(1.0);
        when(mockDistanceMeasure.measure(instance, instance)).thenReturn(0.0);
        assertEquals(2, dataset.size());
        Dataset[] result = dataset.folds(2, mock(Random.class));
        assertEquals(2, result.length);
    }

    @Test
    public void testCopyReturnsCorrectInstanceCount() {
        dataset.add(instance);
        dataset.add(anotherInstance);
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        assertEquals(dataset.size(), copy.size());
    }
}
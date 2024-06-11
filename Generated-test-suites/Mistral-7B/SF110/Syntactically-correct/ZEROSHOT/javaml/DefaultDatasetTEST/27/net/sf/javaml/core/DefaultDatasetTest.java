package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest
{
    private DefaultDataset ds;

    @Before
    public void setup()
    {
        List<Instance> data = new ArrayList<Instance>();
        Instance i1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        Instance i2 = new DenseInstance(new double[]{1.1, 2.1, 3.1}, 1.0);
        Instance i3 = new DenseInstance(new double[]{1.2, 2.2, 3.2}, 0.0);
        Instance i4 = new DenseInstance(new double[]{1.3, 2.3, 3.3}, 1.0);
        data.add(i1);
        data.add(i2);
        data.add(i3);
        data.add(i4);

        ds = new DefaultDataset(data);
    }

    @Test
    public void testConstructorWithCollection()
    {
        DefaultDataset dataset = new DefaultDataset(ds);
        assertEquals(ds.size(), dataset.size());
        for (int i = 0; i < ds.size(); i++)
        {
            Instance instance1 = ds.instance(i);
            Instance instance2 = dataset.instance(i);
            assertEquals(instance1.noAttributes(), instance2.noAttributes());
            assertEquals(instance1.classValue(), instance2.classValue());
        }
    }

    @Test
    public void testConstructorWithoutCollection()
    {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddAll()
    {
        List<Instance> newData = new ArrayList<Instance>();
        Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        newData.add(newInstance);

        ds.addAll(newData);

        assertEquals(ds.size(), 5);
        Instance instance1 = ds.instance(0);
        Instance instance2 = ds.instance(1);
        Instance instance3 = ds.instance(2);
        Instance newInstance1 = ds.instance(3);
        assertEquals(instance1.noAttributes(), instance3.noAttributes());
        assertEquals(instance1.classValue(), instance2.classValue());
        assertEquals(instance1.noAttributes(), newInstance1.noAttributes());
        assertEquals(instance1.classValue(), newInstance1.classValue());
    }

    @Test
    public void testAdd()
    {
        Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        ds.add(newInstance);

        assertEquals(ds.size(), 5);
        Instance instance1 = ds.instance(0);
        Instance newInstance1 = ds.instance(4);
        assertEquals(instance1.noAttributes(), newInstance1.noAttributes());
        assertEquals(instance1.classValue(), newInstance1.classValue());
    }

    @Test
    public void testAddElement()
    {
        Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        ds.addElement(newInstance);

        assertEquals(ds.size(), 5);
        Instance instance1 = ds.instance(0);
        Instance newInstance1 = ds.instance(4);
        assertEquals(instance1.noAttributes(), newInstance1.noAttributes());
        assertEquals(instance1.classValue(), newInstance1.classValue());
    }

    @Test
    public void testInsertElementAt()
    {
        Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        ds.insertElementAt(newInstance, 1);

        assertEquals(ds.size(), 5);
        Instance instance0 = ds.instance(0);
        Instance newInstance0 = ds.instance(1);
        Instance instance1 = ds.instance(2);
        assertEquals(instance0.noAttributes(), instance1.noAttributes());
        assertEquals(instance0.classValue(), newInstance0.classValue());
        assertEquals(instance0.noAttributes(), newInstance0.noAttributes());
        assertEquals(instance1.noAttributes(), newInstance1.noAttributes());
        assertEquals(instance1.classValue(), newInstance1.classValue());
    }

    @Test
    public void testSetElementAt()
    {
        Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        ds.setElementAt(newInstance, 1);

        assertEquals(ds.size(), 5);
        Instance instance0 = ds.instance(0);
        Instance newInstance0 = ds.instance(1);
        Instance instance1 = ds.instance(2);
        assertEquals(instance0.noAttributes(), instance1.noAttributes());
        assertEquals(instance0.classValue(), newInstance0.classValue());
        assertEquals(newInstance0.noAttributes(), newInstance0.noAttributes());
        assertEquals(newInstance0.classValue(), 1.0);
        assertEquals(instance1.noAttributes(), newInstance1.noAttributes());
        assertEquals(instance1.classValue(), newInstance1.classValue());
    }

    @Test
    public void testInstance()
    {
        Instance instance = ds.instance(0);
        assertEquals(instance.noAttributes(), 3);
        assertEquals(instance.classValue(), 0.0);
    }

    @Test
    public void testClasses()
    {
        Set<Object> classes = ds.classes();
        assertEquals(classes.size(), 2);
        assertTrue(classes.contains(0.0));
        assertTrue(classes.contains(1.0));
    }

    @Test
    public void testKNearest()
    {
        List<Instance> expected = Arrays.asList(ds.instance(1), ds.instance(2), ds.instance(0));
        DistanceMeasure euclideanDistance = new EuclideanDistance();
        Set<Instance> result = ds.kNearest(3, ds.instance(0), euclideanDistance);
        Comparator<Instance> comparator = new Comparator<Instance>()
        {
            @Override
            public int compare(Instance o1, Instance o2)
            {
                double distance1 = euclideanDistance.measure(ds.instance(0), o1);
                double distance2 = euclideanDistance.measure(ds.instance(0), o2);
                return Double.compare(distance1, distance2);
            }
        };
        Collections.sort(new ArrayList<Instance>(result), comparator);
        List<Instance> resultSorted = new ArrayList<Instance>(result);
        assertEquals(resultSorted, expected);
    }

    @Test
    public void testFolds()
    {
        int numFolds = 3;
        Random random = new Random(123);
        Dataset[] expected = new Dataset[numFolds];

        for (int i = 0; i < numFolds; i++)
        {
            List<Integer> indices = new ArrayList<Integer>();
            for (int j = 0; j < ds.size(); j++)
            {
                indices.add(j);
            }
            int[][] array = new int[numFolds][ds.size()];
            for (int j = 0; j < ds.size(); j++)
            {
                array[i % numFolds][j] = indices.remove(random.nextInt(indices.size()));
            }
            expected[i] = new Fold(ds, array[i]);
        }

        Dataset[] result = ds.folds(numFolds, random);
        for (int i = 0; i < numFolds; i++)
        {
            Instance instance = result[i].instance(0);
            Instance expectedInstance = expected[i].instance(0);
            assertEquals(instance.noAttributes(), expectedInstance.noAttributes());
            assertEquals(instance.classValue(), expectedInstance.classValue());
        }
    }

    @Test
    public void testNoAttributes()
    {
        assertEquals(ds.noAttributes(), 3);
    }

    @Test
    public void testClassIndex()
    {
        assertEquals(ds.classIndex(0.0), 0);
        assertEquals(ds.classIndex(1.0), 1);
    }

    @Test
    public void testClassValue()
    {
        assertEquals(ds.classValue(0), 0.0);
        assertEquals(ds.classValue(1), 1.0);
    }

    @Test
    public void testCopy()
    {
        DefaultDataset copy = ds.copy();
        assertEquals(ds.size(), copy.size());
        for (int i = 0; i < ds.size(); i++)
        {
            Instance instance1 = ds.instance(i);
            Instance instance2 = copy.instance(i);
            assertEquals(instance1.noAttributes(), instance2.noAttributes());
            assertEquals(instance1.classValue(), instance2.classValue());
        }
    }
}
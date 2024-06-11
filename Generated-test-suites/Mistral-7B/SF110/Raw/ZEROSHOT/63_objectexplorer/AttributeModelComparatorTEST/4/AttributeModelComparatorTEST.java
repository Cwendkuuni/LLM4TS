package de.paragon.explorer.model.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_Null_Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        Assert.assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompare_Null_NonNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        Assert.assertNull(comparator.compare(null, model1));
        Assert.assertTrue(comparator.compare(model1, null) < 0);
    }

    @Test
    public void testCompare_NonNull_Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        Assert.assertTrue(comparator.compare(model1, null) > 0);
        Assert.assertNull(comparator.compare(null, model2));
    }

    @Test
    public void testCompare_SameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "Value2");
        Assert.assertFalse(comparator.compare(model1, model2) < 0);
        Assert.assertFalse(comparator.compare(model2, model1) < 0);
        Assert.assertEquals(0, comparator.compare(model1, model2));
        Assert.assertEquals(0, comparator.compare(model2, model1));
    }

    @Test
    public void testCompare_DifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        Assert.assertTrue(comparator.compare(model1, model2) < 0);
        Assert.assertFalse(comparator.compare(model2, model1) < 0);
        Assert.assertFalse(comparator.compare(model1, model2) == 0);
        Assert.assertFalse(comparator.compare(model2, model1) == 0);
    }

    @Test
    public void testCompare_DifferentNames_A_First() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        Assert.assertTrue(comparator.compare(model1, model2) > 0);
        Assert.assertFalse(comparator.compare(model2, model1) > 0);
    }

    @Test
    public void testCompare_DifferentNames_A_Last() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("a", "Value2");
        Assert.assertTrue(comparator.compare(model2, model1) < 0);
        Assert.assertFalse(comparator.compare(model1, model2) < 0);
    }

    @Test
    public void testCompare_SameName_DifferentValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "Value2");
        Assert.assertTrue(comparator.compare(model1, model2) < 0);
        Assert.assertFalse(comparator.compare(model2, model1) < 0);
    }

    @Test
    public void testComparator_Equals() {
        AttributeModelComparator comparator1 = new AttributeModelComparator();
        AttributeModelComparator comparator2 = new AttributeModelComparator();
        Assert.assertSame(comparator1, comparator2);
    }

    @Test
    public void testComparator_NotEquals() {
        AttributeModelComparator comparator1 = new AttributeModelComparator();
        Object obj = new Object();
        Assert.assertNotSame(comparator1, obj);
    }

    @Test
    public void testSerializability() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(comparator);
        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objInput = new ObjectInputStream(byteInputStream);
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) objInput.readObject();
        Assert.assertNotNull(deserializedComparator);
        Assert.assertEquals(comparator, deserializedComparator);
    }

    @Test
    public void testComparator_Null_Null_InList() {
        List<AttributeModel> models = new ArrayList<>();
        models.add(null);
        models.add(new AttributeModel("Name1", "Value1"));
        models.add(new AttributeModel("Name2", "Value2"));
        models.add(null);

        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> sortedModels = new ArrayList<>(models);
        Collections.sort(sortedModels, comparator);

        List<String> expectedNames = Arrays.asList("Name1", "Name2");
        List<String> actualNames = new ArrayList<>();
        for (AttributeModel model : sortedModels) {
            actualNames.add(model.getName());
        }
        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testComparator_Null_InList_First() {
        List<AttributeModel> models = Arrays.asList(null, new AttributeModel("Name1", "Value1"), new AttributeModel("Name2", "Value2"));
        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> sortedModels = new ArrayList<>(models);
        Collections.sort(sortedModels, comparator);

        List<String> expectedNames = Arrays.asList("Name1", "Name2");
        List<String> actualNames = new ArrayList<>();
        for (AttributeModel model : sortedModels) {
            actualNames.add(model.getName());
        }
        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testComparator_Null_InList_Last() {
        List<AttributeModel> models = new ArrayList<>();
        models.add(new AttributeModel("Name1", "Value1"));
        models.add(new AttributeModel("Name2", "Value2"));
        models.add(null);

        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> sortedModels = new ArrayList<>(models);
        Collections.sort(sortedModels, comparator);

        List<String> expectedNames = Arrays.asList("Name1", "Name2");
        List<String> actualNames = new ArrayList<>();
        for (AttributeModel model : sortedModels) {
            actualNames.add(model.getName());
        }
        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testComparator_DifferentNames_A_First() {
        List<AttributeModel> models = Arrays.asList(
                new AttributeModel("a", "Value1"),
                new AttributeModel("Name1", "Value1"),
                new AttributeModel("Name2", "Value2")
        );
        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> sortedModels = new ArrayList<>(models);
        Collections.sort(sortedModels, comparator);

        List<String> expectedNames = Arrays.asList("a", "Name1", "Name2");
        List<String> actualNames = new ArrayList<>();
        for (AttributeModel model : sortedModels) {
            actualNames.add(model.getName());
        }
        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testComparator_DifferentNames_A_Last() {
        List<AttributeModel> models = Arrays.asList(
                new AttributeModel("Name1", "Value1"),
                new AttributeModel("Name2", "Value2"),
                new AttributeModel("a", "Value1")
        );
        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> sortedModels = new ArrayList<>(models);
        Collections.sort(sortedModels, comparator);

        List<String> expectedNames = Arrays.asList("Name1", "Name2", "a");
        List<String> actualNames = new ArrayList<>();
        for (AttributeModel model : sortedModels) {
            actualNames.add(model.getName());
        }
        Assert.assertEquals(expectedNames, actualNames);
    }
}
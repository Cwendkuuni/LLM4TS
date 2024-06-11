package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel createAttributeModel(String name) {
 return new AttributeModel(name);
 }

 @Test
 public void testCompareNullAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertNull(comparator.compare(null, null));
 }

 @Test
 public void testCompareEqualAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("name1");
 AttributeModel o2 = createAttributeModel("name1");
 int result = comparator.compare(o1, o2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareDifferentAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("name1");
 AttributeModel o2 = createAttributeModel("name2");
 int result = comparator.compare(o1, o2);
 assertNotEquals(0, result);
 }

 @Test
 public void testCompareIgnoreCase() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("name1");
 AttributeModel o2 = createAttributeModel("Name1");
 int result = comparator.compare(o1, o2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareWithA() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("name1");
 AttributeModel o2 = createAttributeModel("a");
 int result = comparator.compare(o1, o2);
 assertLessThan(0, result);

 AttributeModel o3 = createAttributeModel("a");
 AttributeModel o4 = createAttributeModel("name1");
 int result2 = comparator.compare(o3, o4);
 assertGreaterThan(0, result2);
 }

 @Test
 public void testCompareWithNullAttributeModel() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("name1");
 AttributeModel o2 = null;
 int result = comparator.compare(o1, o2);
 assertNotNull(result);
 assertLessThan(0, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareWithNullNameAttributeModel() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel(null);
 AttributeModel o2 = createAttributeModel("name2");
 comparator.compare(o1, o2);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareWithNullNameAttributeModel2() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("name1");
 AttributeModel o2 = new AttributeModel(null);
 comparator.compare(o1, o2);
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 AttributeModelComparator comparator = new AttributeModelComparator();
 oos.writeObject(comparator);
 byte[] byteArray = baos.toByteArray();
 ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
 ObjectInputStream ois = new ObjectInputStream(bais);
 AttributeModelComparator deserializedComparator = (AttributeModelComparator) ois.readObject();
 assertEquals(comparator, deserializedComparator);
 }

}
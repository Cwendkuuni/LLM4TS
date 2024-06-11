package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel model1, model2, model3, model4;

 @Test
 public void testCompare_sameName_returnsZero() {
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("name1", "value2");

 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_differentNames_returnsNegativeOrPositive() {
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("name2", "value2");

 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertNotEquals(0, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_nullArguments_throwsNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(null, model2);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_nullSecondArgument_throwsNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, null);
 }

 @Test
 public void testCompare_aName_smallerThanAnotherName() {
 model1 = new AttributeModel("nameA", "valueA");
 model2 = new AttributeModel("nameB", "valueB");

 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertTrue(result < 0);
 }

 @Test
 public void testCompare_aName_greaterThanAnotherName() {
 model1 = new AttributeModel("nameA", "valueA");
 model2 = new AttributeModel("nameB", "valueB");

 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model2, model1);
 assertTrue(result > 0);
 }

 @Test
 public void testCompare_aName_equalToAnotherName() {
 model1 = new AttributeModel("nameA", "valueA");
 model2 = new AttributeModel("nameA", "valueA");

 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_aName_equalToSpecialName_returnsSpecialBehavior() {
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("a", "value2");

 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertEquals(1, result);

 model1 = new AttributeModel("a", "value1");
 model2 = new AttributeModel("name2", "value2");

 AttributeModelComparator comparator2 = new AttributeModelComparator();
 int result2 = comparator2.compare(model1, model2);
 assertEquals(-1, result2);
 }

 @Test
 public void testCompare_twoObjectsWithSameNameButDifferentValues() {
 model1 = new AttributeModel("nameA", "value1");
 model2 = new AttributeModel("nameA", "value2");

 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertNotEquals(0, result);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 byte[] byteArray = serialize(new AttributeModelComparator());
 AttributeModelComparator deserializedObject = deserialize(byteArray);
 }

 private byte[] serialize(Object obj) throws IOException {
 ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
 ObjectOutputStream objOutputStream = new ObjectOutputStream(byteArrayOS);
 objOutputStream.writeObject(obj);
 return byteArrayOS.toByteArray();
 }

 private Object deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
 ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 Object obj = objInputStream.readObject();
 return obj;
 }
}
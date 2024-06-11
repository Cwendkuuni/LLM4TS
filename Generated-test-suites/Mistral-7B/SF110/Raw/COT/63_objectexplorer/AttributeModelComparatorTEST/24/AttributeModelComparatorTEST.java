package de.paragon.explorer.model.test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.isA;

import org.junit.Before;
import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {

 private AttributeModelComparator comparator;

 @Before
 public void setup() {
 comparator = new AttributeModelComparator();
 }

 @Test
 public void testCompare_whenBothObjectsAreNotNull_thenCompareNames() {
 AttributeModel model1 = new AttributeModel("Name1", "Value1");
 AttributeModel model2 = new AttributeModel("Name2", "Value2");

 int result = comparator.compare(model1, model2);
 String expected = "Name1".compareToIgnoreCase("Name2");

 assertThat(result, is(expected));
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenFirstObjectIsNull_thenThrowNullPointerException() {
 AttributeModel model1 = null;
 AttributeModel model2 = new AttributeModel("Name2", "Value2");

 comparator.compare(model1, model2);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenSecondObjectIsNull_thenThrowNullPointerException() {
 AttributeModel model1 = new AttributeModel("Name1", "Value1");
 AttributeModel model2 = null;

 comparator.compare(model1, model2);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameName_thenReturnZero() {
 AttributeModel model1 = new AttributeModel("NameA", "Value1");
 AttributeModel model2 = new AttributeModel("NameA", "Value2");

 int result = comparator.compare(model1, model2);

 assertThat(result, is(0));
 }

 @Test
 public void testCompare_whenFirstObjectHasNameLessThanSecondObject_thenReturnNegativeNumber() {
 AttributeModel model1 = new AttributeModel("NameB", "Value1");
 AttributeModel model2 = new AttributeModel("NameC", "Value2");

 int result = comparator.compare(model1, model2);

 assertThat(result, is(-1));
 }

 @Test
 public void testCompare_whenFirstObjectHasNameGreaterThanSecondObject_thenReturnPositiveNumber() {
 AttributeModel model1 = new AttributeModel("NameD", "Value1");
 AttributeModel model2 = new AttributeModel("NameB", "Value2");

 int result = comparator.compare(model1, model2);

 assertThat(result, is(1));
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameEqualToASpecificValue_thenReturnSpecialBehavior() {
 AttributeModel model1 = new AttributeModel("a", "Value1");
 AttributeModel model2 = new AttributeModel("NameB", "Value2");

 int result = comparator.compare(model1, model2);

 assertThat(result, is(-1));
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameLessThanASpecificValue_thenReturnSpecialBehavior() {
 AttributeModel model1 = new AttributeModel("NameA", "Value1");
 AttributeModel model2 = new AttributeModel("a", "Value2");

 int result = comparator.compare(model1, model2);

 assertThat(result, is(1));
 }

 @Test
 public void testCompare_whenBothObjectsAreSerializable() {
 AttributeModel model1 = new AttributeModel("Name1", "Value1");
 AttributeModel model2 = new AttributeModel("Name2", "Value2");

 byte[] model1Bytes = serialize(model1);
 byte[] model2Bytes = serialize(model2);

 AttributeModel deserializedModel1 = deserialize(model1Bytes);
 AttributeModel deserializedModel2 = deserialize(model2Bytes);

 int result = comparator.compare(deserializedModel1, deserializedModel2);

 String expected = "Name1".compareToIgnoreCase("Name2");

 assertThat(result, is(expected));
 }

 private byte[] serialize(Object object) {
 byte[] bytes = null;
 try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(object);
 bytes = baos.toByteArray();
 } catch (IOException e) {
 e.printStackTrace();
 }
 return bytes;
 }

 private Object deserialize(byte[] bytes) {
 Object object = null;
 try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
 ObjectInputStream ois = new ObjectInputStream(bais);
 object = ois.readObject();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 return object;
 }
}
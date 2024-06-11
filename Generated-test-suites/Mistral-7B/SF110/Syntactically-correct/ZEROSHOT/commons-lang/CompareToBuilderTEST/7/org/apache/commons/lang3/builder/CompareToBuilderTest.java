package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

public class CompareToBuilderTest {

 @Test
 public void testCompareToBuilder_defaultConstructor() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int comparison = compareToBuilder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testCompareToBuilder_append_int_int() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(1, 1);
 int comparison = compareToBuilder.toComparison();
 assertEquals(0, comparison);
 compareToBuilder.append(1, 2);
 comparison = compareToBuilder.toComparison();
 assertEquals(-1, comparison);
 compareToBuilder.append(2, 1);
 comparison = compareToBuilder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToBuilder_append_Object_Object() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append("abc", "abc");
 int comparison = compareToBuilder.toComparison();
 assertEquals(0, comparison);
 compareToBuilder.append("abc", "def");
 comparison = compareToBuilder.toComparison();
 assertEquals(-1, comparison);
 compareToBuilder.append("def", "abc");
 comparison = compareToBuilder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToBuilder_append_long_long() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(1L, 1L);
 int comparison = compareToBuilder.toComparison();
 assertEquals(0, comparison);
 compareToBuilder.append(1L, 2L);
 comparison = compareToBuilder.toComparison();
 assertEquals(-1, comparison);
 compareToBuilder.append(2L, 1L);
 comparison = compareToBuilder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToBuilder_append_intArray_intArray() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int[] arr1 = new int[] { 1, 2, 3 };
 int[] arr2 = new int[] { 1, 2, 3 };
 compareToBuilder.append(arr1, arr2);
 int comparison = compareToBuilder.toComparison();
 assertEquals(0, comparison);
 int[] arr3 = new int[] { 1, 2, 3, 4 };
 comparison = compareToBuilder.toComparison(arr1, arr3);
 assertEquals(-1, comparison);
 comparison = compareToBuilder.toComparison(arr3, arr1);
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToBuilder_append_StringArray_StringArray() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String[] arr1 = new String[] { "abc", "def", "ghi" };
 String[] arr2 = new String[] { "abc", "def", "ghi" };
 compareToBuilder.append(arr1, arr2);
 int comparison = compareToBuilder.toComparison();
 assertEquals(0, comparison);
 String[] arr3 = new String[] { "abc", "def", "ghi", "jkl" };
 comparison = compareToBuilder.toComparison(arr1, arr3);
 assertEquals(-1, comparison);
 comparison = compareToBuilder.toComparison(arr3, arr1);
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToBuilder_append_Object_Object_Comparator() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String str1 = new String("abc");
 String str2 = new String("def");
 compareToBuilder.append(str1, str2, new Comparator<String>() {
 @Override
 public int compare(String o1, String o2) {
 return o1.length() - o2.length();
 }
 });
 int comparison = compareToBuilder.toComparison();
 assertEquals(-1, comparison);
 compareToBuilder.append(str2, str1, new Comparator<String>() {
 @Override
 public int compare(String o1, String o2) {
 return o1.length() - o2.length();
 }
 });
 comparison = compareToBuilder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToBuilder_appendSuper() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 CompareToBuilder compareToBuilder2 = new CompareToBuilder();
 compareToBuilder2.append(1, 2);
 compareToBuilder.appendSuper(compareToBuilder2.toComparison());
 int comparison = compareToBuilder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareToBuilder_null_lhs() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(null, 1);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareToBuilder_null_rhs() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(1, null);
 }

 @Test
 public void testCompareToBuilder_reflectionCompare() {
 Object obj1 = new MyClass(1, "abc");
 Object obj2 = new MyClass(2, "def");
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
 assertEquals(-1, comparison);
 }

 @Test
 public void testCompareToBuilder_reflectionCompare_excludeFields() {
 Object obj1 = new MyClass(1, "abc");
 Object obj2 = new MyClass(2, "def");
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("field1", "field2"));
 assertEquals(0, comparison);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToBuilder_reflectionCompare_differentClasses() {
 Object obj1 = new MyClass1(1);
 Object obj2 = new MyClass2(2);
 CompareToBuilder.reflectionCompare(obj1, obj2);
 }

 @Test
 public void testCompareToBuilder_reflectionCompare_transients() {
 Object obj1 = new MyClassWithTransientField(1, "abc");
 Object obj2 = new MyClassWithTransientField(2, "def");
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, true);
 assertEquals(0, comparison);
 }

 @Test
 public void testCompareToBuilder_reflectionCompare_superClass() {
 Object obj1 = new MySubClass(1, "abc");
 Object obj2 = new MySuperClass(2, "def");
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, MySubClass.class, MySuperClass.class);
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToBuilder_reflectionCompare_multipleLevels() {
 Object obj1 = new MySubClass2(1, new MySubClass(1, "abc"));
 Object obj2 = new MySuperClass(2, new MySubClass(2, "def"));
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, MySubClass2.class, MySuperClass.class);
 assertEquals(-1, comparison);
 }

}

class MyClass {
 int field1;
 String field2;

 MyClass(int field1, String field2) {
 this.field1 = field1;
 this.field2 = field2;
 }

 @Override
 public boolean equals(Object o) {
 if (this == o) {
 return true;
 }
 if (o == null || getClass() != this.getClass()) {
 return false;
 }
 MyClass myClass = (MyClass) o;
 return field1 == myClass.field1 && field2.equals(myClass.field2);
 }

 @Override
 public String toString() {
 return "MyClass{" +
 "field1=" + field1 +
 ", field2='" + field2 + '\'' +
 '}';
 }
}

class MyClassWithTransientField {
 @SuppressWarnings("transientField")
 transient int transientField;
 int field1;
 String field2;

 MyClassWithTransientField(int field1, String field2) {
 this.field1 = field1;
 this.field2 = field2;
 }

 @Override
 public boolean equals(Object o) {
 if (this == o) {
 return true;
 }
 if (o == null || getClass() != this.getClass()) {
 return false;
 }
 MyClassWithTransientField myClass = (MyClassWithTransientField) o;
 return field1 == myClass.field1 && field2.equals(myClass.field2);
 }

 @Override
 public String toString() {
 return "MyClassWithTransientField{" +
 "transientField=" + transientField +
 ", field1=" + field1 +
 ", field2='" + field2 + '\'' +
 '}';
 }
}

class MySubClass extends MySuperClass {
 int field3;

 MySubClass(int field1, String field2) {
 super(field1, field2);
 this.field3 = 1;
 }

 @Override
 public boolean equals(Object o) {
 if (this == o) {
 return true;
 }
 if (!(o instanceof MySubClass)) {
 return false;
 }
 MySubClass mySubClass = (MySubClass) o;
 return super.equals(mySubClass) && field3 == mySubClass.field3;
 }

 @Override
 public String toString() {
 return "MySubClass{" +
 "field1=" + super.getField1() +
 ", field2='" + super.getField2() + '\'' +
 ", field3=" + field3 +
 '}';
 }
}

class MySuperClass {
 int field1;
 String field2;

 MySuperClass(int field1, String field2) {
 this.field1 = field1;
 this.field2 = field2;
 }

 int getField1() {
 return field1;
 }

 String getField2() {
 return field2;
 }

 @Override
 public boolean equals(Object o) {
 if (this == o) {
 return true;
 }
 if (o == null || getClass() != this.getClass()) {
 return false;
 }
 MySuperClass mySuperClass = (MySuperClass) o;
 return field1 == mySuperClass.field1 && field2.equals(mySuperClass.field2);
 }

 @Override
 public String toString() {
 return "MySuperClass{" +
 "field1=" + field1 +
 ", field2='" + field2 + '\'' +
 '}';
 }
}

class MySubClass2 extends MySuperClass {
 MySubClass mySubClass;

 MySubClass2(int field1, MySubClass mySubClass) {
 super(field1, "");
 this.mySubClass = mySubClass;
 }

 @Override
 public boolean equals(Object o) {
 if (this == o) {
 return true;
 }
 if (!(o instanceof MySubClass2)) {
 return false;
 }
 MySubClass2 mySubClass2 = (MySubClass2) o;
 return super.equals(mySubClass2) && mySubClass.equals(mySubClass2.mySubClass);
 }

 @Override
 public String toString() {
 return "MySubClass2{" +
 "field1=" + super.getField1() +
 ", mySubClass=" + mySubClass +
 '}';
 }
}
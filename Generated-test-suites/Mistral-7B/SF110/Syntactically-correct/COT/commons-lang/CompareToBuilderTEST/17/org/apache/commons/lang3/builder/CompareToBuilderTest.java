package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private CompareToBuilder builder;

 @Test
 public void testComparison_Objects() {
      lhs = new Object();
      rhs = new Object();
      builder = new CompareToBuilder();
      builder.append(lhs, rhs);
      assertEquals(-1, builder.toComparison());

      lhs = new Object();
      rhs = lhs;
      builder = new CompareToBuilder();
      builder.append(lhs, rhs);
      assertEquals(0, builder.toComparison());

      lhs = new Object();
      rhs = null;
      builder = new CompareToBuilder();
      builder.append(lhs, rhs);
      assertEquals(1, builder.toComparison());

      lhs = null;
      rhs = new Object();
      builder = new CompareToBuilder();
      builder.append(lhs, rhs);
      assertEquals(-1, builder.toComparison());

      lhs = new Object();
      rhs = new Object();
      builder = new CompareToBuilder();
      builder.append(rhs, lhs);
      assertEquals(1, builder.toComparison());
 }

 @Test
 public void testComparison_Arrays_ObjectComparator() {
      Object[] lhs = {new Object(), new Object()};
      Object[] rhs = {new Object(), new Object()};
      Comparator<?> comparator = Comparator.comparingInt(o -> ((Object)o).hashCode());
      builder = new CompareToBuilder();
      builder.append(lhs, rhs, comparator);
      assertEquals(0, builder.toComparison());

      Object[] lhs1 = {new Object(), new Object()};
      Object[] rhs1 = {new Object(), new Object(), new Object()};
      builder = new CompareToBuilder();
      builder.append(lhs1, rhs1, comparator);
      assertEquals(-1, builder.toComparison());

      Object[] lhs2 = {null, new Object(), new Object()};
      Object[] rhs2 = {new Object(), new Object(), new Object()};
      builder = new CompareToBuilder();
      builder.append(lhs2, rhs2, comparator);
      assertEquals(-1, builder.toComparison());

      Object[] lhs3 = {new Object(), null, new Object()};
      Object[] rhs3 = {new Object(), new Object(), new Object()};
      builder = new CompareToBuilder();
      builder.append(lhs3, rhs3, comparator);
      assertEquals(1, builder.toComparison());

      Object[] lhs4 = {new Object(), new Object(), new Object()};
      Object[] rhs4 = null;
      builder = new CompareToBuilder();
      builder.append(lhs4, rhs4, comparator);
      assertEquals(-1, builder.toComparison());
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException_Objects() {
      lhs = null;
      rhs = new Object();
      builder = new CompareToBuilder();
      builder.append(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testClassCastException_Objects() {
      lhs = new String("hello");
      rhs = new Integer(1);
      builder = new CompareToBuilder();
      builder.append(lhs, rhs);
 }

 @Test(expected = InternalError.class)
 public void testUnexpectedIllegalAccessException_Objects() {
      lhs = new Object();
      rhs = new Object();
      try {
          builder.append(lhs, rhs, new CompareToBuilder());
      } catch (IllegalAccessException e) {
          throw new InternalError();
      }
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException_Arrays_Comparator() {
      Object[] lhs = new Object[2];
      Object[] rhs = new Object[2];
      Comparator<?> comparator = null;
      builder = new CompareToBuilder();
      builder.append(lhs, rhs, comparator);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException_Arrays() {
      Object[] lhs = null;
      Object[] rhs = new Object[2];
      builder = new CompareToBuilder();
      builder.append(lhs, rhs);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException_Arrays_Comparator_Comparator() {
      Object[] lhs = new Object[2];
      Object[] rhs = new Object[2];
      Comparator<?> comparator1 = null;
      Comparator<?> comparator2 = Comparator.comparingInt(o -> ((Object)o).hashCode());
      builder = new CompareToBuilder();
      builder.append(lhs, rhs, comparator1, comparator2);
 }

}
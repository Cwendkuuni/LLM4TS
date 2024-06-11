package com.example.test.jackson;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

 private ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);

 @Test
 public void testValueClass() {
 assertSame(String.class, valueInstantiator.getValueClass());
 }

 @Test
 public void testValueTypeDesc() {
 assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
 }

 @Test
 public void testCanInstantiate() {
 assertFalse(valueInstantiator.canInstantiate());
 }

 @Test
 public void testCanCreateFromString() {
 assertFalse(valueInstantiator.canCreateFromString());
 }

 @Test
 public void testCanCreateFromInt() {
 assertFalse(valueInstantiator.canCreateFromInt());
 }

 @Test
 public void testCanCreateFromLong() {
 assertFalse(valueInstantiator.canCreateFromLong());
 }

 @Test
 public void testCanCreateFromDouble() {
 assertFalse(valueInstantiator.canCreateFromDouble());
 }

 @Test
 public void testCanCreateFromBoolean() {
 assertFalse(valueInstantiator.canCreateFromBoolean());
 }

 @Test
 public void testCanCreateUsingDefault() {
 assertFalse(valueInstantiator.canCreateUsingDefault());
 }

 @Test
 public void testCanCreateUsingDelegate() {
 assertFalse(valueInstantiator.canCreateUsingDelegate());
 }

 @Test
 public void testCanCreateFromObjectWith() {
 assertFalse(valueInstantiator.canCreateFromObjectWith());
 }

 @Test
 public void testCreateUsingDefault() throws IOException {
 try {
 valueInstantiator.createUsingDefault(null);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateFromObjectWith() throws IOException {
 try {
 valueInstantiator.createFromObjectWith(null, new Object[0]);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateUsingDelegate() throws IOException {
 try {
 valueInstantiator.createUsingDelegate(null, null);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateUsingArrayDelegate() throws IOException {
 try {
 valueInstantiator.createUsingArrayDelegate(null, null);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateFromString() throws IOException {
 try {
 valueInstantiator.createFromString(null, "");
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateFromInt() throws IOException {
 try {
 valueInstantiator.createFromInt(null, 0);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateFromLong() throws IOException {
 try {
 valueInstantiator.createFromLong(null, 0L);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateFromDouble() throws IOException {
 try {
 valueInstantiator.createFromDouble(null, 0.0);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }

 @Test
 public void testCreateFromBoolean() throws IOException {
 try {
 valueInstantiator.createFromBoolean(null, false);
 fail("Expected MissingInstantiatorException");
 } catch (MissingInstantiatorException e) {
 // Expected
 }
 }
}
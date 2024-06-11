package org.apache.commons.cli; // Replace with the actual package name if necessary

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TShortByteMapDecoratorTest {

 private TShortByteMapDecorator decorator;
 @Mock
 private TShortByteMap mapMock;

 @Before
 public void setUp() {
 this.decorator = new TShortByteMapDecorator(this.mapMock);
 }

 @Test
 public void testPut() {
 short key = (short) 5;
 byte value = 10;
 byte returnedValue = this.decorator.put(key, value);

 assertEquals(value, returnedValue);
 verify(this.mapMock, times(1)).put((short) 5, value);
 }

 @Test
 public void testGet() {
 short key = (short) 5;
 byte expectedValue = 10;
 byte actualValue = this.decorator.get(key);

 assertEquals(expectedValue, actualValue);
 verify(this.mapMock, times(1)).get((short) 5);
 }

 @Test
 public void testClear() {
 this.decorator.clear();
 verify(this.mapMock, times(1)).clear();
 }

 @Test
 public void testRemove() {
 short key = (short) 5;
 byte value = 10;
 this.decorator.put(key, value);
 Byte removedValue = this.decorator.remove(key);

 assertEquals(value, removedValue);
 verify(this.mapMock, times(1)).remove((short) 5);
 }

 @Test
 public void testEntrySet() {
 TShortByteMapIterator mapIterator = mock(TShortByteIterator.class);
 when(this.mapMock.iterator()).thenReturn(mapIterator);

 Set<Entry<Short, Byte>> entrySet = this.decorator.entrySet();
 assertEquals(this.mapMock.size(), entrySet.size());
 }

 @Test
 public void testContainsValue() {
 byte value = 10;
 assertTrue(this.decorator.containsValue(value));
 verify(this.mapMock, times(1)).containsValue(value);
 }

 @Test
 public void testContainsKey() {
 short key = (short) 5;
 assertTrue(this.decorator.containsKey(key));
 verify(this.mapMock, times(1)).containsKey(key);
 }

 @Test
 public void testSize() {
 assertEquals(this.mapMock.size(), this.decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(this.mapMock.isEmpty(), this.decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 Map<Short, Byte> mapToAdd = new HashMap<>();
 mapToAdd.put((short) 1, (byte) 1);
 mapToAdd.put((short) 2, (byte) 2);
 this.decorator.putAll(mapToAdd);

 assertEquals(mapToAdd.size(), this.decorator.size());
 for (Entry<Short, Byte> entry : mapToAdd.entrySet()) {
 assertEquals(entry.getKey(), this.decorator.get(entry.getKey()));
 assertEquals(entry.getValue(), this.decorator.get(entry.getKey()));
 }
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 this.decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TShortByteMapDecorator readDecorator = new TShortByteMapDecorator();
 readDecorator.readExternal(in);
 assertEquals(this.decorator, readDecorator);
 }

}
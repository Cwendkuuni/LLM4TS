package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatCharHashTest {

private TFloatCharHash<Float, Character> hash;

@Test
public void testEmptyHashConstructor() {
hash = new TFloatCharHash<>();
assertEquals(0, hash.size());
}

@Test
public void testIntInitialCapacityConstructor() {
hash = new TFloatCharHash(5);
assertEquals(5, hash.size());
}

@Test
public void testIntInitialCapacityLoadFactorConstructor() {
hash = new TFloatCharHash(5, 0.75f);
assertEquals(5, hash.size());
assertEquals(3, hash.capacity());
}

@Test
public void testIntInitialCapacityLoadFactorNoEntryKeyNoEntryValueConstructor() {
hash = new TFloatCharHash(5, 0.75f, 1.0f, 'X'); ;
assertEquals(5, hash.size());
assertEquals(3, hash.capacity());
assertEquals(1.0f, hash.getNoEntryKey());
assertEquals('X', hash.getNoEntryValue());
}

@Test
public void testSetUp() {
hash = new TFloatCharHash<>(5);
assertEquals(5, hash.size());
assertEquals(5, hash.capacity());
}

@Test
public void testContains() {
hash = new TFloatCharHash<>(5);
hash.insert(1.0f, 'A');
hash.insert(2.0f, 'B');
assertTrue(hash.contains(1.0f));
assertTrue(hash.contains(2.0f));
assertFalse(hash.contains(3.0f));
}

@Test
public void testForEach() {
hash = new TFloatCharHash<>(5);
hash.insert(1.0f, 'A');
hash.insert(2.0f, 'B');
hash.insert(3.0f, 'C');
TFloatProcedure procedure = new TFloatProcedure() {
@Override
public boolean execute(Float key) {
assertEquals(key, hash.get(key));
return true;
}
};
assertTrue(hash.forEach(procedure));
}

@Test
public void testRemoveAt() {
hash = new TFloatCharHash<>(5);
hash.insert(1.0f, 'A');
hash.insert(2.0f, 'B');
hash.insert(3.0f, 'C');
hash.removeAt(1);
assertEquals(2, hash.size());
assertEquals(3.0f, hash.get(2.0f));
}

@Test(expected = IndexOutOfBoundsException.class)
public void testIndexOutOfBoundsException() {
hash = new TFloatCharHash<>(5);
hash.removeAt(5);
}

@Test
public void testIndex() {
hash = new TFloatCharHash<>(5);
hash.insert(1.0f, 'A');
hash.insert(2.0f, 'B');
hash.insert(3.0f, 'C');
assertEquals(2, hash.index(2.0f));
assertEquals(-1, hash.index(4.0f));
}

@Test
public void testInsertKey() {
hash = new TFloatCharHash<>(5);
hash.insert(1.0f, 'A');
hash.insert(2.0f, 'B');
hash.insert(3.0f, 'C');
assertFalse(hash.insert(2.0f, 'D'));
assertEquals(3, hash.size());
}

@Test
public void testWriteExternal() throws IOException {
ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
ObjectOutputStream out = new ObjectOutputStream(byteStream);
hash = new TFloatCharHash<>(5);
hash.insert(1.0f, 'A');
hash.insert(2.0f, 'B');
hash.writeExternal(out);
out.close();
byte[] bytes = byteStream.toByteArray();
ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
ObjectInputStream in = new ObjectInputStream(byteInput);
TFloatCharHash hashFromByte = (TFloatCharHash) in.readObject();
in.close();
assertEquals(2, hashFromByte.size());
assertEquals(1.0f, hashFromByte.get(1.0f));
assertEquals('A', hashFromByte.getChar(1.0f));
}

@Test
public void testReadExternal() throws IOException, ClassNotFoundException {
ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
ObjectOutputStream out = new ObjectOutputStream(byteStream);
TFloatCharHash hashToWrite = new TFloatCharHash<>(5);
hashToWrite.insert(1.0f, 'A');
hashToWrite.insert(2.0f, 'B');
hashToWrite.writeExternal(out);
out.close();
ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
ObjectInputStream in = new ObjectInputStream(byteInput);
TFloatCharHash hashFromByte = (TFloatCharHash) in.readObject();
in.close();
assertEquals(2, hashFromByte.size());
assertEquals(1.0f, hashFromByte.get(1.0f));
assertEquals('A', hashFromByte.getChar(1.0f));
}
}
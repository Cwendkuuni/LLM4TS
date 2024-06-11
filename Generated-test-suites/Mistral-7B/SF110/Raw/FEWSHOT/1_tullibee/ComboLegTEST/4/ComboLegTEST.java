package com.ib.client;

import org.junit.Assert;import static org.junit.Assert.assertEquals;import static org.junit.Assert.assertNotEquals;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertTrue;

public class ComboLegTest {

 ComboLeg comboLeg1, comboLeg2, comboLeg3;

 @Before
 public void setup() {
 comboLeg1 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);
 comboLeg2 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);
 comboLeg3 = new ComboLeg(2, 2, "SELL", "IBM", ComboLeg.CLOSE);
 }

 @Test
 public void testConstructorWithDefaultValues() {
 ComboLeg comboLeg4 = new ComboLeg();
 assertEquals(0, comboLeg4.m_conId);
 assertEquals(0, comboLeg4.m_ratio);
 assertNull(comboLeg4.m_action);
 assertNull(comboLeg4.m_exchange);
 assertEquals(ComboLeg.SAME, comboLeg4.m_openClose);
 assertNull(comboLeg4.m_designatedLocation);
 }

 @Test
 public void testConstructorWithArgs() {
 assertEquals(1, comboLeg1.m_conId);
 assertEquals(2, comboLeg1.m_ratio);
 assertEquals("BUY", comboLeg1.m_action);
 assertEquals("IBM", comboLeg1.m_exchange);
 assertEquals(ComboLeg.OPEN, comboLeg1.m_openClose);
 assertNull(comboLeg1.m_designatedLocation);
 }

 @Test
 public void testConstructorWithAllArgs() {
 assertEquals(1, comboLeg1.m_conId);
 assertEquals(2, comboLeg1.m_ratio);
 assertEquals("BUY", comboLeg1.m_action);
 assertEquals("IBM", comboLeg1.m_exchange);
 assertEquals(ComboLeg.OPEN, comboLeg1.m_openClose);
 assertEquals(0, comboLeg1.m_shortSaleSlot);
 assertNull(comboLeg1.m_designatedLocation);
 }

 @Test
 public void testEqualsMethod() {
 assertTrue(comboLeg1.equals(comboLeg2));
 assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullObject() {
 assertFalse(comboLeg1.equals(null));
 }

 @Test
 public void testHashCodeMethod() {
 int hashCode1 = comboLeg1.hashCode();
 ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);
 int hashCode2 = comboLeg5.hashCode();
 Assert.assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testToStringMethod() {
 String toString1 = comboLeg1.toString();
 String toString2 = comboLeg2.toString();
 assertEquals(toString1, toString2);
 }
}
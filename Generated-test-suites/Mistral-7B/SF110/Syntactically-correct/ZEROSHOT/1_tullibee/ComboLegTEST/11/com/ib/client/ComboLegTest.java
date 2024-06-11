package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ComboLeg;

public class ComboLegTest {

 @Test
 public void testComboLegConstructor() {
 ComboLeg comboLeg1 = new ComboLeg();
 ComboLeg comboLeg2 = new ComboLeg(1, 1, "BUY", "IBKR", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 1, "BUY", "IBKR", 1, 5, "NY");

 assertEquals(0, comboLeg1.getConId());
 assertEquals(0, comboLeg1.getRatio());
 assertNull(comboLeg1.getAction());
 assertNull(comboLeg1.getExchange());
 assertEquals(0, comboLeg1.getOpenClose());
 assertEquals(0, comboLeg1.getShortSaleSlot());
 assertNull(comboLeg1.getDesignatedLocation());

 assertEquals(1, comboLeg2.getConId());
 assertEquals(1, comboLeg2.getRatio());
 assertEquals("BUY", comboLeg2.getAction());
 assertEquals("IBKR", comboLeg2.getExchange());
 assertEquals(1, comboLeg2.getOpenClose());
 assertEquals(0, comboLeg2.getShortSaleSlot());
 assertNull(comboLeg2.getDesignatedLocation());

 assertEquals(1, comboLeg3.getConId());
 assertEquals(1, comboLeg3.getRatio());
 assertEquals("BUY", comboLeg3.getAction());
 assertEquals("IBKR", comboLeg3.getExchange());
 assertEquals(1, comboLeg3.getOpenClose());
 assertEquals(5, comboLeg3.getShortSaleSlot());
 assertEquals("NY", comboLeg3.getDesignatedLocation());
 }

 @Test
 public void testComboLegEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "IBKR", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 1, "BUY", "IBKR", 1);
 ComboLeg comboLeg3 = new ComboLeg(2, 1, "BUY", "IBKR", 1);

 assertTrue(comboLeg1.equals(comboLeg2));
 assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test
 public void testComboLegHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "IBKR", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 1, "BUY", "IBKR", 1);
 ComboLeg comboLeg3 = new ComboLeg(2, 1, "BUY", "IBKR", 1);

 int hashCode1 = comboLeg1.hashCode();
 int hashCode2 = comboLeg2.hashCode();
 int hashCode3 = comboLeg3.hashCode();

 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 }
}
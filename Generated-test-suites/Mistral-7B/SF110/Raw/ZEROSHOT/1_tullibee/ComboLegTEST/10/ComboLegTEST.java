package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit. Assert.assertTrue;

public class ComboLegTest {

 @Test
 public void testConstructorWithDefaultValues() {
 ComboLeg comboLeg = new ComboLeg();
 assertEquals(0, comboLeg.getConId());
 assertEquals(0, comboLeg.getRatio());
 assertNull(comboLeg.getAction());
 assertNull(comboLeg.getExchange());
 assertEquals(0, comboLeg.getOpenClose());
 assertEquals(0, comboLeg.getShortSaleSlot());
 assertNull(comboLeg.getDesignatedLocation());
 }

 @Test
 public void testConstructorWithAllArguments() {
 final int conId = 1;
 final int ratio = 2;
 final String action = "BUY";
 final String exchange = "SMART";
 final int openClose = ComboLeg.OPEN;
 final int shortSaleSlot = 3;
 final String designatedLocation = "NY";

 ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 assertEquals(conId, comboLeg.getConId());
 assertEquals(ratio, comboLeg.getRatio());
 assertEquals(action, comboLeg.getAction());
 assertEquals(exchange, comboLeg.getExchange());
 assertEquals(openClose, comboLeg.getOpenClose());
 assertEquals(shortSaleSlot, comboLeg.getShortSaleSlot());
 assertEquals(designatedLocation, comboLeg.getDesignatedLocation());
 }

 @Test
 public void testEqualsMethod() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NY");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NY");
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NYS");
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NY");

 assertTrue(comboLeg1.equals(comboLeg2));
 assertNotSame(comboLeg1, comboLeg3);
 assertNotSame(comboLeg1, comboLeg4);
 }
}
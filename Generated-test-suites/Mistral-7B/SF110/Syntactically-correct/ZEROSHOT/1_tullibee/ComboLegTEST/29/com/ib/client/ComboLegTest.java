package com.ib.client.test;

import static org.junit.Assert.*;

import com.ib.client.ComboLeg;

import org.junit.Test;

public class ComboLegTest {

	@Test
	public void testComboLegConstructor() {
		ComboLeg comboLeg = new ComboLeg();
		assertNull(comboLeg.m_action);
		assertNull(comboLeg.m_designatedLocation);
		assertEquals(0, comboLeg.m_conId);
		assertEquals(0, comboLeg.m_openClose);
		assertEquals(0, comboLeg.m_ratio);
		assertEquals(null, comboLeg.m_exchange);
		assertEquals(0, comboLeg.m_shortSaleSlot);

		ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3);
		assertEquals(1, comboLeg1.m_conId);
		assertEquals("Action1", comboLeg1.m_action);
		assertEquals("Exchange1", comboLeg1.m_exchange);
		assertEquals(3, comboLeg1.m_openClose);
		assertEquals(2, comboLeg1.m_ratio);
		assertEquals(0, comboLeg1.m_shortSaleSlot);
		assertNull(comboLeg1.m_designatedLocation);

		ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
		assertEquals(1, comboLeg2.m_conId);
		assertEquals("Action1", comboLeg2.m_action);
		assertEquals("Exchange1", comboLeg2.m_exchange);
		assertEquals(3, comboLeg2.m_openClose);
		assertEquals(2, comboLeg2.m_ratio);
		assertEquals(4, comboLeg2.m_shortSaleSlot);
		assertEquals("DesignatedLocation1", comboLeg2.m_designatedLocation);
	}

	@Test
	public void testComboLegEquals() {
		ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
		ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");

		assertTrue(comboLeg1.equals(comboLeg2));

		ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 5, "DesignatedLocation1");
		assertFalse(comboLeg1.equals(comboLeg3));

		ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
		assertFalse(comboLeg1.equals(comboLeg4));
	}
}
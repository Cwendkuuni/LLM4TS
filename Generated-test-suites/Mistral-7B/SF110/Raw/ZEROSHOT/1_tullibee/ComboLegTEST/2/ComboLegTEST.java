package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ComboLeg;

public class ComboLegTest {

	@Test
	public void testComboLegConstructor() {
		ComboLeg comboLeg1 = new ComboLeg();
		ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
		ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "Location1");

		assertEquals(0, comboLeg1.m_conId);
		assertEquals(0, comboLeg1.m_ratio);
		assertNull(comboLeg1.m_action);
		assertNull(comboLeg1.m_exchange);
		assertEquals(0, comboLeg1.m_openClose);
		assertEquals(0, comboLeg1.m_shortSaleSlot);
		assertNull(comboLeg1.m_designatedLocation);

		assertEquals(1, comboLeg2.m_conId);
		assertEquals(2, comboLeg2.m_ratio);
		assertEquals("Action1", comboLeg2.m_action);
		assertEquals("Exchange1", comboLeg2.m_exchange);
		assertEquals(ComboLeg.OPEN, comboLeg2.m_openClose);
		assertEquals(0, comboLeg2.m_shortSaleSlot);
		assertNull(comboLeg2.m_designatedLocation);

		assertEquals(1, comboLeg3.m_conId);
		assertEquals(2, comboLeg3.m_ratio);
		assertEquals("Action1", comboLeg3.m_action);
		assertEquals("Exchange1", comboLeg3.m_exchange);
		assertEquals(ComboLeg.OPEN, comboLeg3.m_openClose);
		assertEquals(1, comboLeg3.m_shortSaleSlot);
		assertEquals("Location1", comboLeg3.m_designatedLocation);
	}

	@Test
	public void testComboLegEquals() {
		ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
		ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
		ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN);
		ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

		assertTrue(comboLeg1.equals(comboLeg2));
		assertFalse(comboLeg1.equals(comboLeg3));
		assertFalse(comboLeg1.equals(comboLeg4));
	}

	@Test
	public void testComboLegHashCode() {
		ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
		ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
		ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN);
		ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

		assertEquals(hashCode(comboLeg1), hashCode(comboLeg2));
		assertNotSame(hashCode(comboLeg1), hashCode(comboLeg3));
		assertNotSame(hashCode(comboLeg1), hashCode(comboLeg4));
	}

	private int hashCode(ComboLeg comboLeg) {
		return 31 * (31 + comboLeg.m_conId) + comboLeg.m_ratio;
	}
}
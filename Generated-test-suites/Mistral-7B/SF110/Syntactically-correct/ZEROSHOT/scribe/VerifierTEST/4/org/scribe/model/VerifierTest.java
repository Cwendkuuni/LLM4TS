package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

	@Test
	public void testVerifierInstance() {
		String testValue = "testVerifier";
		Verifier verifier = new Verifier(testValue);
		assertNotNull(verifier);
		assertEquals(testValue, verifier.getValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullVerifier() {
		Verifier verifier = new Verifier(null);
		fail("An IllegalArgumentException should have been thrown");
	}

	@Test
	public void testGetVerifierValue() {
		String testValue = "testVerifier";
		Verifier verifier = new Verifier(testValue);
		assertEquals(testValue, verifier.getValue());
	}
}
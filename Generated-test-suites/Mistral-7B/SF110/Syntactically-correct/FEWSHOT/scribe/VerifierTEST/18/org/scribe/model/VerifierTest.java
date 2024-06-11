package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {

 @Test
 public void testVerifierConstruction() {
 Verifier verifier = new Verifier("testValue");
 assertNotNull(verifier);
 assertEquals("testValue", verifier.getValue());
 }
}
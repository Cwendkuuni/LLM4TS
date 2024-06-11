package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

 @Test
 public void testVerifierConstruction() {
 Verifier verifier = new Verifier("testValue");
 assertEquals("testValue", verifier.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testVerifierNullArgument() {
 Verifier verifier = new Verifier(null);
 fail("Expected NullPointerException, but got none.");
 }

}
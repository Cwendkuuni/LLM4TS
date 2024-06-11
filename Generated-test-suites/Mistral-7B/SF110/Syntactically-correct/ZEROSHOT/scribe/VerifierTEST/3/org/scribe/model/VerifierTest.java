package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VerifierTest {

 private Verifier verifier;

 @Test
 public void testVerifierInstance() {
 verifier = new Verifier("testVerifierValue");
 assertTrue(verifier instanceof Verifier);
 }

 @Test
 public void testVerifierNullArgument() {
 assertNull(new Verifier(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifierEmptyString() {
 assertThrows(IllegalArgumentException.class, () -> new Verifier(""));
 }

 @Test
 public void testVerifierValue() {
 assertEquals("testVerifierValue", verifier.getValue());
 }

 @Test
 public void testPreconditionsCheckNotNull() {
 String value = null;
 try {
 new Verifier(value);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPreconditionsCheckNotNullWithMessage() {
 String value = null;
 try {
 Preconditions.checkNotNull(value, "Message to be displayed on failure");
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("Message to be displayed on failure", e.getMessage());
 }
 }
}
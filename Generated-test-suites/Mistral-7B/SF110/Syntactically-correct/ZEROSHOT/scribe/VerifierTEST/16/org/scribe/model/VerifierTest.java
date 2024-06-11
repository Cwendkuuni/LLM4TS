package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class VerifierTest {

 private Verifier verifier;

 @org.junit.Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 verifier = new Verifier("test_value");
 }

 @Test
 public void testVerifierConstruction() {
 Verifier verifier = new Verifier("test_value");
 assertNotNull(verifier);
 assertEquals("test_value", verifier.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifierNullConstruction() {
 Preconditions mockPreconditions = Mockito.mock(Preconditions.class);
 Mockito.when(mockPreconditions.checkNotNull(Mockito.anyString(), Mockito.eq("Must provide a valid string as verifier"))).thenThrow(new IllegalArgumentException());
 Verifier verifier = new Verifier(null);
 }

 @Test
 public void testGetValue() {
 assertEquals("test_value", verifier.getValue());
 }

}
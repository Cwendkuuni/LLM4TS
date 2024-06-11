package com.example.junit.test.unsafeallocator;

import org.junit.Test;
import org.junit.Assert;

public class UnsafeAllocatorTest {
  private UnsafeAllocator unsafeAllocator;

  @org.junit.Before
  public void setUp() {
    unsafeAllocator = UnsafeAllocator.create();
  }

  @Test
  public void testNewInstance() {
    Assert.assertNotNull(unsafeAllocator.newInstance(String.class));
  }

  @Test
  public void testNewInstanceWithUnsupportedClass() {
    Assert.assertThrows(UnsupportedOperationException.class, () -> {
      unsafeAllocator.newInstance(Interface.class);
    });
  }

  @Test
  public void testNewInstanceWithNullClass() {
    Assert.assertThrows(NullPointerException.class, () -> {
      unsafeAllocator.newInstance(null);
    });
  }
}
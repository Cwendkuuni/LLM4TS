package com.example;

import org.junit.Test;
import org.junit.Assert;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Integer intInstance = unsafeAllocator.newInstance(Integer.class);
    Assert.assertNotNull(intInstance);
  }

  @Test
  public void testNewInstanceNullClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Assert.assertThrows(NullPointerException.class, () -> unsafeAllocator.newInstance(null));
  }

  @Test
  public void testNewInstanceUninstantiatableClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Assert.assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(String.class));
  }

  @Test
  public void testNewInstanceInterfaceClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Assert.assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(RuntimeException.class));
  }

}
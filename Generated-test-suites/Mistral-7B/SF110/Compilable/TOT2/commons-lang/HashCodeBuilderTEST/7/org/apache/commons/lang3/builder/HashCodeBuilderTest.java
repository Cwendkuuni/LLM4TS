package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

   @Test
   public void testAppendBoolean() {
      HashCodeBuilder hcb = new HashCodeBuilder();
      hcb.append(true);
      int expectedHashCode = 32 * 37 + 1; // 32 is the initial odd number, 37 is the multiplier odd number
      int actualHashCode = hcb.toHashCode();
      assertEquals(expectedHashCode, actualHashCode);
   }
}
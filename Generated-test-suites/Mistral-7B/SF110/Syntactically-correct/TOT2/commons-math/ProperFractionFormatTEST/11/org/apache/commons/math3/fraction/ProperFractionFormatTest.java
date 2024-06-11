import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ParseException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import java.text.*;
import java.util.*;

public class ProperFractionFormatTest {

  @Test public void testProperFractionFormatDefaultConstructor() {
    ProperFractionFormat pff = new ProperFractionFormat();
    assertNotNull(pff);
  }

  // Other tests for each method follow

  @Test (expected = NullArgumentException.class)
  public void testSetWholeFormatNullArgumentException() {
    ProperFractionFormat pff = new ProperFractionFormat();
    pff.setWholeFormat(null);
  }

  // Test cases for other methods follow

}

// Test cases for methods 2, 3, and 4 follow
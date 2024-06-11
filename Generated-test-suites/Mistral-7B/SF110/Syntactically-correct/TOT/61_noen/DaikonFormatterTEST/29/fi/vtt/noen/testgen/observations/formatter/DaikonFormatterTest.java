package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DaikonFormatterTest {
   private DaikonFormatter daikonFormatter;

   @Before
   public void setup() throws Exception {
      String fileName = "test.dtrace";
      daikonFormatter = new DaikonFormatter(fileName, null);
   }

   @Test
   public void testHeader() {
      String expected = "decl-version 2.0\n" +
                         "var-comparability implicit\n";
      String actual = daikonFormatter.header();
      assertEquals(expected, actual);
   }

   @Test
   public void testFooter() {
      String expected = "";
      String actual = daikonFormatter.footer();
      assertEquals(expected, actual);
   }
}
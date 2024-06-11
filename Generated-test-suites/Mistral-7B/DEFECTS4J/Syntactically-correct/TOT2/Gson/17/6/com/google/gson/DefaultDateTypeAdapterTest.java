import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite() throws IOException {
    DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    DateFormat enUsFormat = mock(DateFormat.class);
    when(enUsFormat.format(any(Date.class))).thenReturn("2022-12-31");

    JsonWriter jsonWriter = mock(JsonWriter.class);
    when(jsonWriter.value(anyString())).thenAnswer(invocation -> {});

    defaultDateTypeAdapter.write(jsonWriter, new Date(0));
    assertEquals("2022-12-31", jsonWriter.toString());
  }

}
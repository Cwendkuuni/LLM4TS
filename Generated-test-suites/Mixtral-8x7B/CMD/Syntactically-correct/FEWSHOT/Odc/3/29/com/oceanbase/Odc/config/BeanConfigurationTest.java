import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

@Before
public void setUp() {
SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
when(sensitivePropertyHandler.decrypt("encryptedValue"))
.thenReturn("decryptedValue");
ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
.registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()));
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
String encodedPassword = passwordEncoder.encode("password");
assertNotNull(encodedPassword);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = new ObjectMapper(null, new CustomOutputSerializer());
assertNotNull(objectMapper);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
assertNotNull(defaultHttpFirewall);
}

@Test
public void testNullCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = new DefaultCookieSerializer();
cookieSerializer.setCookieName("JSESSIONID");
assertNotNull(cookieSerializer);
}
}
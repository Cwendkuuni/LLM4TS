import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
String encodedPassword = passwordEncoder.encode("test");
assertNotNull(encodedPassword);
}

@Test
public void testObjectMapper() throws Exception {
ObjectMapper objectMapper = new ObjectMapper(JacksonFactory.unsafeJsonMapper());
SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
when(sensitivePropertyHandler.decrypt("encrypted")).thenReturn("decrypted");

CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
.addSerializer(Internationalizable.class, new I18nOutputSerializer());

objectMapper = objectMapper
.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
.registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

Internationalizable internationalizable = new Internationalizable() {
@Override
public String getLocale() {
return "en_US";
}

@Override
public String getDefaultMessage() {
return "default_message";
}
};

String json = objectMapper.writeValueAsString(internationalizable);
assertNotNull(json);
assertEquals("{\"locale\":\"en_US\",\"defaultMessage\":\"default_message\"}", json);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
assertNotNull(defaultHttpFirewall);
}

@Test
public void testNullCloudMetadataClient() throws UnsupportedException {
CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
}

@Test
public void testCookieSerializer() {
DefaultCookieSerializer serializer = new DefaultCookieSerializer();
serializer.setCookieName("JSESSIONID");
assertNotNull(serializer);
}

}
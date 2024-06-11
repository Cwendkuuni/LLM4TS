import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

@Mock
private SensitivePropertyHandler sensitivePropertyHandler;

@InjectMocks
private BeanConfiguration beanConfiguration;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils
.getField(objectMapper, "customOutputSerializer");
assertEquals(1, customOutputSerializer.getSerializers().size());
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
assertEquals(true, defaultHttpFirewall instanceof DefaultHttpFirewall);
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
assertEquals(true, cloudMetadataClient instanceof NullCloudMetadataClient);
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

}
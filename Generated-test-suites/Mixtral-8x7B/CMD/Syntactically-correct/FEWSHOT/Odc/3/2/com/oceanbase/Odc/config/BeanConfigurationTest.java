import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

@InjectMocks
private BeanConfiguration beanConfiguration;

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());
// Add any assertions you want to test the objectMapper
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
assertFalse(cloudMetadataClient instanceof NullCloudMetadataClient);
NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
assertFalse(nullCloudMetadataClient.supportsTenantInstance());
assertFalse(nullCloudMetadataClient.needsOBTenantName());
assertFalse(nullCloudMetadataClient.needsSysTenantUser());
assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

}
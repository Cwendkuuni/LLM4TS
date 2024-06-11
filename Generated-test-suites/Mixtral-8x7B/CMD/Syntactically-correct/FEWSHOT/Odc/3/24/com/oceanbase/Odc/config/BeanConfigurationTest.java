import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanConfigurationTest {

private BeanConfiguration beanConfiguration;
private SensitivePropertyHandler sensitivePropertyHandler;
private CloudMetadataClient cloudMetadataClient;

@Before
public void setUp() {
sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
cloudMetadataClient = mock(CloudMetadataClient.class);
beanConfiguration = new BeanConfiguration();
ReflectionTestUtils.setField(beanConfiguration, "sensitivePropertyHandler", sensitivePropertyHandler);
ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", cloudMetadataClient);
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
assertNotNull(objectMapper);
CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils
.getField(objectMapper, "customOutputSerializer");
assertNotNull(customOutputSerializer);
List<Class<? extends Internationalizable>> serializerClasses = customOutputSerializer.getSerializerClasses();
assertTrue(serializerClasses.contains(I18nOutputSerializer.class));
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
assertTrue(defaultHttpFirewall.getAllowedUrlEncodedSlash());
}

@Test
@SuppressWarnings("unchecked")
public void testCloudMetadataClient() {
BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
verify(cloudMetadataClient, never()).supportsCloudMetadata();
assertSame(nullCloudMetadataClient, beanConfiguration.cloudMetadataClient());
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

@Configuration
static class TestBeanConfiguration {

@Bean
@Primary
public SensitivePropertyHandler sensitivePropertyHandler() {
return mock(SensitivePropertyHandler.class);
}

@Bean
public CloudMetadataClient cloudMetadataClient() {
return mock(CloudMetadataClient.class);
}
}

}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

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

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

	@Mock
	private SensitivePropertyHandler sensitivePropertyHandler;

	@Mock
	private CloudMetadataClient cloudMetadataClient;

	@InjectMocks
	private BeanConfiguration beanConfiguration;

	private ObjectMapper objectMapper;

	private PasswordEncoder passwordEncoder;

	private CookieSerializer cookieSerializer;

	@Before
	public void setup() {
		objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
		passwordEncoder = beanConfiguration.passwordEncoder();
		cookieSerializer = beanConfiguration.cookieSerializer();
	}

	@Test
	public void testPasswordEncoder() {
		assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
	}

	@Test
	public void testObjectMapper() {
		// You can add assertions here to test the ObjectMapper configuration
	}

	@Test
	public void testHttpFirewall() {
		// You can add assertions here to test the HttpFirewall configuration
	}

	@Test
	public void testNullCloudMetadataClient() {
		// You can add assertions here to test the NullCloudMetadataClient configuration
	}

	@Test
	public void testCookieSerializer() {
		assertEquals("JSESSIONID", cookieSerializer.getCookieName());
	}

	@Test
	public void testSensitivePropertyHandler() {
		// You can add assertions here to test the SensitivePropertyHandler configuration
	}

}
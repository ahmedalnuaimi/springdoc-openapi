/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.ui;

import org.springdoc.core.Constants;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webflux.core.SpringDocWebFluxConfiguration;
import org.springdoc.webflux.ui.SwaggerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@WebFluxTest
@ContextConfiguration(classes = { SpringDocConfiguration.class, SpringDocConfigProperties.class, SpringDocWebFluxConfiguration.class, SwaggerUiConfigParameters.class, SwaggerUiConfigProperties.class, SwaggerConfig.class, SwaggerUiOAuthProperties.class })
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

	@Autowired
	protected WebTestClient webTestClient;

	private static final String DEFAULT_SWAGGER_INITIALIZER_URL= Constants.DEFAULT_WEB_JARS_PREFIX_URL  + Constants.SWAGGER_INITIALIZER_URL;

	protected void checkJS(String fileName, String uri) {
		EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(uri)
				.exchange()
				.expectStatus().isOk()
				.expectBody().returnResult();
		checkJSResult(fileName, new String(getResult.getResponseBody()));
	}

	protected void checkJSResult(String fileName, String result) {
		assertTrue(result.contains("window.ui"));
		String expected = getContent("results/" + fileName);
		assertEquals(expected, result.replace("\r", ""));
	}

	protected void checkJS(String fileName) {
		checkJS(fileName, DEFAULT_SWAGGER_INITIALIZER_URL);
	}
}

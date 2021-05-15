package in.stack.movie;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class MovieDbSwaggerDocsConfig {

	@Bean
	public Docket movieDbApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Stackroute SDET").apiInfo(apiInfo()).select()
				.paths(regex("/api.*")).build();
	}

	private ApiInfo apiInfo() {

		return new ApiInfoBuilder().title("MovieDB-RestAPI").description("MovieDB Rest App")
				.termsOfServiceUrl("All Rights Reserved").license("SDET Group Licence").version("1.0").build();

	}

}

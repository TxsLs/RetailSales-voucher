//package org.retailsales.voucher.config;
//
//
//
//import io.swagger.v3.oas.models.ExternalDocumentation;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////import springfox.documentation.builders.ApiInfoBuilder;
////import springfox.documentation.builders.PathSelectors;
////import springfox.documentation.builders.RequestHandlerSelectors;
////import springfox.documentation.service.ApiInfo;
////import springfox.documentation.spi.DocumentationType;
////import springfox.documentation.spring.web.plugins.Docket;
////import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * <b>Swagger2Config。</b>
// * <p><b>详细说明：</b></p>
// * <!-- 在此添加详细说明 -->
// * 无。
// * @version 1.0
// * @author mex2000
// * @since 1.0
// */
//@Configuration
////@EnableSwagger2
//public class Swagger2Config {
//	@Bean
//
////	public Docket api() {
////		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
////				.apis(RequestHandlerSelectors.basePackage("org.retailsales.voucher.controller")).paths(PathSelectors.any())
////				.build();
////	}
////
////	private ApiInfo apiInfo() {
////		return new ApiInfoBuilder().title("springboot系统接口文档").description("springboot系统接口文档 by Txs").version("1.0")
////				.build();
////	}
//
//	public OpenAPI swaggerOpenApi() {
//		return new OpenAPI()
//				.info(new Info().title("XXX平台YYY微服务")
//						.description("描述平台多牛逼")
//						.version("v1.0.0"))
//				.externalDocs(new ExternalDocumentation()
//						.description("设计文档")
//						//.url("https://juejin.cn/user/254742430749736/posts")
//				);
//	}
//
//}

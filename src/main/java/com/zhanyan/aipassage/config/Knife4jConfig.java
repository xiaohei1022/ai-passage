package com.zhanyan.aipassage.config;

import com.zhanyan.aipassage.exception.StatusCode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class Knife4jConfig {

    private final static Logger logger = LoggerFactory.getLogger(Knife4jConfig.class);

    private static final String SERVICE_URL = "http://127.0.0.1:8567/api/doc.html";
    private static final String API_INFO_TITLE = "AI文章管理系统接口文档";
    private static final String API_INFO_VERSION = "V1.0";
    private static final String API_INFO_DESCRIPTION = "提供文章管理、用户认证等RESTful API接口";
    private static final String API_INFO_LICENSE = "2026年度内部文档,违拷必究.";

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("01-sys-api")
                .displayName("01-系统接口")
                .pathsToMatch("/**")
                // Scan the application's base package so controllers (e.g. com.zhanyan.aipassage.controller)
                // are discovered. Previously this scanned `com.zhanyan.aipassage.sys` which missed controllers.
                .packagesToScan("com.zhanyan.aipassage.controller")
                .addOpenApiCustomizer(this::setCustomStatusCode)
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        // Create a Base OpenAPI instance and register a simple BaseResponse schema so Swagger Models shows it
        Schema<?> baseResponseSchema = new Schema<>();
        baseResponseSchema.setType("object");
        baseResponseSchema.addProperties("code", new Schema<>().type("integer").format("int32"));
        baseResponseSchema.addProperties("data", new Schema<>().description("payload - may be any type").type("object"));
        baseResponseSchema.addProperties("message", new Schema<>().type("string"));

        Components components = new Components()
                .addSchemas("BaseResponse", baseResponseSchema)
                .addSecuritySchemes("Authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("登录Token")
                );

        OpenAPI openApi = new OpenAPI()
                .info(new Info()
                        .title(API_INFO_TITLE)
                        .description(API_INFO_DESCRIPTION)
                        .version(API_INFO_VERSION)
                        .contact(new Contact().name("Zhanyan").email("xiaohei_1022@163.com"))
                        .license(new License().name(API_INFO_LICENSE).url(SERVICE_URL))
                )
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(components);

        // Apply global response customization so the added schemas are referenced in responses
        addGlobalResponse(openApi);

        return openApi;
    }


    private void addGlobalResponse(OpenAPI openApi) {
        if (openApi.getPaths() != null) {
            Paths paths = openApi.getPaths();
            for (PathItem pathItem : paths.values()) {
                addResponseToOperation(pathItem.getGet());
                addResponseToOperation(pathItem.getPost());
                addResponseToOperation(pathItem.getPut());
                addResponseToOperation(pathItem.getDelete());
                addResponseToOperation(pathItem.getPatch());
            }
        }
    }

    private void addResponseToOperation(Operation operation) {
        if (operation == null) {
            return;
        }
        ApiResponses responses = operation.getResponses();
        if (responses == null) {
            responses = new ApiResponses();
            operation.setResponses(responses);
        }
        responses.addApiResponse("1000", new ApiResponse()
                .description("请求成功")
                .content(new Content().addMediaType("*/*", new MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/BaseResponse")))));
        responses.addApiResponse("1001", new ApiResponse()
                .description("非法字段")
                .content(new Content().addMediaType("*/*", new MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/BaseResponse")))));
        responses.addApiResponse("1005", new ApiResponse()
                .description("未授权")
                .content(new Content().addMediaType("*/*", new MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/BaseResponse")))));
    }

    private void setCustomStatusCode(OpenAPI openApi) {
        if (openApi.getPaths() != null) {
            Paths paths = openApi.getPaths();
            for (Map.Entry<String, PathItem> entry : paths.entrySet()) {
                PathItem value = entry.getValue();
                Operation put = value.getPut();
                Operation get = value.getGet();
                Operation delete = value.getDelete();
                Operation post = value.getPost();
                if (put != null) {
                    put.setResponses(handleResponses(put.getResponses()));
                }
                if (get != null) {
                    get.setResponses(handleResponses(get.getResponses()));
                }
                if (delete != null) {
                    delete.setResponses(handleResponses(delete.getResponses()));
                }
                if (post != null) {
                    post.setResponses(handleResponses(post.getResponses()));
                }
            }
        }
    }

    private ApiResponses handleResponses(ApiResponses responses) {
        // Ensure responses is not null
        if (responses == null) {
            responses = new ApiResponses();
        }

        Content content = new Content();
        for (Map.Entry<String, ApiResponse> entry : responses.entrySet()) {
            ApiResponse apiResponse = entry.getValue();
            if (apiResponse != null) {
                content = apiResponse.getContent();
                break;
            }
        }

        Map<Integer, String> map = StatusCode.toMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            ApiResponse api = new ApiResponse();
            api.setContent(content);
            api.description(entry.getValue());
            responses.addApiResponse(entry.getKey() + "", api);
        }
        return responses;
    }
}

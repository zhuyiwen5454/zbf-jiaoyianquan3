package gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 作者: LCG
 * 日期: 2019/7/24 20:07
 * 描述: 解决跨域的问题
 */
@Configuration
public class CsrfConfiguration {

    /**
     * 表示允许跨域的源
     */
    @Value("${csrf.host}")
    private String ALLOWED_ORIGIN;

    /**
     * 这里为支持的请求头，如果有自定义的header字段请自己添加（不知道为什么不能使用*）
     */
    private static final String ALLOWED_HEADERS = "Depth,X-File-Size,X-Requested-With,X-Requested-By,If-Modified-Since,X-File-Name,X-File-Type,Cache-Control,Origin,User-Agent,x-requested-with,Content-Type,Authorization,credential,token,newToken";

    /**
     * 允许访问的请求的格式
     */
    private static final String ALLOWED_METHODS = "*";

    private static final String ALLOWED_Expose = "*";

    /**
     * 一次预检请求后，最多多久可以不发出预检请求
     */
    private static final String MAX_AGE = "18000L";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx,WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {

                ServerHttpResponse response = ctx.getResponse();

                HttpHeaders headers = response.getHeaders();
                //* 对于没有凭据的请求，服务器可以指定“*”作为通配符，从而允许任何来源访问资源。
                // <origin> 指定可以访问资源的 URI
                headers.add("Access-Control-Allow-Origin","*");
                //设置允许的请求方式
                headers.add("Access-Control-Allow-Methods",ALLOWED_METHODS);
                //浏览器的同源策略，就是出于安全考虑，浏览器会限制从脚本发起的跨域HTTP请求
                // （比如异步请求GET,POST,PUT,DELETE,OPTIONS等等），所以浏览器会向所请
                // 求的服务器发起两次请求，第一次是浏览器使用OPTIONS方法发起一个预检请求，
                // 第二次才是真正的异步请求，第一次的预检请求获知服务器是否允许该跨域请求：
                // 如果允许，才发起第二次真实的请求；如果不允许，则拦截第二次请求。
                //Access-Control-Max-Age用来指定本次预检请求的有效期，单位为秒，，在此期间不用发出另一条预检请求。
                headers.add("Access-Control-Max-Age",MAX_AGE);
                //表示允许请求头中允许出现的参数项，
                // 但是application/x-www-form-urlencoded，multipart/form-data或text/plain等这些参数是忽略项
                //也就是说这些参数是总是可用的
                headers.add("Access-Control-Allow-Headers",ALLOWED_HEADERS);
                //响应报头指示哪些报头可以公开为通过列出他们的名字的响应的一部分。
                headers.add("Access-Control-Expose-Headers",ALLOWED_Expose);
                //响应报头指示的请求的响应是否可以暴露于该页面。当true值返回时它可以被暴露。凭证是 Cookie ，授权标头或 TLS 客户端证书。
                headers.add("Access-Control-Allow-Credentials","true");
                //headers.add("token","8");

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }

            }

            //ctx.mutate().response();
            return chain.filter(ctx);
        };
    }

}

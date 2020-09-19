package gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.common.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/7  21:43
 * 描述:
 */
@Component
public class AuthorizeGloableFilter implements GlobalFilter,Ordered {

    /**
     * 无需登录认证的路径  这是Map结构的数据的获取方式
     */
    @Value("#{${spring.noNeedLoginPath}}")
    private Map<String,String> noNeedLoginPath;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 判断登录以及Token是否过期、重新生成Token的逻辑
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //判断当前请求的路径是不是需要登录，如果不需要则会直接通过返回一个Mono 否则的话返回一个null
        Mono<Void> needToLogin = this.isNeedToLogin(exchange, chain);
        if(needToLogin!=null){
            return needToLogin;
        }
        //获取请求头中的Token串
        List<String> tokenList = exchange.getRequest().getHeaders().get("token");
        //检测是否有Token信息
        Mono<Void> hasToken = this.hasToken(tokenList,exchange);
        if(hasToken!=null){
            return  hasToken;
        }
        //校验Token是不是过期，如果没有过期的话进行Toekn的延期，并进行授权否则返回重新登录
        Mono<Void> authorized = authorized(tokenList.get(0), exchange, chain);

        return authorized;

    }


    /**
     * 过滤器的执行顺序
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


    /**
     * 未授权登录也就是没有Token或者是token过期的情况下
     * @param exchange
     * @param errorData  给定的错误信息
     * @return
     */
    public Mono<Void> responseUNAUTHORIZED(ServerWebExchange exchange,String errorData){
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        byte[] bits = errorData.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(httpStatus == null ? HttpStatus.OK : httpStatus);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


    /**
     * 校验请求中是否携带Token参数,如果没有的话返回失败信息
     * @param exchange
     * @return
     */
    private Mono<Void> hasToken(List<String> tokenList,ServerWebExchange exchange){
        if(tokenList.size()==0||tokenList.get(0).equals("")||tokenList.get(0).equals("null")){
            //校验有没有Token参数
            ResponseResult responseResult = ResponseResult.getResponseResult();
            responseResult.setMsg(AllStatusEnum.NO_LOGIN.getMsg());
            responseResult.setCode(AllStatusEnum.NO_LOGIN.getCode());

            return this.responseUNAUTHORIZED(exchange, JSON.toJSONString(responseResult));
        }else{
            return null;
        }

    }


    /**
     * 判断当前请求的路径是不是需要登录操作
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> isNeedToLogin(ServerWebExchange exchange, GatewayFilterChain chain){

        //获取当前请求的URL信息
        String currentAccessUrl = getCurrentAccessUrl(exchange);
        //判断当前请求的路径在不在访问的范围
        boolean isNoNeedLogin = noNeedLoginPath.containsKey(currentAccessUrl);
        if(isNoNeedLogin){
            //如果当前访问的路径不在权限控制的范围，则直接通过过滤
            return chain.filter(exchange);
        }else{
            return null;
        }
    }

    /**
     * 作者: LCG
     * 日期: 2020/9/8  9:51
     * 参数：
     * 返回值：
     * 描述: 校验token是不是过期，没有过期的话进行Token的延期，并进行授权
     */
    public Mono<Void> authorized(String token,ServerWebExchange exchange,GatewayFilterChain chain){
        try{
            //解析Token，进行授权，刷新token并通过响应头回写newToken
            Mono<Void> voidMono = praseTokenCheckAuth(token, exchange, chain);
            return voidMono;

        }catch (ExpiredJwtException e){
            //校验有没有Token参数或者是Token参数过期
            ResponseResult responseResult = ResponseResult.getResponseResult();
            responseResult.setMsg(AllStatusEnum.TOKEN_HAS_EXPIRE.getMsg());
            responseResult.setCode(AllStatusEnum.TOKEN_HAS_EXPIRE.getCode());
            return this.responseUNAUTHORIZED(exchange, JSON.toJSONString(responseResult));
        }
    }

    /**
     * 作者: LCG
     * 日期: 2020/9/8  10:01
     * 参数：
     * 返回值：
     * 描述: 获取当前请求的URL的信息
     */
    public String getCurrentAccessUrl(ServerWebExchange exchange){
        //获取请求的路径
        ServerHttpRequest request = exchange.getRequest();
        //获取当前的请求路径
        String routPath = request.getPath().pathWithinApplication().value().split("\\?")[0];
        return routPath;
    }

    /**
     * 作者: LCG
     * 日期: 2020/9/8  10:10
     * 参数：
     * 返回值：
     * 描述: 对Token进行刷新和延期,并在请求头中设置新的newToken
     */
    public Mono<Void> updateTokenForDelay(String userInfo,ServerWebExchange exchange,GatewayFilterChain chain){
        //重新生成token
        //如果解析过程没有报错的话进行Token的延期(其实就是重新生成一下Token信息)
        String newToken = JwtUtils.generateToken(userInfo);
        //将新的Token写入响应头带回客户端
        exchange.getResponse().getHeaders().set("newToken",newToken);
        //正常的返回客户端
        return chain.filter(exchange);
    }

    /**
     * 作者: LCG
     * 日期: 2020/9/8  10:21
     * 参数：
     * 返回值：
     * 描述: 解析Token，并校验权限进行授权
     */
    public Mono<Void> praseTokenCheckAuth(String token,ServerWebExchange exchange,GatewayFilterChain chain){
        //解析token
        //如果过期的话这个解析的过程会报错 ExpiredJwtException，正确解析的话会得到用户的登录名
        JSONObject jsonObject = JwtUtils.decodeJwtTocken(token);
        //获取用户的登录名信息
        String loginName=jsonObject.getString("info");
        //根据登录名获取用户的角色权限
        Object uinfo = redisTemplate.opsForHash().get("user-auth", loginName);
        if(uinfo==null){
            //返回重新登录
            ResponseResult responseResult = ResponseResult.getResponseResult();
            responseResult.setMsg(AllStatusEnum.NO_AUTH.getMsg());
            responseResult.setCode(AllStatusEnum.NO_AUTH.getCode());
            return this.responseUNAUTHORIZED(exchange, JSON.toJSONString(responseResult));
        }
        String userRole = uinfo.toString();
        //解析出当前用户所拥有的角色权限
        JSONObject jsonObject1 = JSON.parseObject(userRole);
        System.out.println(jsonObject1+"22222222222222222222222222222222222222");
        //获取当前请求的URL信息
        String currentAccessUrl = getCurrentAccessUrl(exchange);
        //获取当前请求的URL需要的访问权限  menuRole 在登录的使用已经加载到Redis中
        //判断当前的用户是否有 menuRole 这个角色信息
        JSONArray authorities = JSON.parseArray(jsonObject1.get("authorities").toString());

        StringBuffer stringBuffer=null;
        for (JSONObject obj:authorities.toJavaList(JSONObject.class)){
            stringBuffer = new StringBuffer();
            stringBuffer.append(obj.getString("authority")).append(":").append(currentAccessUrl);
            Boolean menuRole = redisTemplate.opsForHash().hasKey("menuRole", stringBuffer.toString());
            if(menuRole){
                //如果拥有该权限角色，更新Token,并返回新的Token
                Mono<Void> voidMono = updateTokenForDelay(loginName, exchange, chain);
                return voidMono;
            }

        }

        //如果上一步结束没有return 说明该用户不具备该角色访问权限,返回无权限的异常
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setMsg(AllStatusEnum.NO_AUTH.getMsg());
        responseResult.setCode(AllStatusEnum.NO_AUTH.getCode());
        return this.responseUNAUTHORIZED(exchange, JSON.toJSONString(responseResult));
    }
}

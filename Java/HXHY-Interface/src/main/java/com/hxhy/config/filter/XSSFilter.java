package com.hxhy.config.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hxhy.config.util.ConfigureProperties;



@Component
@WebFilter(urlPatterns = "/*",filterName = "XSSFilter")
//@PropertySource("classpath:configure.properties")
public class XSSFilter implements Filter {

	// XSS处理Map
	private static Map<String, String> xssMap = new LinkedHashMap<String, String>();
	
    private ConfigureProperties config;
	/**
	 * 初始化过滤器
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		// 含有脚本： script
		xssMap.put("[s|S][c|C][r|R][i|C][p|P][t|T]", "");
		// 含有脚本： iframe
		xssMap.put("[i|I][f|F][r|R][a|A][m|M][e|E]", "");
		// // 含有脚本： update
		// xssMap.put("[u|U][p|P][d|D][a|A][t|T][e|E] ", "");
		// 含有脚本： onmouseover
		xssMap.put("[o|O][n|N][m|M][o|O][u|U][s|S][e|E][o|O][v|V][e|E][r|R]",
				"");
		// 含有脚本 javascript
		xssMap.put(
				"[\\\"\\\'][\\s]*[j|J][a|A][v|V][a|A][s|S][c|C][r|R][i|I][p|P][t|T]:(.*)[\\\"\\\']",
				"\"\"");
		// 含有函数： eval
		xssMap.put("[e|E][v|V][a|A][l|L]\\((.*)\\)", "");
		// 含有字符： undefined
		xssMap.put("undefined", "");
		// 含有符号 <
		xssMap.put("<", "&lt;");
		// 含有符号 >
		xssMap.put(">", "&gt;");
		// 含有符号 (
		xssMap.put("\\(", "(");
		// 含有符号 )
		xssMap.put("\\)", ")");
		// 含有符号 '
		xssMap.put("'", "'");
		// 含有符号 "
		xssMap.put("\"", "\"");

	}

	/**
	 * 进行过滤操作
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		BeanFactory factory = WebApplicationContextUtils
				.getRequiredWebApplicationContext(request.getServletContext());
		config = factory.getBean(ConfigureProperties.class);
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", config.getDomainAccesable());// 可以跨域访问
//		response.setHeader("Access-Control-Allow-Headers",
//				"AuthKey,Content-type");
		response.setHeader("Access-Control-Allow-Credentials","true");
		// 构造HttpRequestWrapper对象处理XSS
		HttpRequestWrapper httpReqWarp = new HttpRequestWrapper(request, xssMap);
		chain.doFilter(httpReqWarp, response);
	}

	public void destroy() {

	}
}

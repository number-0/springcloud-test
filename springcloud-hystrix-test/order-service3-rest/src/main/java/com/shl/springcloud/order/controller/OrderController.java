package com.shl.springcloud.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shl.springcloud.order.entity.Product;
import com.shl.springcloud.order.feign.ProductFeignClient;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @DefaultProperties : 指定此接口中公共的熔断设置
 *      如果过在@DefaultProperties指定了公共的降级方法
 *      在@HystrixCommand不需要单独指定了
 */
//@DefaultProperties(defaultFallback = "defaultFallBack")
@RestController
@RequestMapping("/order")
public class OrderController {

	//注入restTemplate对象
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 注入DiscoveryClient :
	 *  springcloud提供的获取原数组的工具类
	 *      调用方法获取服务的元数据信息
	 *
	 */
	@Autowired
	private DiscoveryClient discoveryClient;


	@Resource
	private ProductFeignClient productFeignClient;

	/**
	 * 使用注解配置熔断保护
	 *     fallbackmethod : 配置熔断之后的降级方法
	 */
	@HystrixCommand(fallbackMethod = "orderFallBack")
	@RequestMapping(value = "/buy/{id}",method = RequestMethod.GET)
	public Product findById(@PathVariable Long id) {
		if(id != 1) {
			throw  new  RuntimeException("服务器异常");
		}
		return restTemplate.getForObject("http://service-product/product/1",Product.class);
	}


	/**
	 * 指定统一的降级方法
	 *  * 参数 : 没有参数
	 */
	public Product defaultFallBack() {
		Product product = new Product();
		product.setProductName("触发统一的降级方法");
		return product;
	}

	/**
	 * 降级方法
	 *  和需要收到保护的方法的返回值一致
	 *  方法参数一致
	 */
	public Product orderFallBack(Long id) {
		Product product = new Product();
		product.setProductName("触发降级方法");
		return product;
	}


	/**
	 * 基于feign的形式调用远程微服务
	 */
//	@RequestMapping(value = "/buy/{id}",method = RequestMethod.GET)
//	public Product findById(@PathVariable Long id) {
//		Product product = null;
//		product = productFeignClient.findById(id);
//		return product;
//	}

	/**
	 * 基于ribbon的形式调用远程微服务
	 *  1.使用@LoadBalanced声明RestTemplate
	 *  2.使用服务名称替换ip地址
	 */
//	@RequestMapping(value = "/buy/{id}",method = RequestMethod.GET)
//	public Product findById(@PathVariable Long id) {
//		Product product = null;
//		product = restTemplate.getForObject("http://service-product/product/1",Product.class);
//		return product;
//	}

	/**
	 * 参数:商品id
	 *  通过订单系统,调用商品服务根据id查询商品信息
	 *      1.需要配置商品对象
	 *      2.需要调用商品服务
	 *  使用java中的urlconnection,httpclient,okhttp
	 */
//	@RequestMapping(value = "/buy/{id}",method = RequestMethod.GET)
//	public Product findById(@PathVariable Long id) {
//		//调用discoveryClient方法
//		//已调用服务名称获取所有的元数据
//		List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
//		//获取唯一的一个元数据
//		ServiceInstance instance = instances.get(0);
//		//根据元数据中的主机地址和端口号拼接请求微服务的URL
//		Product product = null;
//		//如何调用商品服务?
//		product = restTemplate.getForObject("http://"+instance.getHost()+":"+instance.getPort()+"/product/1",Product.class);
//		return product;
//	}


}

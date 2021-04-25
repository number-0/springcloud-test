package com.shl.springcloud.product.controller;

import com.shl.springcloud.product.entity.Product;
import com.shl.springcloud.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Value("${server.port}")
	private String port;

	@Value("${spring.cloud.client.ip-address}") //spring cloud 自动的获取当前应用的ip地址
	private String ip;

	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public Product findById(@PathVariable Long id) {
		Product product = productService.findById(id);
		product.setProductName("访问的服务地址:"+ip + ":" + port);
		return product;
	}

	@RequestMapping(value = "",method = RequestMethod.POST)
	public String save(@RequestBody Product product) {
		productService.save(product);
		return "保存成功";
	}
}

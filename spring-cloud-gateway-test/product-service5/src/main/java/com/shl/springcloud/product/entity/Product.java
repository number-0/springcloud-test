package com.shl.springcloud.product.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * 商品实体类
 */
@Data
@Entity
@Table(name="tb_product")
public class Product {

	@Id
	private Long id;
	private String productName;
	private Integer status;
	private BigDecimal price;
	private String productDesc;
	private String caption;
	private Integer inventory;
}

package com.cloud.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cloud.model.CurerncyConversionBean;
import com.cloud.proxy.CurrencyExchangeServiceFeignProxy;

@RestController
public class CurrencyConversionConverter {
	
	@Autowired
	CurrencyExchangeServiceFeignProxy currencyExchangeServiceFeignProxy;

	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurerncyConversionBean convertCurrency(
			@PathVariable String from, 
			@PathVariable String to, 
			@PathVariable BigDecimal quantity) {
		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurerncyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange"
				+ "/from/{from}/to/{to}", CurerncyConversionBean.class, uriVariables );
		
		System.out.println(responseEntity.getBody());
		CurerncyConversionBean response = responseEntity.getBody();

		return new CurerncyConversionBean(response.getId(),from,to,
				response.getConversionMultiple(), quantity, 
				quantity.multiply(response.getConversionMultiple()), 
				response.getPort());
		
		
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurerncyConversionBean convertCurrencyFeign(
			@PathVariable String from, 
			@PathVariable String to, 
			@PathVariable BigDecimal quantity) {
		
		
		CurerncyConversionBean response = currencyExchangeServiceFeignProxy.retrieveExchangeValue(from, to);

		return new CurerncyConversionBean(response.getId(),from,to,
				response.getConversionMultiple(), quantity, 
				quantity.multiply(response.getConversionMultiple()), 
				response.getPort());
		
		
	}
	
	
}

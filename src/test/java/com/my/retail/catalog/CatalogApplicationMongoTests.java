package com.my.retail.catalog;

import com.my.retail.catalog.constants.Constants;
import com.my.retail.catalog.dto.response.product.PriceDTO;
import com.my.retail.catalog.dto.response.product.ProductDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Constants.TEST)
public class CatalogApplicationMongoTests extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void CreateUpdateGetAndDeleteProduct() throws Exception {

		String uri = "/products";
		ProductDTO product = new ProductDTO();
		product.setId(13860428);
		product.setName("The Big Lebowski (Blu-ray) (Widescreen)");
		PriceDTO price = new PriceDTO();
		price.setValue(13.49);
		price.setCurrency_code("USD");
		product.setCurrent_price(price);
		String inputJson = super.mapToJson(product);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String content = mvcResult.getResponse().getContentAsString();
		ProductDTO[] productlist = super.mapFromJson(content, ProductDTO[].class);
		assertTrue(productlist.length > 0);

		uri = "/products/13860428";

		price.setValue(15.99);
		price.setCurrency_code("USD");
		product.setCurrent_price(price);
		inputJson = super.mapToJson(product);
		mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();

		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
		status = mvcResult.getResponse().getStatus();


		mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
}
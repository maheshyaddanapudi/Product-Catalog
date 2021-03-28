package com.my.retail.catalog.service;

import com.my.retail.catalog.db.entities.Price;
import com.my.retail.catalog.db.entities.Product;
import com.my.retail.catalog.db.repositories.PriceRepository;
import com.my.retail.catalog.db.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    public List<Product> findAllProducts(){
        return this.productRepository.findAll();
    }
    public Product findProductById(long id){
        return this.productRepository.findProductById(id).orElseThrow(() -> new IllegalStateException("The product doesn't exist !!!"));
    }

    public boolean createProduct(Product aProduct){
        aProduct = this.productRepository.save(aProduct);
        if(null!=aProduct && aProduct.getId()>0)
            return true;
        else
            return false;
    }

    public boolean updateProduct(Product updatedProduct, Product productFromDB){

        updatedProduct.setProduct_id(productFromDB.getProduct_id());
        updatedProduct.setInsertTimestamp(productFromDB.getInsertTimestamp());
        updatedProduct.setUpdateTimestamp(new Date());
        updatedProduct.getCurrent_price().setId(productFromDB.getCurrent_price().getId());
        updatedProduct.getCurrent_price().setInsertTimestamp(productFromDB.getInsertTimestamp());
        updatedProduct.getCurrent_price().setUpdateTimestamp(new Date());

        this.priceRepository.save(updatedProduct.getCurrent_price());

        updatedProduct = this.productRepository.save(updatedProduct);
        if(null!=updatedProduct && updatedProduct.getId()>0)
            return true;
        else
            return false;
    }

    public void deleteProduct(Product productFromDB){

        Price priceToDelete = productFromDB.getCurrent_price();

        this.productRepository.delete(productFromDB);

        this.priceRepository.delete(priceToDelete);

    }
}

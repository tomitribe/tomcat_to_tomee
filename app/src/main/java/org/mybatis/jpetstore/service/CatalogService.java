/*
 *    Copyright 2010-2013 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.mybatis.jpetstore.service;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.persistence.CategoryMapper;
import org.mybatis.jpetstore.persistence.ItemMapper;
import org.mybatis.jpetstore.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduardo Macarron
 */
@Service
public class CatalogService {

    /**
     * CDI STEP 5 - Inject just about anything just about anywhere.
     *
     * We are only a bit restrained here due to Spring being in charge of the CDI scope
     * but the goal should really be to break out of that.
     */
    @Inject
    public com.tomitribe.ee.cdi.CdiPojo cdiPojo;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Inject
    private CategoryMapper categoryMapperCdi;

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    public Category getCategory(final String categoryId) {
        return categoryMapper.getCategory(categoryId);
    }

    public Product getProduct(final String productId) {
        return productMapper.getProduct(productId);
    }

    public List<Product> getProductListByCategory(final String categoryId) {
        return productMapper.getProductListByCategory(categoryId);
    }

    public List<Product> searchProductList(String keywords) {

        /**
         * CDI STEP 6 - Get creative with your pojo, and look no hands! It's just here by magic.
         */
        keywords = cdiPojo.fixMePlease(keywords);

        final List<Product> products = new ArrayList<Product>();
        for (final String keyword : keywords.split("\\s+")) {
            products.addAll(productMapper.searchProductList("%" + keyword.toLowerCase() + "%"));
        }
        return products;
    }

    public List<Item> getItemListByProduct(final String productId) {
        return itemMapper.getItemListByProduct(productId);
    }

    public Item getItem(final String itemId) {
        return itemMapper.getItem(itemId);
    }

    public boolean isItemInStock(final String itemId) {
        return itemMapper.getInventoryQuantity(itemId) > 0;
    }
}
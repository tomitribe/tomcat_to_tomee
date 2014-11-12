/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.jpetstore.web.actions;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;

import java.util.List;

/**
 * @author Eduardo Macarron
 */
@SessionScope
public class CatalogActionBean extends AbstractActionBean {

    private static final long serialVersionUID = 5849523372175050635L;

    private static final String MAIN = "/WEB-INF/jsp/catalog/Main.jsp";
    private static final String VIEW_CATEGORY = "/WEB-INF/jsp/catalog/Category.jsp";
    private static final String VIEW_PRODUCT = "/WEB-INF/jsp/catalog/Product.jsp";
    private static final String VIEW_ITEM = "/WEB-INF/jsp/catalog/Item.jsp";
    private static final String SEARCH_PRODUCTS = "/WEB-INF/jsp/catalog/SearchProducts.jsp";

    @SpringBean
    private transient CatalogService catalogService;

    private String keyword;

    private String categoryId;
    private Category category;
    private List<Category> categoryList;

    private String productId;
    private Product product;
    private List<Product> productList;

    private String itemId;
    private Item item;
    private List<Item> itemList;

    private transient net.sourceforge.stripes.action.ActionBeanContext context;

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(final ActionBeanContext context) {
        this.context = context;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(final String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(final List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(final List<Product> productList) {
        this.productList = productList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(final List<Item> itemList) {
        this.itemList = itemList;
    }

    @DefaultHandler
    public ForwardResolution viewMain() {
        return new ForwardResolution(MAIN);
    }

    public ForwardResolution viewCategory() {
        if (categoryId != null) {
            productList = catalogService.getProductListByCategory(categoryId);
            category = catalogService.getCategory(categoryId);
        }
        return new ForwardResolution(VIEW_CATEGORY);
    }

    public ForwardResolution viewProduct() {
        if (productId != null) {
            itemList = catalogService.getItemListByProduct(productId);
            product = catalogService.getProduct(productId);
        }
        return new ForwardResolution(VIEW_PRODUCT);
    }

    public ForwardResolution viewItem() {
        item = catalogService.getItem(itemId);
        product = item.getProduct();
        return new ForwardResolution(VIEW_ITEM);
    }

    public ForwardResolution searchProducts() {
        if (keyword == null || keyword.length() < 1) {
            setMessage("Please enter a keyword to search for, then press the search button.");
            return new ForwardResolution(ERROR);
        } else {
            productList = catalogService.searchProductList(keyword.toLowerCase());
            return new ForwardResolution(SEARCH_PRODUCTS);
        }
    }

    public void clear() {
        keyword = null;

        categoryId = null;
        category = null;
        categoryList = null;

        productId = null;
        product = null;
        productList = null;

        itemId = null;
        item = null;
        itemList = null;
    }

}

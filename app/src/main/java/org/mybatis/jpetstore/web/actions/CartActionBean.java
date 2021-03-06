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

package org.mybatis.jpetstore.web.actions;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.CartItem;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.service.CatalogService;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 * @author Eduardo Macarron
 */
@SessionScope
public class CartActionBean extends AbstractActionBean {

    private static final long serialVersionUID = -4038684592582714235L;

    private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";
    private static final String CHECK_OUT = "/WEB-INF/jsp/cart/Checkout.jsp";

    @SpringBean
    private transient CatalogService catalogService;

    private Cart cart = new Cart();
    private String workingItemId;
    private transient net.sourceforge.stripes.action.ActionBeanContext context;

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(final ActionBeanContext context) {
        this.context = context;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(final Cart cart) {
        this.cart = cart;
    }

    public void setWorkingItemId(final String workingItemId) {
        this.workingItemId = workingItemId;
    }

    public Resolution addItemToCart() {
        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        } else {
            // isInStock is a "real-time" property that must be updated
            // every time an item is added to the cart, even if other
            // item details are cached.
            final boolean isInStock = catalogService.isItemInStock(workingItemId);
            final Item item = catalogService.getItem(workingItemId);
            cart.addItem(item, isInStock);
        }

        return new ForwardResolution(VIEW_CART);
    }

    public Resolution removeItemFromCart() {

        final Item item = cart.removeItemById(workingItemId);

        if (item == null) {
            setMessage("Attempted to remove null CartItem from Cart.");
            return new ForwardResolution(ERROR);
        } else {
            return new ForwardResolution(VIEW_CART);
        }
    }

    public Resolution updateCartQuantities() {
        final HttpServletRequest request = context.getRequest();

        final Iterator<CartItem> cartItems = getCart().getAllCartItems();
        while (cartItems.hasNext()) {
            final CartItem cartItem = (CartItem) cartItems.next();
            final String itemId = cartItem.getItem().getItemId();
            try {
                final int quantity = Integer.parseInt((String) request.getParameter(itemId));
                getCart().setQuantityByItemId(itemId, quantity);
                if (quantity < 1) {
                    cartItems.remove();
                }
            } catch (final Exception e) {
                //ignore parse exceptions on purpose
            }
        }

        return new ForwardResolution(VIEW_CART);
    }

    public ForwardResolution viewCart() {
        return new ForwardResolution(VIEW_CART);
    }

    public ForwardResolution checkOut() {
        return new ForwardResolution(CHECK_OUT);
    }

    public void clear() {
        cart = new Cart();
        workingItemId = null;
    }

}

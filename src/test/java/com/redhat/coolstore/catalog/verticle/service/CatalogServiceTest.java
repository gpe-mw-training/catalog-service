package com.redhat.coolstore.catalog.verticle.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.coolstore.catalog.model.Product;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

public class CatalogServiceTest extends MongoTestBase {

    private Vertx vertx;

    //@Before
    public void setup(TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.exceptionHandler(context.exceptionHandler());
        JsonObject config = getConfig();
        mongoClient = MongoClient.createNonShared(vertx, config);
        Async async = context.async();
        dropCollection(mongoClient, "products", async, context);
        async.await(10000);
    }

    //@After
    public void tearDown() throws Exception {
        mongoClient.close();
        vertx.close();
    }

    //@Test
    public void testAddProduct(TestContext context) throws Exception {
        String itemId = "999999";
        String name = "productName";
        Product product = new Product();
        product.setItemId(itemId);
        product.setName(name);
        product.setDesc("productDescription");
        product.setPrice(100.0);

        CatalogService service = new CatalogServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.addProduct(product, ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                JsonObject query = new JsonObject().put("_id", itemId);
                mongoClient.findOne("products", query, null, ar1 -> {
                    if (ar1.failed()) {
                        context.fail(ar1.cause().getMessage());
                    } else {
                        assertThat(ar1.result().getString("name"), equalTo(name));
                        async.complete();
                    }
                });
            }
        });
    }

    //@Test
    public void testGetProducts(TestContext context) throws Exception {
        // ----
        // To be implemented
        // 
        // In your test:
        // -Insert two or more products in MongoDB. Use the `MongoClient.save` method to do so.
        // - Retrieve the products from Mongo using the `testGetProducts` method.
        // - Verify that no failures happened, 
        //   that the number of products retrieved corresponds to the number inserted, 
        //   and that the product values match what was inserted.
        // 
        // ----
    }

    //@Test
    public void testGetProduct(TestContext context) throws Exception {
        // ----
        // To be implemented
        // 
        // ----
    }

    //@Test
    public void testGetNonExistingProduct(TestContext context) throws Exception {
        // ----
        // To be implemented
        // 
        // ----
    }

    //@Test
    public void testPing(TestContext context) throws Exception {
        CatalogService service = new CatalogServiceImpl(vertx, getConfig(), mongoClient);
        
        Async async = context.async();
        service.ping(ar -> {
            assertThat(ar.succeeded(), equalTo(true));
            async.complete();
        });
    }

}

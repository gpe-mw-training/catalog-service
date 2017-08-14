package com.redhat.coolstore.catalog.verticle.service;

import java.util.List;

import com.redhat.coolstore.catalog.model.Product;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public interface CatalogService {

    final static String ADDRESS = "catalog-service"; 

    static CatalogService create(Vertx vertx, JsonObject config, MongoClient client) {
        return new CatalogServiceImpl(vertx, config, client);
    }

    //----
    //
    // Add a static method that returns an instance of the client side proxy class for this service
    // Initialize the proxy with the vertx instance and the event bus address (ADDRESS)
    // Method signature:
    // static CatalogService createProxy(Vertx vertx)
    //----

    void getProducts(Handler<AsyncResult<List<Product>>> resulthandler);

    void getProduct(String itemId, Handler<AsyncResult<Product>> resulthandler);

    void addProduct(Product product, Handler<AsyncResult<String>> resulthandler);

    void ping(Handler<AsyncResult<String>> resultHandler);

}

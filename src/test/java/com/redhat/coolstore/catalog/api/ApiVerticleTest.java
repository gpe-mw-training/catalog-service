package com.redhat.coolstore.catalog.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.redhat.coolstore.catalog.model.Product;
import com.redhat.coolstore.catalog.verticle.service.CatalogService;

import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

public class ApiVerticleTest {

    private Vertx vertx;
    private Integer port;
    private CatalogService catalogService;

    /**
     * Before executing our test, let's deploy our verticle.
     * <p/>
     * This method instantiates a new Vertx and deploy the verticle. Then, it waits in the verticle has successfully
     * completed its start sequence (thanks to `context.asyncAssertSuccess`).
     *
     * @param context the test context.
     */
    //@Before
    public void setUp(TestContext context) throws IOException {
      vertx = Vertx.vertx();

      // Register the context exception handler
      vertx.exceptionHandler(context.exceptionHandler());

      // Let's configure the verticle to listen on the 'test' port (randomly picked).
      // We create deployment options and set the _configuration_ json object:
      ServerSocket socket = new ServerSocket(0);
      port = socket.getLocalPort();
      socket.close();

      DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("catalog.http.port", port));

      //Mock the catalog Service
      catalogService = mock(CatalogService.class);

      // We pass the options as the second parameter of the deployVerticle method.
      vertx.deployVerticle(new ApiVerticle(catalogService), options, context.asyncAssertSuccess());
    }

    /**
     * This method, called after our test, just cleanup everything by closing
     * the vert.x instance
     *
     * @param context
     *            the test context
     */
    //@After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    //@Test
    public void testGetProducts(TestContext context) throws Exception {
        //----
        // To be implemented
        //
        // * Stub the `getProducts()` method of `CatalogService` mock to return a `List<Product>`
        // * Use the Vert.x Web client to execute a GET request to the "/products" endpoint.
        //   Use the `getNow()` method of the HTTP client.
        // * Verify that the return code of the request equal to 200,
        //   and that the response has a header "Content-type: application/json".
        // * Use the `BodyHandler` method of the `HttpClientResponse` object to obtain and verify the response body.
        //
        //----
    }


    //@Test
    public void testGetProduct(TestContext context) throws Exception {
        //----
        // To be implemented
        //
        //----
    }

    //@Test
    public void testGetNonExistingProduct(TestContext context) throws Exception {
        //----
        // To be implemented
        //
        //----
    }

    //@Test
    public void testAddProduct(TestContext context) throws Exception {
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation){
                Handler<AsyncResult<String>> handler = invocation.getArgument(1);
                handler.handle(Future.succeededFuture(null));
                return null;
             }
         }).when(catalogService).addProduct(any(),any());

        Async async = context.async();
        String itemId = "111111";
        JsonObject json = new JsonObject()
                .put("itemId", itemId)
                .put("name", "productName")
                .put("desc", "productDescription")
                .put("price", new Double(100.0));
        String body = json.encodePrettily();
        String length = Integer.toString(body.length());
        vertx.createHttpClient().post(port, "localhost", "/product")
            .exceptionHandler(context.exceptionHandler())
            .putHeader("Content-type", "application/json")
            .putHeader("Content-length", length)
            .handler(response -> {
                assertThat(response.statusCode(), equalTo(201));
                ArgumentCaptor<Product> argument = ArgumentCaptor.forClass(Product.class);
                verify(catalogService).addProduct(argument.capture(), any());
                assertThat(argument.getValue().getItemId(), equalTo(itemId));
                async.complete();
            })
            .write(body)
            .end();
    }

}

package com.aayush.microservice.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;


public interface InventoryClient {

 /*         New way
 //@FeignClient(value = "inventory", url = "http://localhost:8082")
  // define what kind of http client is going to call in this request
    @RequestMapping(method = RequestMethod.GET, value = "/api/inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam int quantity);

    // this is only getmapping, we could also do same for post put or delete method
*/

    @GetExchange("/api/inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam int quantity);
// Using GetExchange Annotation, Spring will take care of all the url and mapping that we implemented above.


}

package com.test.feign;

import com.test.config.FeignClientConfig;
import com.test.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Created by ren.xiaobo on 2016/8/29.
 */
@FeignClient(name = "springboot", configuration = FeignClientConfig.class)
@Path("/springboot/v1/users")
public interface UserFeignClient {
    @GET
    @Path("/{id}")
    User getUser(@PathParam("id") long id);

    @GET
    @Path("/")
    List<User> getUsers();
}

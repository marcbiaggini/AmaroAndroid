package amaro.api.ServiceInterfaces;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import amaro.api.BuildConfig;
import amaro.api.Model.MyProduct.ProducSelectedResponse;
import lombok.experimental.NonFinal;

/**
 * Created by juan.villa on 12/08/2016.
 */
@NonFinal
@Rest(rootUrl = BuildConfig.DEV_API_URL, converters = { StringHttpMessageConverter.class,MappingJackson2HttpMessageConverter.class , FormHttpMessageConverter.class})
public interface MyProduct extends RestClientErrorHandling {
  @Get("_products/get/{style_color}")
  ProducSelectedResponse getProductSelected(@Path final String style_color);
}

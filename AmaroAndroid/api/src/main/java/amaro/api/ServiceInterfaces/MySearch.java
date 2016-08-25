package amaro.api.ServiceInterfaces;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.RequiresCookie;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by juan.villa on 01/08/2016.
 */
import amaro.api.BuildConfig;
import amaro.api.Model.MySearch.SearchResult;
import lombok.experimental.NonFinal;

@NonFinal
@Rest(rootUrl = BuildConfig.DEV_API_URL, converters = { StringHttpMessageConverter.class,MappingJackson2HttpMessageConverter.class , FormHttpMessageConverter.class})
public interface MySearch extends RestClientErrorHandling {

  @Get("mysearch")
  SearchResult search();

  @Get("mysearch/{s}")
  SearchResult search(@Path final String s);
}

package amaro.api.Error;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.api.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import amaro.api.Helpers.BusProvider;

/**
 * Created by juan.villa on 03/05/2016.
 */
@EBean
public class RSRestErrorHandler implements RestErrorHandler {

  protected static final ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public void onRestClientExceptionThrown(NestedRuntimeException e) {
    String message = null;
    String errorString = null;
    if (HttpStatusCodeException.class.isInstance(e)) {
      HttpStatusCodeException e2 = (HttpStatusCodeException) e;
      List<String> contentType = e2.getResponseHeaders().get("Content-Type");
      if (contentType.size() > 0 && contentType.get(0).equals("application/json")) {
        try {
          final Map<String, Object> json = MAPPER.readValue(e2.getResponseBodyAsString(), new TypeReference<Map<String, Object>>() {
          });
          errorString = (String) json.get("error");
          message = (String) json.get("message");
          ErrorCode error = ErrorCode.valueOf(errorString);
//          throw new Error(error, message);
          BusProvider.getInstance().post(new RestErrorEvent(error, message));
        } catch (IllegalArgumentException iae) {
//          throw new Error(ErrorCode.UnknownError, "Unknown Error from Backend (" + errorString + "): " + message);
          BusProvider.getInstance().post(new RestErrorEvent(ErrorCode.UnknownError, "Unknown Error from Backend (" + errorString + "): " + message));
        } catch (UnknownHostException unknownHostException){
          BusProvider.getInstance().post(new RestErrorEvent(ErrorCode.NoHostSolved, "Unable to resolve host"));
        } catch (IOException ioe) {
//          throw new Error(ErrorCode.InternalError, "Cannot convert JSON: " + ioe.getLocalizedMessage());
          BusProvider.getInstance().post(new RestErrorEvent(ErrorCode.InternalError, "Cannot convert JSON: " + ioe.getLocalizedMessage()));
        } catch (ResourceAccessException rae) {
          BusProvider.getInstance().post(new RestErrorEvent(ErrorCode.ConnectionError, "Connection Refused" + rae.getLocalizedMessage()));
        } catch (RestClientException rce) {
          BusProvider.getInstance().post(new RestErrorEvent(ErrorCode.AmaroServerError, "Unexpected response from server. Check for the url path or server's response at:" + rce.getLocalizedMessage()));
        } catch (HttpMessageNotReadableException nre) {
          BusProvider.getInstance().post(new RestErrorEvent(ErrorCode.AmaroServerError, "Unexpected response from server Cannot convert JSON" + nre.getLocalizedMessage()));
        }
      }
    } else {
      BusProvider.getInstance().post(new RestErrorEvent(ErrorCode.UnknownError, "Unknown Error(" + e.getClass().getName() + "): " + e.getLocalizedMessage()));
    }
  }
}


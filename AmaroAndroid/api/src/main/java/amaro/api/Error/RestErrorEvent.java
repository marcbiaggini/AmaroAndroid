package amaro.api.Error;

/**
 * Created by juan.villa on 03/05/2016.
 */
public class RestErrorEvent {

  private ErrorCode errorCode;
  private String message;

  public RestErrorEvent(ErrorCode errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }

}

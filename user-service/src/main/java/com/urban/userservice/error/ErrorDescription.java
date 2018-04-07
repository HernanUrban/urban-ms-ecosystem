package com.urban.userservice.error;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ErrorDescription {

  /**
   * Default number of lines of the stack trace.
   */
  private static final int MAX_STACK_LINES = 5;

  /**
   * A code identifying the problem.
   */
  private int code;

  /**
   * The module where the problem occurs.
   */
  private String module;

  /**
   * A description indication the problem.
   */
  private String description;

  /**
   * The URI, if any, that causes the problem.
   */
  private String uri;

  /**
   * The HTTP method, POST, GET, etc.
   */
  private String httpMethod;

  /**
   * The host name.
   */
  private String hostName;
  private String localHostName;

  /**
   * Local IP Address.
   */
  private String localAddr;

  /**
   * Remote IP Address.
   */
  private String remoteAddr;

  /**
   * Part of the stack trace.
   */
  private String trace;

  /**
   * Default constructor.
   */
  private ErrorDescription() {
  }

  /**
   * Retrieves the error code.
   *
   * @return the error code.
   */
  public int getCode() {
    return code;
  }

  /**
   * Sets the error code.
   *
   * @param code the error code.
   */
  private void setCode(int code) {
    this.code = code;
  }

  /**
   * Retrieves the name of the module.
   *
   * @return the name of the module.
   */
  public String getModule() {
    return module;
  }

  /**
   * Sets the module where the error occurred.
   *
   * @param module the name of the module.
   */
  private void setModule(String module) {
    this.module = module;
  }

  /**
   * Retrieves the description of the error.
   *
   * @return the description of the error.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the error.
   *
   * @param description the description of the error.
   */
  private void setDescription(String description) {
    this.description = description;
  }

  /**
   * Retrieves the URI where the error occurred.
   *
   * @return the URI.
   */
  public String getUri() {
    return uri;
  }

  /**
   * Sets the URI.
   *
   * @param uri the uri.
   */
  private void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * Retrieves the HTTP method.
   *
   * @return the http method.
   */
  public String getHttpMethod() {
    return httpMethod;
  }

  /**
   * Sets the http method.
   *
   * @param httpMethod the http method.
   */
  private void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  /**
   * Retrieves the host name.
   *
   * @return the host name.
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * Sets the host name.
   *
   * @param hostName the host name.
   */
  private void setHostName(String hostName) {
    this.hostName = hostName;
  }

  /**
   * Retrieves the local host name.
   *
   * @return the local host name.
   */
  public String getLocalHostName() {
    return localHostName;
  }

  /**
   * Sets the local host name.
   *
   * @param localHostName the host name.
   */
  private void setLocalHostName(String localHostName) {
    this.localHostName = localHostName;
  }

  /**
   * Retrieves the local ip address.
   *
   * @return the local address.
   */
  public String getLocalAddr() {
    return localAddr;
  }

  /**
   * k Sets the local ipaddress.
   *
   * @param localAddr the local address.
   */
  private void setLocalAddr(String localAddr) {
    this.localAddr = localAddr;
  }

  /**
   * Sets the remote ip address.
   *
   * @return the remote ip address.
   */
  public String getRemoteAddr() {
    return remoteAddr;
  }

  /**
   * Sets the remote ip address.
   *
   * @param remoteAddr the remote address.
   */
  private void setRemoteAddr(String remoteAddr) {
    this.remoteAddr = remoteAddr;
  }

  /**
   * Retrieves the a resumed stacktrace as string.
   *
   * @return stack trace.
   */
  public String getTrace() {
    return trace;
  }

  /**
   * Sets the stack trace.
   *
   * @param trace the trace.
   */
  private void setTrace(String trace) {
    this.trace = trace;
  }

  /**
   * Creates an instance of the ErrorDescription.
   *
   * @param code      the error code.
   * @param module    the name of the module.
   * @param throwable throwable that produced the error.
   * @param request   the http request. If this was executed inside an http context.
   * @return an ErrorDescription.
   */
  public static ErrorDescription buildError(int code, String module, Throwable throwable,
                                            HttpServletRequest request) {
    ErrorDescription dto = new ErrorDescription();
    dto.setCode(code);
    dto.setModule(module);
    if (throwable != null) {
      dto.setDescription(throwable.getMessage());
      dto.setTrace(buildStringTrace(throwable.getStackTrace()));
    }
    return requestInformation(dto, request);
  }

  /**
   * Builds an error description.
   *
   * @param module  the module name.
   * @param error   the error thrown.
   * @param request the request that causes the error.
   * @return an ErrorDescription.
   */
  public static ErrorDescription buildError(String module, UserServiceError error,
                                            HttpServletRequest
      request) {
    ErrorDescription description = new ErrorDescription();
    description.setCode(error.getErrorCode());
    description.setModule(module);
    description.setDescription(error.getMessage());
    description.setTrace(buildStringTrace(error.getStackTrace()));
    return requestInformation(description, request);
  }

  /**
   * Enhance the error description with request information.
   *
   * @param description the error description.
   * @param request     the request.
   * @return the error description with request information.
   */
  private static ErrorDescription requestInformation(ErrorDescription description,
                                                     HttpServletRequest request) {
    if (request != null) {
      description.setHostName(request.getServerName());
      description.setLocalAddr(request.getLocalAddr());
      description.setRemoteAddr(request.getRemoteAddr());
      description.setLocalHostName(request.getLocalName());
      description.setHttpMethod(request.getMethod());
      description.setUri(request.getRequestURI());
    }
    return description;
  }

  /**
   * Receives an stack trace and returns a String containing some lines.
   *
   * @param elements the stack trace.
   * @return a String with some lines of the stack trace.
   */
  private static String buildStringTrace(StackTraceElement[] elements) {
    if (elements != null && elements.length > 0) {
      StringBuilder builder = new StringBuilder();
      int length = elements.length;
      for (int t = 0; t < length && t < MAX_STACK_LINES; t++) {
        builder.append(String.format("%s:%s:%s(%s) ", elements[t].getClassName(), elements[t]
                .getMethodName(),
            elements[t].getLineNumber(), elements[t].getFileName()));
      }
      return builder.toString();
    }
    return "";
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}

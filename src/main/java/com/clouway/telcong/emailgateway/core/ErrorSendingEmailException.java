package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class ErrorSendingEmailException extends RuntimeException {

  public ErrorSendingEmailException(String message) {
    super(message);
  }
}
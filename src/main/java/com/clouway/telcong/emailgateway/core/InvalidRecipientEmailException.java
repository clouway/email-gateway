package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class InvalidRecipientEmailException extends RuntimeException {

  public InvalidRecipientEmailException(String message) {
    super(message);
  }
}
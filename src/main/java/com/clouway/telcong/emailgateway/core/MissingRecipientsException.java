package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class MissingRecipientsException extends RuntimeException {

  public MissingRecipientsException(String message) {
    super(message);
  }
}
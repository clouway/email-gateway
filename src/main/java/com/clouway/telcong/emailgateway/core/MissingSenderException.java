package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class MissingSenderException extends RuntimeException {

  public MissingSenderException(String message) {
    super(message);
  }
}
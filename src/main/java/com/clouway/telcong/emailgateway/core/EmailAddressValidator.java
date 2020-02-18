package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public interface EmailAddressValidator {

  boolean isValid(String email);

}

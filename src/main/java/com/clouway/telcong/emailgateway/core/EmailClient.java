package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public interface EmailClient {

  void send(Email email, SendEmailCallback callback);

}

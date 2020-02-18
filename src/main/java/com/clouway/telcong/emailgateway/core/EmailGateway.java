package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public interface EmailGateway {

  Email send(Email email);

  Email send(TemplatedEmail email);
}

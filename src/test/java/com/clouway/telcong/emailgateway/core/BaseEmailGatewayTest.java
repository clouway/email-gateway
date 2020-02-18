package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class BaseEmailGatewayTest extends EmailGatewayContractTest {

  @Override
  protected EmailGateway getEmailGateway() {
    return new BaseEmailGateway(emailClient);
  }
}

package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public interface SendEmailCallback {

  void onSuccess();

  void onFailure();

  void onMissingConfiguration();
}

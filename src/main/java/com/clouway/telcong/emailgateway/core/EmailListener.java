package com.clouway.telcong.emailgateway.core;

import java.util.EventListener;

/**
 * @author Georgi Georgiev (georgi.georgiev@clouway.com)
 */
public interface EmailListener extends EventListener {

  /**
   * Provides the email which will be send.
   *
   * @param email for sending
   */
  void onSendEmail(Email email);

  /**
   * Provides the email which is send successfully.
   *
   * @param email which is sent send successfully
   */
  void onSuccess(Email email);

  /**
   * Provides the email for which sending is failed.
   *
   * @param email for which sending is failed
   */
  void onFailure(Email email);
}

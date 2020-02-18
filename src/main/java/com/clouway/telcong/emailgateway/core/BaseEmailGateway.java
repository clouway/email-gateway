package com.clouway.telcong.emailgateway.core;

import java.util.logging.Logger;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class BaseEmailGateway implements EmailGateway {

  private static final Logger log = Logger.getLogger(BaseEmailGateway.class.getName());

  private final EmailClient emailClient;

  public BaseEmailGateway(EmailClient emailClient) {
    this.emailClient = emailClient;
  }

  @Override
  public Email send(final Email email) {
    emailClient.send(email, new SendEmailCallback() {

      @Override
      public void onSuccess() {
      }

      @Override
      public void onFailure() {
        throw new ErrorSendingEmailException("An error occurred while sending the email. Please try again.");
      }

      @Override
      public void onMissingConfiguration() {
      }
    });

    return email;
  }

  @Override
  public Email send(TemplatedEmail email) {
    log.info("Default email gateway not use template evaluator!");

    return send(Email.aNewEmail()
            .subject(email.getSubjectTemplate())
            .setHtmlBody(email.getBodyTemplate())
            .sender(email.getSender())
            .recipients(email.getRecipients())
            .emailType(email.getType())
            .emailReference(email.getReference())
            .setAttachmentFiles(email.getAttachmentFiles())
            .build());
  }
}

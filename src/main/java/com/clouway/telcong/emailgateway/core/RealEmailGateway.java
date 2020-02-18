package com.clouway.telcong.emailgateway.core;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class RealEmailGateway implements EmailGateway {

  private final EmailClient emailClient;
  private final CurrentDateProvider currentDate;
  private final EmailTemplateEvaluator evaluator;
  private final EmailListener listener;

  public RealEmailGateway(EmailClient emailClient,
                          CurrentDateProvider currentDate,
                          EmailTemplateEvaluator evaluator,
                          EmailListener listener) {
    this.emailClient = emailClient;
    this.currentDate = currentDate;
    this.evaluator = evaluator;
    this.listener = listener;
  }

  @Override
  public Email send(final Email email) {
    listener.onSendEmail(email);

    emailClient.send(email, new SendEmailCallback() {

      @Override
      public void onSuccess() {
        email.wasSentAt(currentDate.get());
        listener.onSuccess(email);
      }

      @Override
      public void onFailure() {
        email.wasNotSentAt(currentDate.get());
        listener.onFailure(email);

        throw new ErrorSendingEmailException("An error occurred while sending the email. Please try again.");
      }

      @Override
      public void onMissingConfiguration() {
        email.wasNotSentAt(currentDate.get());
        listener.onFailure(email);
      }
    });

    return email;
  }

  @Override
  public Email send(TemplatedEmail email) {
    String subject = evaluator.evaluateSubject(email.getSubjectModel(), email.getSubjectTemplate());
    String body = evaluator.evaluateBody(email.getBodyModel(), email.getBodyTemplate());

    return send(Email.aNewEmail()
            .id(email.getId())
            .subject(subject)
            .setHtmlBody(body)
            .sender(email.getSender())
            .recipients(email.getRecipients())
            .emailType(email.getType())
            .emailReference(email.getReference())
            .setAttachmentFiles(email.getAttachmentFiles())
            .build());
  }
}
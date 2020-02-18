package com.clouway.telcong.emailgateway.sendgrid;

import com.clouway.telcong.emailgateway.core.AttachmentFile;
import com.clouway.telcong.emailgateway.core.EmailClient;
import com.clouway.telcong.emailgateway.core.SendEmailCallback;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Krasimir Raikov(krasimir.raikov@clouway.com)
 */
class SendgridEmailClient implements EmailClient {

  private static final Logger log = Logger.getLogger(SendgridEmailClient.class.getName());

  private final String apiKey;
  private final String senderDomain;
  private final SendGridHost sendGridHost;

  SendgridEmailClient(String apiKey, String senderDomain, SendGridHost sendGridHost) {
    this.apiKey = apiKey;
    this.senderDomain = senderDomain;
    this.sendGridHost = sendGridHost;
  }

  @Override
  public void send(com.clouway.telcong.emailgateway.core.Email email, SendEmailCallback callback) {

    if (Strings.isNullOrEmpty(apiKey) || Strings.isNullOrEmpty(senderDomain)) {
      callback.onMissingConfiguration();
      return;
    }

    try {
      String sender = email.getSender() + "@" + senderDomain;
      Sendgrid sendgrid = new Sendgrid(sendGridHost.getValue(), apiKey)
              .setFrom(sender)
              .addTo(email.getRecipients().toArray(new String[email.getRecipients().size()]))
              .setSubject(email.getSubject())
              .setHtml(email.getBody());
      for (AttachmentFile attachmentFile : email.getAttachmentFiles()) {
        sendgrid.addAttachment(attachmentFile.name, attachmentFile.content);
      }
      sendgrid.send();
      callback.onSuccess();
    } catch (IOException e) {
      log.info(Throwables.getStackTraceAsString(e));
      callback.onFailure();
    }
  }
}

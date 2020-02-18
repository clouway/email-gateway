package com.clouway.telcong.emailgateway.gae;

import com.clouway.telcong.emailgateway.core.AttachmentFile;
import com.clouway.telcong.emailgateway.core.Email;
import com.clouway.telcong.emailgateway.core.EmailClient;
import com.clouway.telcong.emailgateway.core.SendEmailCallback;
import com.google.appengine.api.mail.MailService;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
class GaeEmailClient implements EmailClient {

  private static final Logger log = Logger.getLogger(GaeEmailClient.class.getName());

  private final MailService mailService;
  private final String domain;

  GaeEmailClient(MailService mailService, String domain) {
    this.mailService = mailService;
    this.domain = domain;
  }

  @Override
  public void send(Email email, SendEmailCallback callback) {

    MailService.Message message = new MailService.Message();

    message.setSender(email.getSender() + "@" + domain);
    message.setBcc(email.getRecipients());
    message.setSubject(email.getSubject());

    String idContainer = "";

    if (!Strings.isNullOrEmpty(email.getId())) {
      idContainer = "<br /><p style=\"font-size: 0px;\">eID:#" + String.valueOf(email.getId()) + "</p>";
    }

    message.setHtmlBody(email.getBody() + idContainer);

    Collection<MailService.Attachment> attachmentFiles = new ArrayList<>();
    for (AttachmentFile attachmentFile : email.getAttachmentFiles()) {
      attachmentFiles.add(new MailService.Attachment(attachmentFile.name, attachmentFile.content));
    }
    message.setAttachments(attachmentFiles);

    try {
      mailService.send(message);
      callback.onSuccess();
    } catch (Exception e) {
      log.info(Throwables.getStackTraceAsString(e));
      callback.onFailure();
    }
  }
}

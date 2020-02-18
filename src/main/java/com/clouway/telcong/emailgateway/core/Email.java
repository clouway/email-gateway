package com.clouway.telcong.emailgateway.core;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class Email {

  public static Builder aNewEmail() {
    return new Builder();
  }

  public static class Builder {

    private String id;
    private String sender;
    private Set<String> recipients = new LinkedHashSet<>();
    private String subject;
    private String body;
    private Set<AttachmentFile> attachmentFiles = new LinkedHashSet<>();
    private Set<String> attachmentFileNames = new LinkedHashSet<>();
    private String emailReference;
    private String emailType;
    private EmailStatus status = EmailStatus.NOT_SENT;
    private Date sentDate;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder sender(String sender) {
      this.sender = sender;
      return this;
    }

    public Builder recipients(String... recipients) {
      Collections.addAll(this.recipients, recipients);
      return this;
    }

    public Builder recipients(Set<String> recipients) {
      this.recipients = recipients;
      return this;
    }

    public Builder subject(String subject) {
      this.subject = subject;
      return this;
    }


    public Builder addAttachmentFile(AttachmentFile attachmentFile) {
      this.attachmentFiles.add(attachmentFile);
      this.attachmentFileNames.add(attachmentFile.name);
      return this;
    }

    public Builder setAttachmentFiles(Set<AttachmentFile> attachmentFiles) {
      this.attachmentFiles = attachmentFiles;

      this.attachmentFileNames = new LinkedHashSet<>();
      for (AttachmentFile attachmentFile : attachmentFiles) {
        this.attachmentFileNames.add(attachmentFile.name);
      }

      return this;
    }

    public Builder emailReference(String emailReference) {
      this.emailReference = emailReference;
      return this;
    }

    public Builder addAttachmentFileName(String fileName) {
      this.attachmentFileNames.add(fileName);
      return this;
    }

    public Builder setAttachmentFileNames(Set<String> attachmentFileNames) {
      this.attachmentFileNames = attachmentFileNames;
      return this;
    }

    public Builder sentDate(Date sentDate) {
      this.sentDate = sentDate;
      return this;
    }

    public Builder emailType(String emailType) {
      this.emailType = emailType;
      return this;
    }

    public Builder status(EmailStatus status) {
      this.status = status;
      return this;
    }

    public Builder setHtmlBody(String body) {
      this.body = body;
      return this;
    }

    public Builder setTextBody(String body) {
      this.body = "<pre> " + body + "</pre>";
      return this;
    }

    public Email build() {

      Email email = new Email();

      email.id = id;
      if (id == null) {
        email.id = UUID.randomUUID().toString().replaceAll("-", "");
      }
      email.sender = sender;
      email.recipients = recipients;
      email.subject = subject;
      email.body = body;
      email.emailReference = emailReference;
      email.attachmentFiles = attachmentFiles;
      email.attachmentFileNames = attachmentFileNames;
      email.type = emailType;
      email.status = status;
      email.sentDate = sentDate;

      return email;
    }
  }

  private String id;

  private String sender;

  private Set<String> recipients = new LinkedHashSet<>();

  private String subject;

  private String body;

  private String emailReference;

  private Set<AttachmentFile> attachmentFiles = new LinkedHashSet<>();

  private Set<String> attachmentFileNames = new LinkedHashSet<>();

  private String type;

  private EmailStatus status;

  private Date sentDate;

  private String sentFrom;

  private String senderUserId;

  public Email() {
  }

  public Set<String> getRecipients() {
    return recipients;
  }

  public String getSender() {
    return sender;
  }

  public Set<AttachmentFile> getAttachmentFiles() {
    return attachmentFiles;
  }

  public Set<String> getAttachmentFileNames() {
    return attachmentFileNames;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public String getEmailReference() {
    return emailReference;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void wasSentAt(Date date) {
    status = EmailStatus.SENT;
    sentDate = date;
  }

  public void wasNotSentAt(Date date) {
    status = EmailStatus.NOT_SENT;
    sentDate = date;
  }

  public EmailStatus getStatus() {
    return status;
  }

  public Date getSentDate() {
    return sentDate;
  }

  public void wasNotDelivered() {
    status = EmailStatus.NOT_DELIVERED;
  }

  public void addAttachmentFile(AttachmentFile attachmentFile) {
    this.attachmentFiles.add(attachmentFile);
  }

  public String getSentFrom() {
    return sentFrom;
  }

  public String getSenderUserId() {
    return senderUserId;
  }

  public String getId() {
    return id;
  }

  public void sentFromUser(String name, String eid) {
    this.sentFrom = name;
    this.senderUserId = eid;
  }
}

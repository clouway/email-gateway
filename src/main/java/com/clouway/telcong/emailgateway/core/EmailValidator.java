package com.clouway.telcong.emailgateway.core;

import com.google.common.base.Strings;

import java.util.Set;

/**
 * @author Georgi Georgiev (georgi.georgiev@clouway.com)
 */
public class EmailValidator implements EmailGateway {

  private final EmailAddressValidator emailAddressValidator;
  private final EmailGateway origin;

  public EmailValidator(EmailGateway origin, EmailAddressValidator emailAddressValidator) {
    this.origin = origin;
    this.emailAddressValidator = emailAddressValidator;
  }

  @Override
  public Email send(Email email) {
    validate(email.getSender(), email.getRecipients());
    return origin.send(email);
  }

  @Override
  public Email send(TemplatedEmail email) {
    validate(email.getSender(), email.getRecipients());
    return origin.send(email);
  }

  private void validate(String sender, Set<String> recipients) {

    if (Strings.isNullOrEmpty(sender)) {
      throw new MissingSenderException("Missing Email Sender");
    }

    if (recipients.isEmpty()) {
      throw new MissingRecipientsException("Missing Email Recipient");
    }

    if (!isRecipientEmailAddressValid(recipients)) {
      throw new InvalidRecipientEmailException("Invalid Recipient Email");
    }
  }

  private boolean isRecipientEmailAddressValid(Set<String> recipients) {
    for (String to : recipients) {
      if (emailAddressValidator.isValid(to)) {
        return true;
      }
    }
    return false;
  }
}

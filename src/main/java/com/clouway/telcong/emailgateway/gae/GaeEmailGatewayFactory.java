package com.clouway.telcong.emailgateway.gae;

import com.clouway.telcong.emailgateway.core.BaseEmailGateway;
import com.clouway.telcong.emailgateway.core.CurrentDateProvider;
import com.clouway.telcong.emailgateway.core.EmailAddressValidatorImpl;
import com.clouway.telcong.emailgateway.core.EmailClient;
import com.clouway.telcong.emailgateway.core.EmailGateway;
import com.clouway.telcong.emailgateway.core.EmailListener;
import com.clouway.telcong.emailgateway.core.EmailTemplateEvaluator;
import com.clouway.telcong.emailgateway.core.EmailValidator;
import com.clouway.telcong.emailgateway.core.RealEmailGateway;
import com.clouway.telcong.internal.Announcer;
import com.google.appengine.api.mail.MailService;

import java.util.Set;

/**
 * @author Georgi Georgiev (georgi.georgiev@clouway.com)
 */
public class GaeEmailGatewayFactory {

  public static EmailGateway create(MailService mailService, String domain) {
    return new EmailValidator(new BaseEmailGateway(createEmailClient(mailService, domain)), new EmailAddressValidatorImpl());
  }

  public static EmailGateway create(MailService mailService,
                                    String domain,
                                    CurrentDateProvider currentDate,
                                    EmailTemplateEvaluator evaluator,
                                    Set<EmailListener> listeners) {

    RealEmailGateway origin = new RealEmailGateway(createEmailClient(mailService, domain), currentDate, evaluator, Announcer.to(EmailListener.class, listeners).announce());

    return new EmailValidator(origin, new EmailAddressValidatorImpl());
  }

  private static EmailClient createEmailClient(MailService mailService, String domain) {
    return new GaeEmailClient(mailService, domain);
  }
}

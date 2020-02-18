package com.clouway.telcong.emailgateway.core;

import com.clouway.testing.util.ArgumentCaptor;
import com.google.common.collect.Sets;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.clouway.telcong.emailgateway.core.Email.aNewEmail;
import static com.clouway.testing.CommonMatchers.matching;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class RealEmailGatewayTest extends EmailGatewayContractTest {

  @Mock
  private EmailTemplateEvaluator evaluator;

  @Mock
  private EmailListener listener;

  private Date currentDate;

  private final ArgumentCaptor<SendEmailCallback> callbackCaptor = new ArgumentCaptor<>();

  private final String sender = "test@gmail.com";
  private final String recipient = "john@gmail.com";
  private final Email email = aNewEmail().id("abc123").emailReference("referent").sender(sender)
          .recipients(recipient).subject("subject").build();

  private EmailGateway emailGateway;

  @Before
  public void init() {
    emailGateway = initEmailService();
  }

  @Override
  protected EmailGateway getEmailGateway() {
    return emailGateway;
  }

  @Test
  public void emailIsSuccessfullySent() {
    context.checking(new Expectations() {{
      oneOf(listener).onSendEmail(email);

      oneOf(emailClient).send(with(email), with(callbackCaptor));

      oneOf(listener).onSuccess(email);
    }});

    Email sendEmail = emailGateway.send(email);
    callbackCaptor.getValue().onSuccess();

    assertThat(sendEmail.getStatus(), is(EmailStatus.SENT));
    assertThat(sendEmail.getSentDate(), is(currentDate));
  }

  @Test
  public void failedToSentEmail() {

    context.checking(new Expectations() {{
      oneOf(listener).onSendEmail(email);

      oneOf(emailClient).send(with(email), with(callbackCaptor));

      oneOf(listener).onFailure(email);
    }});

    try {

      emailGateway.send(email);
      callbackCaptor.getValue().onFailure();
      fail();

    } catch (ErrorSendingEmailException e) {
      assertThat(e.getMessage(), is("An error occurred while sending the email. Please try again."));
    }
  }

  @Test
  public void missingEmailProviderConfigurations() {

    context.checking(new Expectations() {{
      oneOf(listener).onSendEmail(email);

      oneOf(emailClient).send(with(email), with(callbackCaptor));

      oneOf(listener).onFailure(email);
    }});

    emailGateway.send(email);
    callbackCaptor.getValue().onMissingConfiguration();
  }

  @Test
  public void sendTemplatedEmail() throws Exception {
    final Object subjectModel = new Object();
    final Object bodyModel = new Object();

    TemplatedEmail templatedEmail = new TemplatedEmail.Builder().id("abc123")
            .subject(subjectModel, "SubjectTemplate")
            .body(bodyModel, "BodyTemplate")
            .sender(sender)
            .recipient(recipient)
            .type("EMAIL_CONFIRMATION")
            .reference("referent")
            .attachmentFiles(Sets.newHashSet(new AttachmentFile("file name", new byte[0])))
            .build();

    final Email expectedSendEmail = aNewEmail().id(templatedEmail.getId()).sender(sender).recipients(recipient)
            .subject("subject content").setHtmlBody("body content").emailType("EMAIL_CONFIRMATION")
            .emailReference("referent").addAttachmentFile(new AttachmentFile("file name", new byte[0]))
            .status(EmailStatus.NOT_SENT).build();

    final Email expectedSentEmail = aNewEmail().id(templatedEmail.getId()).sender(sender).recipients(recipient)
            .subject("subject content").setHtmlBody("body content").emailType("EMAIL_CONFIRMATION")
            .emailReference("referent").addAttachmentFile(new AttachmentFile("file name", new byte[0]))
            .status(EmailStatus.SENT).sentDate(currentDate).build();

    context.checking(new Expectations() {{
      oneOf(evaluator).evaluateSubject(subjectModel, "SubjectTemplate");
      will(returnValue("subject content"));

      oneOf(evaluator).evaluateBody(bodyModel, "BodyTemplate");
      will(returnValue("body content"));

      oneOf(listener).onSendEmail(with(matching(expectedSendEmail)));

      oneOf(emailClient).send(with(matching(expectedSendEmail)), with(callbackCaptor));

      oneOf(listener).onSuccess(with(matching(expectedSentEmail)));
    }});

    Email sendEmail = emailGateway.send(templatedEmail);

    callbackCaptor.getValue().onSuccess();

    assertThat(sendEmail.getStatus(), is(EmailStatus.SENT));
    assertThat(sendEmail.getSentDate(), is(currentDate));
  }

  private EmailGateway initEmailService() {
    currentDate = super.getCurrentDate();
    emailClient = super.getEmailClient();

    CurrentDateProvider currentDate = new CurrentDateProvider() {
      @Override
      public Date get() {
        return RealEmailGatewayTest.this.currentDate;
      }
    };

    return emailGateway = new RealEmailGateway(emailClient, currentDate, evaluator, listener);
  }
}

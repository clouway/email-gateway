package com.clouway.telcong.emailgateway.core;

import com.clouway.testing.util.ArgumentCaptor;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static com.clouway.telcong.emailgateway.core.Email.aNewEmail;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public abstract class EmailGatewayContractTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  protected EmailClient emailClient;

  protected abstract EmailGateway getEmailGateway();

  private final String sender = "test@gmail.com";
  private final String recipient = "john@gmail.com";
  private final Email email = aNewEmail().sender(sender).recipients(recipient).build();

  private final ArgumentCaptor<SendEmailCallback> callbackCaptor = new ArgumentCaptor<>();

  private EmailGateway emailGateway;

  @Before
  public void setUp() throws Exception {
    emailGateway = getEmailGateway();
  }

  @Test
  public void emailIsSuccessfullySent() {

    context.checking(new Expectations() {{
      oneOf(emailClient).send(with(email), with(callbackCaptor));
    }});

    emailGateway.send(email);
    callbackCaptor.getValue().onSuccess();
  }

  @Test
  public void failedToSentEmail() {

    context.checking(new Expectations() {{
      oneOf(emailClient).send(with(email), with(callbackCaptor));
    }});

    try {

      emailGateway.send(email);
      callbackCaptor.getValue().onFailure();
      fail();

    } catch (ErrorSendingEmailException e) {
      assertThat(e.getMessage(), is("An error occurred while sending the email. Please try again."));
    }
  }

  public EmailClient getEmailClient() {
    return emailClient;
  }

  public JUnitRuleMockery getContext() {
    return context;
  }

  public Date getCurrentDate() {
    return new Date();
  }

  public String getSender() {
    return sender;
  }

  public Email getEmail() {
    return email;
  }

  public String getRecipient() {
    return recipient;
  }
}

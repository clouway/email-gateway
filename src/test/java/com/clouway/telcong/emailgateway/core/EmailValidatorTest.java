package com.clouway.telcong.emailgateway.core;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.clouway.telcong.emailgateway.core.Email.aNewEmail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Georgi Georgiev (georgi.georgiev@clouway.com)
 */
public class EmailValidatorTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private EmailGateway origin;

  @Mock
  private EmailAddressValidator emailAddressValidator;

  private EmailGateway gateway;

  private final String sender = "test@gmail.com";
  private final String recipient = "john@gmail.com";

  @Before
  public void setUp() throws Exception {
    gateway = new EmailValidator(origin, emailAddressValidator);
  }

  @Test
  public void successfullyValidatedEmail() throws Exception {
    final Email email = aNewEmail().sender(sender).recipients(recipient).build();

    context.checking(new Expectations() {{
      oneOf(emailAddressValidator).isValid(recipient);
      will(returnValue(true));

      oneOf(origin).send(email);
      will(returnValue(email));
    }});

    Email sendEmail = gateway.send(email);

    assertThat(sendEmail, is(email));
  }

  @Test
  public void validateEmailWithMissingRecipient() {
    final Email email = aNewEmail().sender(sender).build();

    context.checking(new Expectations() {{

      never(origin);
    }});

    try {
      gateway.send(email);
      fail();
    } catch (MissingRecipientsException e) {
      assertThat(e.getMessage(), is(equalTo("Missing Email Recipient")));
    }
  }

  @Test
  public void validateEmailWithoutSender() {
    final Email email = aNewEmail().recipients(recipient).build();

    try {
      gateway.send(email);
      fail();
    } catch (MissingSenderException e) {
      assertThat(e.getMessage(), is(equalTo("Missing Email Sender")));
    }
  }

  @Test
  public void validateEmailWithInvalidEmailAddress() {
    final Email email = aNewEmail().sender(sender).recipients(recipient).build();

    context.checking(new Expectations() {{
      oneOf(emailAddressValidator).isValid(recipient);
      will(returnValue(false));
    }});

    try {
      gateway.send(email);
      fail();
    } catch (InvalidRecipientEmailException e) {
      assertThat(e.getMessage(), is(equalTo("Invalid Recipient Email")));
    }
  }

  @Test
  public void successfullyValidatedTempatedEmail() throws Exception {
    final TemplatedEmail email = new TemplatedEmail.Builder().sender(sender).recipient(recipient).build();

    context.checking(new Expectations() {{
      oneOf(emailAddressValidator).isValid(recipient);
      will(returnValue(true));

      oneOf(origin).send(email);
    }});

    gateway.send(email);
  }
}
package com.clouway.telcong.emailgateway.gae;

import com.clouway.telcong.emailgateway.core.AttachmentFile;
import com.clouway.telcong.emailgateway.core.Email;
import com.clouway.telcong.emailgateway.core.EmailClient;
import com.clouway.telcong.emailgateway.core.SendEmailCallback;
import com.clouway.testing.util.ArgumentCaptor;
import com.google.appengine.api.mail.MailService;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.clouway.telcong.emailgateway.core.Email.aNewEmail;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class GaeEmailClientTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private EmailClient emailClient;

  @Mock
  private MailService mailService;

  private ArgumentCaptor<MailService.Message> messageCaptor = new ArgumentCaptor<MailService.Message>();

  private String domain = "domain.com";

  @Before
  public void init() {
    emailClient = new GaeEmailClient(mailService, domain);
  }

  @Test
  public void sendEmailSuccessfully() throws IOException {

    Email email = aNewEmail().id("abc123")
                             .sender("test")
                             .recipients("abc@gmail.com")
                             .subject("This is the subject")
                             .addAttachmentFile(new AttachmentFile("file_1", new byte[0]))
                             .addAttachmentFile(new AttachmentFile("file_2", new byte[0]))
                             .build();

    StubSendEmailCallback callback = new StubSendEmailCallback();

    context.checking(new Expectations() {{
      oneOf(mailService).send(with(messageCaptor));
    }});

    emailClient.send(email, callback);

    MailService.Message message = messageCaptor.getValue();

    assertThat(message.getSender(), is("test@domain.com"));
    assertThat(message.getBcc().containsAll(email.getRecipients()), is(true));
    assertThat(message.getSubject(), is(equalTo(email.getSubject())));
    assertThat(message.getHtmlBody().contains(String.valueOf(email.getId())), is(true));
    assertThat(message.getAttachments().size(), is(equalTo(email.getAttachmentFiles().size())));

    assertThat(callback.success, is(true));
    assertThat(callback.failed, is(false));
  }

  @Test
  public void failedToSendEmail() throws IOException {

    StubSendEmailCallback callback = new StubSendEmailCallback();

    context.checking(new Expectations() {{
      oneOf(mailService).send(with(any(MailService.Message.class)));
      will(throwException(new IllegalArgumentException()));
    }});

    emailClient.send(aNewEmail().build(), callback);

    assertThat(callback.success, is(false));
    assertThat(callback.failed, is(true));
  }

  @Test
  public void skipIdAdditionWhenMissingEmailId() throws Exception {
    Email email = aNewEmail().id("")
            .sender("test@gmail.com")
            .recipients("abc@gmail.com")
            .subject("This is the subject")
            .addAttachmentFile(new AttachmentFile("file_1", new byte[0]))
            .addAttachmentFile(new AttachmentFile("file_2", new byte[0]))
            .build();

    context.checking(new Expectations() {{
      oneOf(mailService).send(with(messageCaptor));
    }});

    emailClient.send(email, new StubSendEmailCallback());

    MailService.Message message = messageCaptor.getValue();
    String body = message.getHtmlBody();

    assertFalse("email id tags should not be added", body.contains("eID:#"));
  }

  private class StubSendEmailCallback implements SendEmailCallback {

    public boolean success = false;
    public boolean failed = false;

    @Override
    public void onSuccess() {
      success = true;
    }

    @Override
    public void onFailure() {
      failed = true;
    }

    @Override
    public void onMissingConfiguration() {
    }
  }
}

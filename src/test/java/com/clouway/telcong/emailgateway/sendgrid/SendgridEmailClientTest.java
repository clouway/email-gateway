package com.clouway.telcong.emailgateway.sendgrid;

import com.clouway.telcong.emailgateway.core.AttachmentFile;
import com.clouway.telcong.emailgateway.core.Email;
import com.clouway.telcong.emailgateway.core.SendEmailCallback;
import com.clouway.testing.wiremock.MockServerRule;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;

import static com.clouway.telcong.emailgateway.core.Email.aNewEmail;
import static com.clouway.telcong.testing.JsonBuilder.aNewJson;
import static com.clouway.telcong.testing.JsonBuilder.aNewJsonArray;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

/**
 * @author Krasimir Raikov(krasimir.raikov@clouway.com)
 */
public class SendgridEmailClientTest {
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public MockServerRule wireMockRule = new MockServerRule();

  private String host = "";
  @Mock
  private SendEmailCallback callback;

  private String apiKey = "::apiKey::";
  private String domain = "domain.com";

  private SendgridEmailClient emailClient;

  @Before
  public void setUp() {
    host = wireMockRule.getHost();
    emailClient = new SendgridEmailClient(apiKey, domain, new SendGridHost(host));
  }

  @Test
  public void shouldNotSendEmailWhenNoApiKey() throws Exception {

    context.checking(new Expectations() {{
      oneOf(callback).onMissingConfiguration();
    }});

    new SendgridEmailClient("", domain, new SendGridHost(host)).send(aNewEmail().build(), callback);
  }

  @Test
  public void shouldNotSendEmailWhenNoDomainConfiguration() throws Exception {

    context.checking(new Expectations() {{
      oneOf(callback).onMissingConfiguration();
    }});

    new SendgridEmailClient("apikey", "", new SendGridHost(host)).send(aNewEmail().build(), callback);
  }

  @Test
  public void sendinEmailWithNoAttachments() {

    stubFor(post(urlMatching("/v3/mail/send")).willReturn(aResponse().withStatus(HttpURLConnection.HTTP_ACCEPTED).withBody("")));

    context.checking(new Expectations() {{
      oneOf(callback).onSuccess();
    }});

    Email email = aNewEmail().recipients("me@clouway.com").sender("noreply").subject("email subject").setHtmlBody("html body").build();
    emailClient.send(email, callback);

    verify(postRequestedFor(urlMatching("/v3/mail/send"))
            .withHeader("Content-Type", equalTo("application/json; charset=utf-8"))
            .withHeader("Authorization", equalTo("Bearer " + apiKey))
            .withRequestBody(equalTo(aNewJson()
                    .add("personalizations", aNewJsonArray(aNewJson().add("to", aNewJsonArray(aNewJson().add("email", "me@clouway.com")))))
                    .add("from", aNewJson().add("email", "noreply@domain.com"))
                    .add("subject", "email subject")
                    .add("content", aNewJsonArray(aNewJson().add("type", "text/html").add("value", "html body")))
                    .build())));
  }

  @Test
  public void sendingEmailWithAttachments() {
    stubFor(post(urlMatching("/v3/mail/send")).willReturn(aResponse().withStatus(HttpURLConnection.HTTP_ACCEPTED).withBody("")));

    context.checking(new Expectations() {{
      oneOf(callback).onSuccess();
    }});

    AttachmentFile attachmentFile = new AttachmentFile("::filename::", new byte[]{(byte) 0xe0});
    Email email = aNewEmail().recipients("me@clouway.com").sender("noreply").subject("email subject").setHtmlBody("html body").addAttachmentFile(attachmentFile).addAttachmentFileName(attachmentFile.name).build();
    emailClient.send(email, callback);

    verify(postRequestedFor(urlMatching("/v3/mail/send"))
            .withHeader("Content-Type", equalTo("application/json; charset=utf-8"))
            .withHeader("Authorization", equalTo("Bearer " + apiKey))
            .withRequestBody(equalTo(aNewJson()
                    .add("personalizations", aNewJsonArray(aNewJson().add("to", aNewJsonArray(aNewJson().add("email", "me@clouway.com")))))
                    .add("from", aNewJson().add("email", "noreply@domain.com"))
                    .add("subject", "email subject")
                    .add("content", aNewJsonArray(aNewJson().add("type", "text/html").add("value", "html body")))
                    .add("attachments", aNewJsonArray(aNewJson().add("content", "4A\u003d\u003d").add("filename", "::filename::")))
                    .build())));
  }

  @Test
  public void unsuccessfullSending() {
    stubFor(post(urlMatching("/v3/mail/send")).willReturn(aResponse().withStatus(HttpURLConnection.HTTP_BAD_REQUEST).withBody("")));

    context.checking(new Expectations() {{
      oneOf(callback).onFailure();
    }});

    AttachmentFile attachmentFile = new AttachmentFile("::filename::", new byte[]{(byte) 0xe0});
    Email email = aNewEmail().recipients("me@clouway.com").sender("noreply").subject("email subject").setHtmlBody("html body").addAttachmentFile(attachmentFile).addAttachmentFileName(attachmentFile.name).build();
    emailClient.send(email, callback);

    verify(postRequestedFor(urlMatching("/v3/mail/send"))
            .withHeader("Content-Type", equalTo("application/json; charset=utf-8"))
            .withHeader("Authorization", equalTo("Bearer " + apiKey))
            .withRequestBody(equalTo(aNewJson()
                    .add("personalizations", aNewJsonArray(aNewJson().add("to", aNewJsonArray(aNewJson().add("email", "me@clouway.com")))))
                    .add("from", aNewJson().add("email", "noreply@domain.com"))
                    .add("subject", "email subject")
                    .add("content", aNewJsonArray(aNewJson().add("type", "text/html").add("value", "html body")))
                    .add("attachments", aNewJsonArray(aNewJson().add("content", "4A\u003d\u003d").add("filename", "::filename::")))
                    .build())));
  }
}
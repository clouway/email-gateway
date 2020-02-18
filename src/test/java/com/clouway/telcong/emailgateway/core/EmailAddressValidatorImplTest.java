package com.clouway.telcong.emailgateway.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class EmailAddressValidatorImplTest {

  private EmailAddressValidator validator;

  @Before
  public void init() {
    this.validator = new EmailAddressValidatorImpl();
  }

  @Test
  public void validEmail() {

    assertThat(validator.isValid("john@gmail.com"), is(true));
  }

  @Test
  public void missingAtSign() {

    assertThat(validator.isValid("johngmail.com"), is(false));
  }

  @Test
  public void emptyEmail() {

    assertThat(validator.isValid(""), is(false));
  }

  @Test
  public void emailWithHashTag() {

    assertThat(validator.isValid("john@gmail.com#"), is(false));
  }

  @Test
  public void emailWithDollarSign() {

    assertThat(validator.isValid("john$gmail.com"), is(false));
  }

  @Test
  public void emailEndingWithEmptySpace() {

    assertThat(validator.isValid("john@gmail.com "), is(false));
  }

  @Test
  public void emailStartingWithEmptySpace() {

    assertThat(validator.isValid(" john@gmail.com"), is(false));
  }

  @Test
  public void emailWithEmptySpaceInTheMiddle() {

    assertThat(validator.isValid("john@ gmail.com"), is(false));
  }

  @Test
  public void emailWithInvertedComma() {

    assertThat(validator.isValid("joh\"n@gmail.com"), is(false));
  }

  @Test
  public void emailWithMissingDomain() {

    assertThat(validator.isValid("john@"), is(false));
  }

  @Test
  public void emailWithWrongQualifiedDomain() {

    assertThat(validator.isValid("john@gmailcom"), is(false));
  }

  @Test
  public void gaeEmailAddressSender() {

    assertThat(validator.isValid("noreply@s~clouwaytestapp.appspotmail.com"), is(true));
  }

  @Test
  public void nullEmailAddress() {

    assertThat(validator.isValid(null), is(false));
  }

  @Test
  public void emailDomainContainingUnderscore() {

    assertThat(validator.isValid("john@g_mail.com"), is(true));
  }
}

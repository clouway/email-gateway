package com.clouway.telcong.emailgateway.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ivan Lazov <ivan.lazov@clouway.com>
 */
public class EmailAddressValidatorImpl implements EmailAddressValidator {

  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-_~]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  private Pattern pattern;
  private Matcher matcher;

  public EmailAddressValidatorImpl() {
    pattern = Pattern.compile(EMAIL_PATTERN);
  }

  @Override
  public boolean isValid(String email) {

    if (email == null) {
      return false;
    }

    matcher = pattern.matcher(email);
    return matcher.matches();
  }
}

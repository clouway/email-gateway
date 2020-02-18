package com.clouway.telcong.emailgateway.core;

/**
 * @author Krasimir Dimitrov (krasimir.dimitrov@clouway.com, kpackapgo@gmail.com)
 */
public interface EmailTemplateEvaluator {

  String evaluateSubject(Object model, String templateName);

  String evaluateBody(Object model, String templateName);

}

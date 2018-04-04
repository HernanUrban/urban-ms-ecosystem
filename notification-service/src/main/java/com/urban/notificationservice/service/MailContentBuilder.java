package com.urban.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String template, Map<String, String> variables) {
        Context context = new Context();
        variables.forEach((k, v) -> context.setVariable(k, v));
        return templateEngine.process(template, context);
    }

}

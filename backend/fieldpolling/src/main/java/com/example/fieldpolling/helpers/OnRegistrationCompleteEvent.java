package com.example.fieldpolling.helpers;

import org.springframework.context.ApplicationEvent;

import com.example.fieldpolling.models.User;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
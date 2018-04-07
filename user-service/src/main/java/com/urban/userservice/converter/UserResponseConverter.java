package com.urban.userservice.converter;

import com.urban.userservice.domain.User;
import com.urban.userservice.dto.UserResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserResponseConverter implements Converter<User, UserResponse> {

    @Override
    public UserResponse convert(User user) {
        if (user == null) return null;
        return new UserResponse()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setEnabled(user.isEnabled())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setUsername(user.getUsername());

    }
}

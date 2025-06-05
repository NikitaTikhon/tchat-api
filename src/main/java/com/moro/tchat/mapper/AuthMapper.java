package com.moro.tchat.mapper;

import com.moro.tchat.dto.request.SignupRequest;
import com.moro.tchat.entity.User;
import com.moro.tchat.mapper.annotation.EncodingMapping;
import com.moro.tchat.mapper.annotation.PasswordEncoderMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface AuthMapper {

    @Mapping(target = "password", source = "password", qualifiedBy = EncodingMapping.class)
    User signupRequestToUser(SignupRequest signupRequest);

}

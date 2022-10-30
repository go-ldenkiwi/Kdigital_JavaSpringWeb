package com.joongbu.WebSNS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.joongbu.WebSNS.dto.MailDto;
import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "b2b2007@naver.com";

    public MailDto createMailAndChangePassword(String userId , String email) {
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(email);
        dto.setTitle(userId + "님의 K-digital-project 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Codmeter 임시비밀번호 안내 관련 이메일 입니다." + "[" + userId + "]" + "님의 임시 비밀번호는 "
                + str + " 입니다.");
        updatePassword(str, userId);
        return dto;
    }

    public void updatePassword(String str, String userId) {
    	int update = 0;
        String pw = encoder.encode(str);
        System.out.println("userid = " + userId);
        UserDto user = userMapper.findbyuserId(userId);
        System.out.println("user" + user);
        user.setPw(pw);
        update = userMapper.update(user);
        System.out.println(update);
    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void mailSend(MailDto mailDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        mailSender.send(message);
    }
}

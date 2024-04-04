package com.API.User;
import org.hibernate.cache.spi.support.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailService {
	
	
	
	@Value("${spring.mail.username}") 
	private String gmail;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    CacheService cacheService;
    

    public ResponseEntity<?> joinEmail(String email) {
    	Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }
        int authNumber = Integer.parseInt(randomNumber);
        
        String setFrom = gmail;
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다."; 
        String content =
                "나의 APP을 방문해주셔서 감사합니다." + 
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요"; 
        ResponseEntity<?> entity = mailSend(setFrom, toMail, title, content,authNumber);
        return entity;
    }

    public ResponseEntity<?> mailSend(String setFrom, String toMail, String title, String content, int authNumber) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
            cacheService.evictCache(String.valueOf(authNumber),toMail);
            cacheService.cacheAuthNumber(String.valueOf(authNumber),toMail);
            return ResponseEntity.ok("요청되었습니다.");
            
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    
    public ResponseEntity<String> checkAuthNum(String email, String authNum) {
        try {
            Map<String,String> registerMail = cacheService.cacheAuthNumber(authNum,email);
            if (email.equals(registerMail.get("email")) && authNum.equals(registerMail.get("authNumber"))) {
            	cacheService.evictCache(authNum,email);
            	return ResponseEntity.ok("일치합니다.");
            }
            else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    

    public ResponseEntity<?> mailSend2(String email, String password) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
        	String setFrom = gmail;
            String toMail = email;
            String title = "새 비밀번호 설정 이메일 입니다."; 
            String content =
                    "나의 APP을 방문해주셔서 감사합니다." + 
                            "<br><br>" +
                            "비밀번호는 " + password + "입니다." +
                            "<br>" +
                           "감사합니다."; 
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
            return ResponseEntity.ok("요청되었습니다.");
            
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    
    


}
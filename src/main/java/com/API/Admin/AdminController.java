package com.API.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.Admin.DTO.BlockSiteUserDTO;
import com.API.User.MailService;
import com.API.User.UserController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
    @PostMapping("/admin/block-by-site")
    public ResponseEntity<?> blockBySite(@RequestBody BlockSiteUserDTO dto){
    	return adminService.blockBySite(dto);
    }
    

}

package com.coreon.auth.mapper;

import java.util.ArrayList;



import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.coreon.auth.dto.RegistDTO;

@Mapper
public interface AuthMemberMapper {
	
	int registProc(RegistDTO member);

	RegistDTO login(String id);




}

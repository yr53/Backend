package com.coreon.member.mapper;

import java.util.ArrayList;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.coreon.member.dto.MemberDTO;
import com.coreon.member.dto.ResponseDTO;

@Mapper
public interface IMemberMapper {

	

	int updateProc(@Param("id") String id, @Param("m") MemberDTO member);


	ArrayList<MemberDTO> memberInfo(@Param("begin")int begin, @Param("end")int end,
			@Param("select")String select, @Param("search")String search);
	

    
    // ✅ 나의 프로필 조회
    ResponseDTO getMyProfileById(@Param("id") String id);

    // ✅ 이상형 프로필 조회

	
	MemberDTO userInfoById(String id);
	
	
	
	int updateMyProfile(ResponseDTO my);



}













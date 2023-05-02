package com.dhlee.web.mapper;

import com.dhlee.message.StandardMessage;
import com.dhlee.service.DefaultInterfaceMapper;

public class TestInterfaceMapper extends DefaultInterfaceMapper {
	//------------------------------------------------------------
	// 재정의가 필요한 항목에 대한 로직 추가
	//------------------------------------------------------------
	@Override
	public String getEaiSvcCode(StandardMessage standardMessage) {
		String eaiSvcCode = null;
		String cicsTranCd = getTranCode(standardMessage);
		// TODO : site에 맞게 조합해야 함.
		eaiSvcCode = cicsTranCd +"SWEB01";
		return eaiSvcCode;
	}	
	//------------------------------------------------------------
	
	public TestInterfaceMapper() {
		
	}
}

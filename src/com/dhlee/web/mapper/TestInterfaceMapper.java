package com.dhlee.web.mapper;

import com.dhlee.message.StandardMessage;
import com.dhlee.service.DefaultInterfaceMapper;

public class TestInterfaceMapper extends DefaultInterfaceMapper {
	//------------------------------------------------------------
	// �����ǰ� �ʿ��� �׸� ���� ���� �߰�
	//------------------------------------------------------------
	@Override
	public String getEaiSvcCode(StandardMessage standardMessage) {
		String eaiSvcCode = null;
		String cicsTranCd = getTranCode(standardMessage);
		// TODO : site�� �°� �����ؾ� ��.
		eaiSvcCode = cicsTranCd +"SWEB01";
		return eaiSvcCode;
	}	
	//------------------------------------------------------------
	
	public TestInterfaceMapper() {
		
	}
}

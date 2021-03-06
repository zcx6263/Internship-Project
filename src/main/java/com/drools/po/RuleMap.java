package com.drools.po;

import java.util.Map;

import com.company.dto.DomainRuleVO;
import com.company.dto.PatientVO;

/**
 * 
 * 각 ruleID(진단명) 과 각각 Rule을 맵핑시켜서 drl에 insert 할 클래스
 *
 */
public class RuleMap {
	public Map<String, Object> ruleMap;
	
	public Map<String, Object> getRuleMap() {
		return ruleMap;
	}
	public void setRuleMap(Map<String, Object> ruleMap) {
		this.ruleMap = ruleMap;
	}
	
	public DomainRuleVO getRuleMatch(String key)
	{
		return (DomainRuleVO) ruleMap.get(key);
	}
	public boolean checkSymptom(PatientVO patient, DomainRuleVO domain) {
		
		int pLen = patient.symptomArr.size();
		int dLen = domain.symptoms.size();
		
		int andCount =0, orFlagNum =0, andFlagNum= domain.andSymptom; // or 연산자와 and 연산자 증상들을 count
		

		for(int target = 0; target < pLen; target++)
		{
			int middle=0, startIndex=0, endIndex = dLen-1;
			do // 이진 검색
			{
				if(startIndex > endIndex) break;
				
				middle = (startIndex + endIndex)/2;
				if(domain.symptoms.get(middle).getSymptom().compareTo(patient.symptomArr.get(target).getSymptom()) == 0)
				{
					if(domain.symptoms.get(middle).getFlag() == 0) {
						
						orFlagNum++;
					}
					else andCount++;
					break;
					
				}
				else if(domain.symptoms.get(middle).getSymptom().compareTo(patient.symptomArr.get(target).getSymptom()) < 0) 
				{
					startIndex = middle+1;
				}
				else if(domain.symptoms.get(middle).getSymptom().compareTo(patient.symptomArr.get(target).getSymptom()) > 0)
				{
					endIndex = middle-1;
				}
			}
			while(true);
		}
		if(andFlagNum ==0 && orFlagNum > 0) return true; // or 증상들로만 구성된 경우 : 하나만 만족한게 나와도 true
		else if( orFlagNum == 0 && andFlagNum ==0 ) return false;
		else if( andCount == andFlagNum ) return true; // or 증상들과 and 증상들이 섞여있는 경우 : and 증상들의 갯수가 모두 만족하면 true
		else return false;
	}
}

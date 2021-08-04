package com.companyname.ofbizdemo

import org.apache.ofbiz.entity.GenericEntityException

def createOfbizDemo() {
    result = [:]

    try {
        ofbizDemo = delegator.makeValue("OfbizDemo")

        // Auto generating next sequence of ofbizDemoId primary key
        ofbizDemo.setNextSeqId()

        // PK 이외의 나머지 필드 셋업
        ofbizDemo.setNonPKFields(context)

        // DB 에 record insert
        ofbizDemo = delegator.create(ofbizDemo)

        result.ofbizDemoId = ofbizDemo.ofbizDemoId

        logInfo("==========This is my first Groovy Service implementation in Apache OFBiz. "
                + "OfbizDemo record created successfully with ofbizDemoId: "
                + ofbizDemo.getString("ofbizDemoId")
        )

    } catch (GenericEntityException e) {
        logError(e.getLocalizedMessage())
        return error("Error in creating record in OfbizDemo entity ......")
    }

    return result
}

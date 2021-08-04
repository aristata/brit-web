package com.companyname.ofbizdemo.events;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OfbizDemoEvents {

    // 이 클래스의 이름을 가져와서 module 이라는 변수에 담는다.
    public static final String module = OfbizDemoEvents.class.getName();

    public static String createOfbizDemoEvent(HttpServletRequest request, HttpServletResponse response) {

        // 위임자: OFBiz 에서 제공하는 인터페이스, DB와 통신을 하는 역할
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        /*
         * 서비스 디스패처(ServiceDispatcher)는 서비스가 호출되면 적절한 서비스 엔진으로 배차하는 역할을 한다.
         * 서비스 디스패처는 많은 로컬 디스패처(LocalDispatcher)와 연결되어 있다.
         * 서비스 디스패처는 로컬 디스패처를 통해 액세스 된다.
         * 각 로컬 디스패처는 고유한 이름이 지정되며, 고유한 서비스 정의 목록을 포함한다.
         * */
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        // Generic Entity Value Object: 정의된 엔티티에 대한 영속성(persistence)을 다룬다.
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        // request 에서 각 파라미터 값을 불러온다.
        String ofbizDemoTypeId = request.getParameter("ofbizDemoTypeId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String comments = request.getParameter("comments");

        // 만약 firstName 과 lastName 중 하나라도 빈값이 있다면 에러를 반환한다.
        if (UtilValidate.isEmpty(firstName) || UtilValidate.isEmpty(lastName)) {
            String errorMessage = "First Name and Last Name are required fields on the form and can't be empty.";
            request.setAttribute("_ERROR_MESSAGE_", errorMessage);
            return "error";
        }

        try {
            String infoMessage = "========== Creating OfbizDemo record in event" +
                    " using service createOfbizDemoByGroovyService ==========";
            Debug.logInfo(infoMessage, module);

            // 디스패처를 사용하여 createOfbizDemoByGroovyService 를 호출한다. 파라미터는 맵으로 만들어 넘긴다.
            dispatcher.runSync("createOfbizDemoByGroovyService",
                    UtilMisc.toMap(
                            "ofbizDemoTypeId", ofbizDemoTypeId,
                            "firstName", firstName,
                            "lastName", lastName,
                            "comments", comments,
                            "userLogin", userLogin
                    )
            );
        } catch (GenericServiceException e) {
            String errorMessage = "Unable to create new records in OfbizDemo entity: " + e.getLocalizedMessage();
            request.setAttribute("_ERROR_MESSAGE_", errorMessage);
            return "error";
        }

        request.setAttribute("_EVENT_MESSAGE_", "OFBiz Demo created successfully.");
        return "success";
    }
}

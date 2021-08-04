/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.ofbiz.example;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ExampleServices {
    private static final String MODULE = ExampleServices.class.getName();

    public static Map<String, Object> sendExamplePushNotifications(DispatchContext dctx, Map<String, ? extends Object> context) {
        /* 서비스에서 아래와 같이 attribute 로 데이터를 전달함
         * <attribute name="exampleId" mode="IN" type="String" optional="true"></attribute>
         * <attribute name="message" mode="IN" type="String" optional="true"></attribute>
         * 전달 받은 파라미터는 Map<String, Object> 타입
         * 그래서 파라미터에서 key name 으로 값을 찾은 다음 String 타입으로 다시 캐스팅을 하여 변수에 저장한다
         */
        String exampleId = (String) context.get("exampleId");
        String message = (String) context.get("message");

        /* WebSocket 에서 client 를 가져온다.
         * javax 의 webSocket 기술을 사용
         */
        Set<Session> clients = ExampleWebSockets.getClients();
        try {
            synchronized (clients) {
                for (Session client : clients) {
                    client.getBasicRemote().sendText(message + ": " + exampleId);
                }
            }
        } catch (IOException e) {
            Debug.logError(e.getMessage(), MODULE);
        }
        return ServiceUtil.returnSuccess();
    }
}

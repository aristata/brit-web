/*
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
 */

import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.condition.EntityFieldValue
import org.apache.ofbiz.entity.condition.EntityFunction
import org.apache.ofbiz.entity.condition.EntityOperator

// 엔티티에 접근하는데 사용되는 instance
delegator = request.getAttribute("delegator")

// 조건식을 저장할 빈 배열 생성
andExprs = []

// 서비스 파라미터에서 exampleFeatureId 가져 옴
fieldValue = request.getParameter("exampleFeatureId")

// 만약 fieldValue 가 존재한다면
if (fieldValue) {
    // 배열에 조건 추가,
    // 엔티티의 exampleFeatureId 와 파라미터의 exampleFeatureId 를 Upper case 로 변환한 다음
    // 파라미터의 값을 포함하는 엔티티를 찾는 조건
    andExprs.add(
            EntityCondition.makeCondition(
                    EntityFunction.UPPER(EntityFieldValue.makeFieldValue("exampleFeatureId")),
                    EntityOperator.LIKE, "%" + fieldValue.toUpperCase() + "%"
            )
    )
}

// 조회 결과를 저장할 빈 배열 생성
autocompleteOptions = []

// 만약 조건식이 존재한다면
if (andExprs) {
    autocompleteOptions = select("exampleFeatureId", "description")
            .from("ExampleFeature")
            .where(andExprs)
            .orderBy("-exampleFeatureId")
            .queryList()
    //context.autocompleteOptions = autocompleteOptions
    request.setAttribute("autocompleteOptions", autocompleteOptions)
}
return "success"

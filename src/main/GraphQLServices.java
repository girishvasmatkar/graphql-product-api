package org.apache.ofbiz.graphql.service;

import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityFunction;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

public class GraphQLServices {
	
	public static Map<String, Object> searchProductsByGoodIdentificationValue(DispatchContext dctx,
			Map<String, Object> context) {

		Map<String, Object> serviceResult = ServiceUtil.returnSuccess();
		Delegator delegator = dctx.getDelegator();
		String idFragment = (String) context.get("idFragment");		
		List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition(
					EntityCondition.makeCondition("goodIdentificationTypeId", EntityOperator.IN,
								UtilMisc.toList("SKU", "UPC", "ISBN")),
						EntityOperator.AND,
						EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("idValue"), 
								EntityOperator.LIKE, EntityFunction.UPPER(((String) "%" + idFragment + "%").toUpperCase()))));
		try {
			List<GenericValue> productGIViewList = EntityQuery.use(delegator).from("ProductAndGoodIdentification")
					.where(exprs).queryList();
			serviceResult.put("_graphql_result_", productGIViewList);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError(e.getMessage());
		}
		return serviceResult;
	}

}

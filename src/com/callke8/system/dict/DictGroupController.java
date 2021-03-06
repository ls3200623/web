package com.callke8.system.dict;

import com.callke8.common.IController;
import com.callke8.utils.MemoryVariableUtil;
import com.callke8.utils.RenderJson;
import com.jfinal.core.Controller;

public class DictGroupController extends Controller implements IController {
	
	public void index() {
		render("list.jsp");
	}
	
	public void datagrid() {
		String groupCode = getPara("groupCode");
		String groupName = getPara("groupName");
		String state = getPara("state");
		
		int rows = Integer.valueOf(getPara("rows"));
		int page = Integer.valueOf(getPara("page"));
		
		if(page==0){page=1;}
		
		renderJson(DictGroup.dao.getDictGroupByPaginateToMap(page,rows, groupCode, groupName, state));
	}
	
	public void update() {
		
		DictGroup dictGroup = getModel(DictGroup.class,"dictgroup");
		
		int count = DictGroup.dao.update(dictGroup);
		
		if(count>0) {
			//如果修改成功时，需要重新加载数据字典的数据到内存
			MemoryVariableUtil.dictMap = DictGroup.dao.loadDictInfo();
			render(RenderJson.success("修改成功!"));
		}else {
			render(RenderJson.error("修改失败"));
		}
		
	}
	
	public void delete() {
		String ids = getPara("ids");
		
		//批量删除
		int count = DictGroup.dao.batchDelete(ids);
		
		if(count > 0) {
			//如果删除成功时，需要重新加载数据字典的数据到内存
			MemoryVariableUtil.dictMap = DictGroup.dao.loadDictInfo();
			
			render(RenderJson.success("成功删除" + count + "个数据字典组"));
		}else {
			render(RenderJson.error("删除失败"));
		}
	}

	
	public void add() {
		DictGroup dictGroup = getModel(DictGroup.class,"dictgroup");
		
		String groupCode = dictGroup.get("GROUP_CODE");
		
		//在添加之前，先检查是否已经存在相同的groupcode
		int count = DictGroup.dao.checkDictGroup(groupCode);
		
		if(count>0) {
			render(RenderJson.error("添加失败，已经存在相同的数据字典组编码!"));
			return;
		}
		
		boolean b = DictGroup.dao.add(dictGroup);
		
		if(b) {
			//如果添加成功时，需要重新加载数据字典的数据到内存
			MemoryVariableUtil.dictMap = DictGroup.dao.loadDictInfo();
			render(RenderJson.success("添加成功!"));
		}else {
			render(RenderJson.error("添加失败!"));
		}
		
	}
	
}

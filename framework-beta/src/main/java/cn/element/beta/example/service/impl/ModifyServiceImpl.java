package cn.element.beta.example.service.impl;

import cn.element.beta.example.service.IModifyService;
import cn.element.ioc.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ModifyServiceImpl implements IModifyService {

	/**
	 * 增加
	 */
	@Override
	public String add(String name,String addr) throws Exception {
		throw new Exception("这是故意抛的异常！！");
		//return "modifyService add,name=" + name + ",addr=" + addr;
	}

	/**
	 * 修改
	 */
	@Override
	public String edit(Integer id,String name) {
		return "modifyService edit,id=" + id + ",name=" + name;
	}

	/**
	 * 删除
	 */
	@Override
	public String remove(Integer id) {
		return "modifyService id =" + id;
	}
	
}

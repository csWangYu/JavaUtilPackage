package edu.hqu.utils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * @author hp
 *
 */
public class JsonFormer {

	/**
	 * 列表转Json
	 * @param list
	 * @return list in Json
	 */
	public String list2Json(List<Object> list){
		String jsonStr = "";
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			jsonStr += obj2Json(obj,true) + ",";
		}
		jsonStr = "[" + jsonStr.substring(0, jsonStr.length() - 1) + "]";
		return jsonStr;
	}
	
	/**
	 * 对象转为Json格式
	 * @param obj
	 * @param flag 
	 * @return
	 */
	private String obj2Json(Object obj,boolean flag){
		
		String objInJson = "";
		Field[] field = obj.getClass().getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			// 属性类型
			Class<?> type = field[i].getType();
			System.out.println(type.getName() + " " + field[i].getName() + ";");
			Object value = null;
			field[i].setAccessible(true);  
			if (type.isPrimitive() || isJavaClass(type)) {
				System.out.println("is primitive");
				try {
					value = field[i].get(obj);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				objInJson += "\"" + field[i].getName() + "\":\""
						+ value.toString() + "\",";
			} else {
					System.out.println("is custom Class");
					try {
						objInJson += obj2Json(field[i].get(obj),false);
					}  catch (IllegalAccessException e) {
						e.printStackTrace();
					}
			}
		}
		
		if(flag) {
			objInJson = "{" + objInJson.substring(0, objInJson.length() - 1) + "}";
		}
		return objInJson;
	}
	/**
	 * 判断是否为Java自身类
	 * @param clazz 类型
	 * @return return true if clazz is a JavaClass
	 */
	public boolean isJavaClass(Class<?> clazz) {  
	    return clazz != null && clazz.getClassLoader() == null;  
 } 
	
	
}

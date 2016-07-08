package kr.co.hucloud.utilities.web;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * JSON String을 Object로 변환해주는 유틸리티.
 * Google의 GSON이 더 간편함. GSON을 쓰는 것을 권장함.
 * 
 * @author Minchang Jang (mc.jang@hucloud.co.kr)
 *
 */
@Deprecated
public class JSONUtil {

	private static List<Class<?>> classes;
	private static List<Class<?>> collections;
	
	static {
		classes = new ArrayList<Class<?>>();
		classes.add(String.class);
		classes.add(Integer.class);
		classes.add(Long.class);
		classes.add(Double.class);
		classes.add(Boolean.class);
		classes.add(Byte.class);
		classes.add(Short.class);
		classes.add(Float.class);
		
		collections = new ArrayList<Class<?>>();
		collections.add(List.class);
		collections.add(ArrayList.class);
		collections.add(Map.class);
		collections.add(HashMap.class);
	}
	
	/**
	 * JSON String 을 returnValue에 맞게 데이터를 넣어준다.
	 * @param source
	 * @param returnType
	 * @return List Instance
	 */
	public static <T> List<T> toListInstance(String source, Class returnType) {
		JSONArray objArray = (JSONArray) convertObjectToJSON(source);
		
		if ( objArray != null ) {
			
			List<T> result = new ArrayList<T>();
			
			JSONObject obj = null;
			for ( int i = 0; i < objArray.length(); i++) {
				obj = objArray.getJSONObject(i);
				
				try {
					Object newObj = returnType.newInstance();
					Object obj2 = toInstance(obj.toString(), newObj);
					result.add((T) obj2);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e.getMessage(), e);
				} 
			}
			
			return result;
		}
		
		return null;
	}
	
	/**
	 * JSON String 을 returnValue에 맞게 데이터를 넣어준다.
	 * @param source : JSON String
	 * @param returnObject
	 * @return Instance
	 */
	public static <T> T toInstance(String source, T returnObject) {
		JSONObject obj = (JSONObject) convertObjectToJSON(source);
		
		if ( obj != null ) {
//			Field[] fields = returnObject.getClass().getDeclaredFields();
			List<Field> fieldsList1 = Arrays.asList(returnObject.getClass().getDeclaredFields());
			List<Field> fields = new ArrayList<Field>();
			fields.addAll(fieldsList1);
			
			Object tempObject = returnObject;
			
			while( !tempObject.getClass().getSuperclass().getSimpleName().equals("Object") ) {
				List<Field> fieldsList2 = Arrays.asList(returnObject.getClass().getSuperclass().getDeclaredFields());
				fields.addAll(fieldsList2);
				tempObject = tempObject.getClass().getSuperclass();
			}
			
			Iterator<String> keys = obj.keys();
			
			String key = "";
			String setter = "";
			
			Method setterMethod = null;
			Object jsonValue = null;
			
			Class<?> parameterType = null;
			
			while( keys.hasNext() ) {
				key = keys.next();
				
				for(Field field : fields) {
					
					if(field.getName().equals(key)) {
						setter = getCamelCaseMethod(field.getName());
						try {
							try {
								setterMethod = returnObject.getClass().getDeclaredMethod(setter, field.getType());
							}
							catch(NoSuchMethodException nsme) {
								Object tempObject1 = returnObject;
								while( !tempObject1.getClass().getSuperclass().getSimpleName().equals("Object") ) {
									setterMethod = returnObject.getClass().getSuperclass().getDeclaredMethod(setter, field.getType());
									tempObject1 = tempObject1.getClass().getSuperclass();
								}
							}
							parameterType = setterMethod.getParameterTypes()[0];
							
							jsonValue = getValue(obj, key);
							
							if ( classes.contains(parameterType) ) { 
								setValue(returnObject, obj, key, setterMethod, jsonValue);
							}
							else if ( collections.contains(parameterType) ) {
								
								ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
						        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
								
								if ( parameterType == List.class || parameterType == ArrayList.class ) {
									
									JSONArray arr = new JSONArray(jsonValue.toString());
									JSONObject eachObject = null;
									
									List list = new ArrayList();
									
									for ( int i = 0; i < arr.length(); i++) {
										eachObject = arr.getJSONObject(i);
										
										try {
											Object newObj = stringListClass.newInstance();
											Object obj2 = toInstance(eachObject.toString(), newObj);
											list.add(obj2);
										} catch (InstantiationException e) {
											throw new RuntimeException(e.getMessage(), e);
										}
									}
									setterMethod.invoke(returnObject, list);
								}
							}
							else {
								try {
									
									Object eachObject = null;
									Object obj2 = null;
									if ( parameterType.isPrimitive() ) {
										if ( parameterType == int.class ) {
											obj2 = Integer.parseInt(jsonValue.toString());
										}
										else if ( parameterType == long.class ) {
											obj2 = Long.parseLong(jsonValue.toString());
										}
										else if ( parameterType == short.class ) {
											obj2 = Short.parseShort(jsonValue.toString());
										}
										else if ( parameterType == byte.class ) {
											obj2 = Byte.parseByte(jsonValue.toString());
										}
										else if ( parameterType == char.class ) {
											obj2 = jsonValue.toString();
										}
										else if ( parameterType == float.class ) {
											obj2 = Float.parseFloat(jsonValue.toString());
										}
										else if ( parameterType == double.class ) {
											obj2 = Double.parseDouble(jsonValue.toString());
										}
										else if ( parameterType == boolean.class ) {
											obj2 = Boolean.parseBoolean(jsonValue.toString());
										}
									}
									else {
										eachObject = parameterType.newInstance();
										obj2 = toInstance(jsonValue.toString(), eachObject);
									}
									setterMethod.invoke(returnObject, obj2);
								} catch (InstantiationException e) {
									e.printStackTrace();
									throw new RuntimeException(e.getMessage(), e);
								}
							}
							
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | JSONException e1) {
							throw new RuntimeException(e1.getMessage(), e1);
						}
					}
				}
			}
		}
		else {
			return null;
		}
		return returnObject;
	}

	private static void setValue(Object returnObject, JSONObject obj, String key, Method setterMethod, Object jsonValue) throws IllegalAccessException, InvocationTargetException {
		
		if ( setterMethod.getParameterTypes()[0] == String.class ) {
			setterMethod.invoke(returnObject, jsonValue.toString());
		}
		else if ( setterMethod.getParameterTypes()[0] == Integer.class ) {
			setterMethod.invoke(returnObject, getInt(obj, key));
		}
		else if ( setterMethod.getParameterTypes()[0] == Long.class ) {
			setterMethod.invoke(returnObject, getLong(obj, key));
		}
		else if ( setterMethod.getParameterTypes()[0] == Double.class ) {
			setterMethod.invoke(returnObject, getDouble(obj, key));
		}
		else if ( setterMethod.getParameterTypes()[0] == Boolean.class ) {
			setterMethod.invoke(returnObject, getBoolean(obj, key));
		}
		else if ( setterMethod.getParameterTypes()[0] == Byte.class ) {
			setterMethod.invoke(returnObject, getByte(obj, key));
		}
		else if ( setterMethod.getParameterTypes()[0] == Short.class ) {
			setterMethod.invoke(returnObject, getShort(obj, key));
		}
		else if ( setterMethod.getParameterTypes()[0] == Float.class ) {
			setterMethod.invoke(returnObject, getFloat(obj, key));
		}
	}
	
	private static Object convertObjectToJSON(String source) {
		try {
			JSONObject obj = new JSONObject(source);
			return obj;
		}
		catch(JSONException jsone) {
			try {
				JSONArray objArray = new JSONArray(source);
				return objArray;
			}
			catch(JSONException jsone2) {
				throw jsone;
			}
		}
	}
	
	private static String getCamelCaseMethod(String fieldName) {
		String result = "";
		
		for(int i = 0; i < fieldName.length(); i++) {
			if ( i == 0 ) {
				result += (fieldName.charAt(i)+"").toUpperCase();
			}
			else {
				result += (fieldName.charAt(i)+"");
			}
		}
		
		return "set" + result;
	}
	
	private static Object getValue(JSONObject object, String key) {
		try {
			return object.get(key);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	private static boolean getBoolean(JSONObject object, String key) {
		return Boolean.parseBoolean(getValue(object, key).toString());
	}
	
	private static byte getByte(JSONObject object, String key) {
		return Byte.parseByte(getValue(object, key).toString());
	}
	
	private static short getShort(JSONObject object, String key) {
		return Short.parseShort(getValue(object, key).toString());
	}
	
	private static int getInt(JSONObject object, String key) {
		return Integer.parseInt(getValue(object, key).toString());
	}
	
	private static long getLong(JSONObject object, String key) {
		return Long.parseLong(getValue(object, key).toString());
	}
	
	private static float getFloat(JSONObject object, String key) {
		return Float.parseFloat(getValue(object, key).toString());
	}
	
	private static double getDouble(JSONObject object, String key) {
		return Double.parseDouble(getValue(object, key).toString());
	}
	
}

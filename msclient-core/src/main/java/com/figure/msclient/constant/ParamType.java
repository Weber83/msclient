package com.figure.msclient.constant;

import java.io.InputStream;
import java.util.Date;

public enum ParamType {
	
	STRING("String", String.class),
	INTEGER("Integer", Integer.class),
	LONG("Long", Long.class),
	SHORT("Short", Short.class),
	BOOLEAN("Boolean", Boolean.class),
	BYTE("Byte", Byte.class),
	DOUBLE("Double", Double.class),
	FLOAT("Float", Float.class),
	DATE("Date", Date.class),
	;
	
	private String typeName;
	private Class<?> clazz;
	
	ParamType(String typeName, Class<?> clazz) {
        this.typeName = typeName;
        this.clazz = clazz;
    }
	
	public static Class<?> getClazz(String typeName) {
        for (ParamType paramType : ParamType.values()) {
            if (typeName.equalsIgnoreCase(paramType.getTypeName())) {
                return paramType.getClazz();
            }
        }
        return null;
    }
	
	public static String getClazzName(String typeName) {
        for (ParamType paramType : ParamType.values()) {
            if (typeName.equalsIgnoreCase(paramType.getTypeName())) {
                return paramType.getClazz().getName();
            }
        }
        return null;
    }
	
	public static boolean hasType(String typeName){
		for (ParamType paramType : ParamType.values()) {
            if (typeName.equalsIgnoreCase(paramType.getTypeName()) || typeName.equalsIgnoreCase(paramType.getClazz().getName())) {
                return true;
            }
        }
		return false;
	}
	
	public static boolean hasType(ParamType paramType, String typeName){
		return typeName.equalsIgnoreCase(paramType.getTypeName()) 
				|| typeName.equalsIgnoreCase(paramType.getClazz().getName());
	}

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public static void main(String[] args) {
		System.out.println(int.class.getName());
	}
	
}

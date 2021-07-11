package com.kim.ilhwaland.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 커스텀 어노테이션 **/

/**
 * @interface
 * @Retention(RetentionPolicy.RUNTIME) : 메모리에 적재 
 * @Target : 어노테이션 사용을 허가하는 target을 설정
 * 사용법 : 클래스, 필드, 메서드 같은 곳에 붙이면 어노테이션으로써의 껍데기 역할을 한다.  
 **/

//사용할 위치 (타입, 필드)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {
	String headerName() default "";
}

package com.xm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xm on 2018/1/21.
 */
/** 标注此注解只能用在方法上 */
@Target(ElementType.METHOD)
/** 标注此注解生命周期是在Runtime运行时 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {
	String value();
}

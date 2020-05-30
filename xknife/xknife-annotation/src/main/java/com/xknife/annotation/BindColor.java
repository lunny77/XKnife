package com.xknife.annotation;

import androidx.annotation.ColorRes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindColor {

    /* Color resource ID to which the field will be bound. */
    @ColorRes int value();

}

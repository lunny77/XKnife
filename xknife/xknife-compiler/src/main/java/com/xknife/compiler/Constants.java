package com.xknife.compiler;

import com.squareup.javapoet.ClassName;

public class Constants {
    public static ClassName ANDROID_R = ClassName.get("android","R");

    public static ClassName ANDROID_VIEW = ClassName.get("android.view", "View");

    public static ClassName XKNIFE_UTILS = ClassName.get("com.xknife", "Utils");

    public static ClassName XKNIFE_DEBOUNCING_LISTENER = ClassName.get("com.xknife", "DebouncingOnClickListener");

}